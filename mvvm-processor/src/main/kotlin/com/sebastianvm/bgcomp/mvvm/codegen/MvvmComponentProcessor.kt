package com.sebastianvm.bgcomp.mvvm.codegen

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ksp.toClassName

class MvvmComponentProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return MvvmComponentProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
    }
}

/**
 * KSP processor that generates MvvmComponent implementations and MvvmComponentInitializer from
 * ViewModels annotated with @MvvmComponent.
 *
 * Generated code includes:
 * - MvvmComponent implementation with proper DI pattern (singleton or assisted)
 * - Factory interfaces for assisted injection
 * - MvvmComponentInitializer with routing and serialization
 */
class MvvmComponentProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private val processedViewModels = mutableListOf<ViewModelInfo>()
    lateinit var dependencies: Dependencies

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols =
            resolver
                .getSymbolsWithAnnotation(Constants.CodeGen.ANNOTATION_QUALIFIED_NAME)
                .filterIsInstance<KSClassDeclaration>()

        val validSymbols = symbols.filter { it.validate() }.toList()
        validSymbols.forEach { viewModelClass ->
            val vmDependency = viewModelClass.containingFile!!
            processViewModel(viewModelClass, Dependencies(aggregating = false, vmDependency))
        }
        dependencies =
            Dependencies(
                aggregating = false,
                *symbols.map { it.containingFile!! }.toList().toTypedArray(),
            )

        return symbols.filterNot { it.validate() }.toList()
    }

    private fun processViewModel(viewModelClass: KSClassDeclaration, dependencies: Dependencies) {
        val annotation =
            viewModelClass.annotations.first {
                it.shortName.asString() == Constants.CodeGen.ANNOTATION_SHORT_NAME
            }

        val argsType =
            annotation.arguments.first { it.name?.asString() == "argsClass" }.value as KSType

        val uiType = annotation.arguments.first { it.name?.asString() == "uiClass" }.value as KSType

        val savedStateTypes =
            (annotation.arguments.firstOrNull { it.name?.asString() == "savedStateClasses" }?.value
                    as? List<*>)
                ?.filterIsInstance<KSType>()
                .orEmpty()

        val argsClassName = argsType.toClassName()
        val uiClassName = uiType.toClassName()
        val viewModelClassName = viewModelClass.toClassName()

        // Only include savedState if it's not Unit::class (the default)
        val savedStateClassNames =
            savedStateTypes.map {
                val qualifiedName = it.declaration.qualifiedName?.asString()
                // Validate that the saved state class has a zero-arg constructor
                val savedStateDeclaration = it.declaration as? KSClassDeclaration
                if (savedStateDeclaration != null) {
                    val hasZeroArgConstructor =
                        savedStateDeclaration.primaryConstructor?.parameters?.all { param ->
                            param.hasDefault
                        } ?: false

                    if (!hasZeroArgConstructor) {
                        logger.error(
                            "SavedState class $qualifiedName must have a zero-argument constructor (all parameters must have default values)",
                            savedStateDeclaration,
                        )
                    }
                }
                it.toClassName()
            }

        // Determine the props, state and action types from the ViewModel's BaseViewModel supertype
        // Handle both direct BaseViewModel inheritance and indirect inheritance through
        // BaseNavHostViewModel
        val baseViewModelType =
            viewModelClass.superTypes
                .map { it.resolve() }
                .firstOrNull {
                    it.declaration.qualifiedName?.asString() ==
                        Constants.CodeGen.BASE_VIEW_MODEL_QUALIFIED_NAME
                }

        val (propsType, stateType, actionType) =
            if (baseViewModelType != null) {
                // Direct BaseViewModel inheritance - extract props, state, action from
                // BaseViewModel
                Triple(
                    baseViewModelType.arguments[0].type!!.resolve().toClassName(),
                    baseViewModelType.arguments[1].type!!.resolve().toClassName(),
                    baseViewModelType.arguments[2].type!!.resolve().toClassName(),
                )
            } else {
                // Check for BaseNavHostViewModel
                val navHostViewModel =
                    viewModelClass.superTypes
                        .map { it.resolve() }
                        .firstOrNull {
                            it.declaration.qualifiedName?.asString() ==
                                "com.sebastianvm.bgcomp.navigation.viewmodel.BaseNavHostViewModel"
                        }

                if (navHostViewModel != null) {
                    // BaseNavHostViewModel<ParentProps, ChildrenProps> extends
                    // BaseViewModel<ParentProps, NavHostState, NavHostUserAction>
                    // Extract ParentProps from BaseNavHostViewModel (first type parameter)
                    val parentPropsType =
                        navHostViewModel.arguments[0].type!!.resolve().toClassName()

                    // Get state and action from the BaseViewModel that BaseNavHostViewModel extends
                    val baseVmType =
                        (navHostViewModel.declaration as KSClassDeclaration)
                            .superTypes
                            .map { it.resolve() }
                            .first {
                                it.declaration.qualifiedName?.asString() ==
                                    Constants.CodeGen.BASE_VIEW_MODEL_QUALIFIED_NAME
                            }

                    Triple(
                        parentPropsType, // Use ParentProps from BaseNavHostViewModel
                        baseVmType.arguments[1].type!!.resolve().toClassName(),
                        baseVmType.arguments[2].type!!.resolve().toClassName(),
                    )
                } else {
                    logger.error(
                        "ViewModel must extend BaseViewModel or BaseNavHostViewModel",
                        viewModelClass,
                    )
                    throw IllegalStateException(
                        "ViewModel ${viewModelClass.qualifiedName?.asString()} does not extend BaseViewModel or BaseNavHostViewModel"
                    )
                }
            }

        val viewModelInfo =
            ViewModelInfo(
                viewModelClass = viewModelClassName,
                argsClass = argsClassName,
                uiClass = uiClassName,
                propsClass = propsType,
                stateClass = stateType,
                actionClass = actionType,
                savedStateClasses = savedStateClassNames,
                isSingleton = isDataObject(argsType.declaration as KSClassDeclaration),
                packageName = viewModelClass.packageName.asString(),
                componentName =
                    "${
                        viewModelClass.simpleName.asString().removeSuffix(Constants.CodeGen.VIEW_MODEL_SUFFIX)
                    }${Constants.CodeGen.MVVM_COMPONENT_SUFFIX}",
            )

        processedViewModels.add(viewModelInfo)
        generateMvvmComponent(viewModelInfo, dependencies)
    }

    /**
     * Determines if the arguments class is an object declaration (singleton pattern) vs a data
     * class (assisted injection pattern).
     */
    private fun isDataObject(declaration: KSClassDeclaration): Boolean {
        return declaration.classKind == ClassKind.OBJECT
    }

    private fun generateMvvmComponent(info: ViewModelInfo, dependencies: Dependencies) {
        val fileSpec = MvvmComponentBuilder(info).build()

        codeGenerator
            .createNewFile(
                dependencies = dependencies,
                packageName = info.packageName,
                fileName = info.componentName,
            )
            .use { outputStream ->
                outputStream.writer().use { writer -> fileSpec.writeTo(writer) }
            }
    }
}
