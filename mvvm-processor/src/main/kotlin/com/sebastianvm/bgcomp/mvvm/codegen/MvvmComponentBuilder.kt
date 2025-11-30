package com.sebastianvm.bgcomp.mvvm.codegen

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

/**
 * Builder for generating MvvmComponent implementation files.
 *
 * Creates component classes with proper dependency injection patterns:
 * - Singleton pattern for object-based arguments
 * - Assisted injection pattern for data class arguments
 */
class MvvmComponentBuilder(private val info: ViewModelInfo) {

    fun build(): FileSpec {
        return FileSpec.builder(info.packageName, info.componentName)
            .addType(buildComponentClass())
            .addKotlinDefaultImports()
            .build()
    }

    private fun buildComponentClass(): TypeSpec {
        return TypeSpec.classBuilder(info.componentName)
            .apply {
                if (info.isSingleton) {
                    addSingletonPattern()
                } else {
                    addAssistedPattern()
                }
            }
            .addSuperinterface(buildMvvmComponentInterface())
            .addFunction(buildContentFunction())
            .build()
    }

    private fun TypeSpec.Builder.addSingletonPattern() {
        addAnnotation(Constants.ClassNames.METRO_ASSISTED_INJECT)
        primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter(
                    ParameterSpec.builder(Constants.Names.ARGS_PARAMETER, info.argsClass)
                        .addAnnotation(Constants.ClassNames.METRO_ASSISTED)
                        .build()
                )
                .apply {
                    if (info.usesProps()) {
                        addParameter(
                            ParameterSpec.builder(
                                    Constants.Names.PROPS_PARAMETER,
                                    Constants.ClassNames.KOTLINX_STATE_FLOW.parameterizedBy(
                                        info.propsClass
                                    ),
                                )
                                .addAnnotation(Constants.ClassNames.METRO_ASSISTED)
                                .build()
                        )
                    }
                }
                .addParameter(
                    ParameterSpec.builder(
                            Constants.Names.VIEW_MODEL_FACTORY_PROPERTY,
                            ClassName("", Constants.Names.VIEW_MODEL_FACTORY_INTERFACE),
                        )
                        .build()
                )
                .build()
        )
        addProperty(
            PropertySpec.builder(Constants.Names.ARGUMENTS_PROPERTY, info.argsClass)
                .addModifiers(KModifier.OVERRIDE)
                .initializer(Constants.Names.ARGS_PARAMETER)
                .build()
        )
        if (info.usesProps()) {
            addProperty(
                PropertySpec.builder(
                        Constants.Names.PROPS_PARAMETER,
                        Constants.ClassNames.KOTLINX_STATE_FLOW.parameterizedBy(info.propsClass),
                    )
                    .initializer(Constants.Names.PROPS_PARAMETER)
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
        }
        addProperty(
            PropertySpec.builder(
                    Constants.Names.VIEW_MODEL_FACTORY_PROPERTY,
                    ClassName("", Constants.Names.VIEW_MODEL_FACTORY_INTERFACE),
                )
                .initializer(Constants.Names.VIEW_MODEL_FACTORY_PROPERTY)
                .addModifiers(KModifier.PRIVATE)
                .build()
        )

        // Add Factory interface (for singleton args, but still needs factory for props)
        addType(
            TypeSpec.funInterfaceBuilder(Constants.Names.FACTORY_INTERFACE)
                .addSuperinterface(
                    if (info.usesProps()) {
                        Constants.ClassNames.MVVM_COMPONENT_FACTORY.parameterizedBy(
                            info.argsClass,
                            info.propsClass,
                            info.stateClass,
                            info.actionClass,
                            info.uiClass,
                        )
                    } else {
                        Constants.ClassNames.MVVM_COMPONENT_NO_PROPS_FACTORY.parameterizedBy(
                            info.argsClass,
                            info.stateClass,
                            info.actionClass,
                            info.uiClass,
                        )
                    }
                )
                .addAnnotation(Constants.ClassNames.METRO_ASSISTED_FACTORY)
                .addAnnotation(
                    AnnotationSpec.builder(Constants.ClassNames.METRO_CONTRIBUTES_BINDING)
                        .addMember("%T::class", Constants.ClassNames.METRO_APP_SCOPE)
                        .build()
                )
                .addFunction(
                    FunSpec.builder(Constants.Names.CREATE_FUNCTION)
                        .addModifiers(KModifier.ABSTRACT, KModifier.OVERRIDE)
                        .addParameter(Constants.Names.ARGS_PARAMETER, info.argsClass)
                        .apply {
                            if (info.usesProps()) {
                                addParameter(
                                    Constants.Names.PROPS_PARAMETER,
                                    Constants.ClassNames.KOTLINX_STATE_FLOW.parameterizedBy(
                                        info.propsClass
                                    ),
                                )
                            }
                        }
                        .returns(ClassName(info.packageName, info.componentName))
                        .build()
                )
                .build()
        )

        // Add ViewModelFactory interface (for singleton args)
        addType(
            TypeSpec.funInterfaceBuilder(Constants.Names.VIEW_MODEL_FACTORY_INTERFACE)
                .addAnnotation(Constants.ClassNames.METRO_ASSISTED_FACTORY)
                .addFunction(
                    FunSpec.builder(Constants.Names.CREATE_FUNCTION)
                        .addModifiers(KModifier.ABSTRACT)
                        .apply {
                            if (info.usesProps()) {
                                addParameter(
                                    Constants.Names.PROPS_PARAMETER,
                                    Constants.ClassNames.KOTLINX_STATE_FLOW.parameterizedBy(
                                        info.propsClass
                                    ),
                                )
                            }
                        }
                        .apply {
                            info.savedStateClasses.forEachIndexed { index, savedStateClass ->
                                addParameter(
                                    ParameterSpec.builder(
                                            "${Constants.Names.SAVED_STATE_PARAMETER}$index",
                                            Constants.ClassNames.KOTLINX_MUTABLE_STATE_FLOW
                                                .parameterizedBy(savedStateClass),
                                        )
                                        .build()
                                )
                            }
                        }
                        .returns(info.viewModelClass)
                        .build()
                )
                .build()
        )
    }

    private fun TypeSpec.Builder.addAssistedPattern() {
        addAnnotation(Constants.ClassNames.METRO_ASSISTED_INJECT)
        primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameter(
                    ParameterSpec.builder(Constants.Names.ARGS_PARAMETER, info.argsClass)
                        .addAnnotation(Constants.ClassNames.METRO_ASSISTED)
                        .build()
                )
                .apply {
                    if (info.usesProps()) {
                        addParameter(
                            ParameterSpec.builder(
                                    Constants.Names.PROPS_PARAMETER,
                                    Constants.ClassNames.KOTLINX_STATE_FLOW.parameterizedBy(
                                        info.propsClass
                                    ),
                                )
                                .addAnnotation(Constants.ClassNames.METRO_ASSISTED)
                                .build()
                        )
                    }
                }
                .addParameter(
                    ParameterSpec.builder(
                            Constants.Names.VIEW_MODEL_FACTORY_PROPERTY,
                            ClassName("", Constants.Names.VIEW_MODEL_FACTORY_INTERFACE),
                        )
                        .build()
                )
                .build()
        )
        addProperty(
            PropertySpec.builder(Constants.Names.ARGUMENTS_PROPERTY, info.argsClass)
                .initializer(Constants.Names.ARGS_PARAMETER)
                .addModifiers(KModifier.OVERRIDE)
                .build()
        )
        if (info.usesProps()) {
            addProperty(
                PropertySpec.builder(
                        Constants.Names.PROPS_PARAMETER,
                        Constants.ClassNames.KOTLINX_STATE_FLOW.parameterizedBy(info.propsClass),
                    )
                    .initializer(Constants.Names.PROPS_PARAMETER)
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
        }
        addProperty(
            PropertySpec.builder(
                    Constants.Names.VIEW_MODEL_FACTORY_PROPERTY,
                    ClassName("", Constants.Names.VIEW_MODEL_FACTORY_INTERFACE),
                )
                .initializer(Constants.Names.VIEW_MODEL_FACTORY_PROPERTY)
                .addModifiers(KModifier.PRIVATE)
                .build()
        )

        // Add Factory interface
        addType(
            TypeSpec.funInterfaceBuilder(Constants.Names.FACTORY_INTERFACE)
                .addSuperinterface(
                    if (info.usesProps()) {
                        Constants.ClassNames.MVVM_COMPONENT_FACTORY.parameterizedBy(
                            info.argsClass,
                            info.propsClass,
                            info.stateClass,
                            info.actionClass,
                            info.uiClass,
                        )
                    } else {
                        Constants.ClassNames.MVVM_COMPONENT_NO_PROPS_FACTORY.parameterizedBy(
                            info.argsClass,
                            info.stateClass,
                            info.actionClass,
                            info.uiClass,
                        )
                    }
                )
                .addAnnotation(Constants.ClassNames.METRO_ASSISTED_FACTORY)
                .addAnnotation(
                    AnnotationSpec.builder(Constants.ClassNames.METRO_CONTRIBUTES_BINDING)
                        .addMember("%T::class", Constants.ClassNames.METRO_APP_SCOPE)
                        .build()
                )
                .addFunction(
                    FunSpec.builder(Constants.Names.CREATE_FUNCTION)
                        .addModifiers(KModifier.ABSTRACT, KModifier.OVERRIDE)
                        .addParameter(Constants.Names.ARGS_PARAMETER, info.argsClass)
                        .apply {
                            if (info.usesProps()) {
                                addParameter(
                                    Constants.Names.PROPS_PARAMETER,
                                    Constants.ClassNames.KOTLINX_STATE_FLOW.parameterizedBy(
                                        info.propsClass
                                    ),
                                )
                            }
                        }
                        .returns(ClassName(info.packageName, info.componentName))
                        .build()
                )
                .build()
        )

        // Add ViewModelFactory interface
        addType(
            TypeSpec.funInterfaceBuilder(Constants.Names.VIEW_MODEL_FACTORY_INTERFACE)
                .addAnnotation(Constants.ClassNames.METRO_ASSISTED_FACTORY)
                .addFunction(
                    FunSpec.builder(Constants.Names.CREATE_FUNCTION)
                        .addModifiers(KModifier.ABSTRACT)
                        .addParameter(Constants.Names.ARGS_PARAMETER, info.argsClass)
                        .apply {
                            if (info.usesProps()) {
                                addParameter(
                                    Constants.Names.PROPS_PARAMETER,
                                    Constants.ClassNames.KOTLINX_STATE_FLOW.parameterizedBy(
                                        info.propsClass
                                    ),
                                )
                            }
                        }
                        .apply {
                            info.savedStateClasses.forEachIndexed { index, savedStateClass ->
                                addParameter(
                                    ParameterSpec.builder(
                                            "${Constants.Names.SAVED_STATE_PARAMETER}$index",
                                            Constants.ClassNames.KOTLINX_MUTABLE_STATE_FLOW
                                                .parameterizedBy(savedStateClass),
                                        )
                                        .build()
                                )
                            }
                        }
                        .returns(info.viewModelClass)
                        .build()
                )
                .build()
        )
    }

    private fun buildMvvmComponentInterface(): com.squareup.kotlinpoet.ParameterizedTypeName {
        return Constants.ClassNames.MVVM_COMPONENT.parameterizedBy(
            info.argsClass,
            info.propsClass,
            info.stateClass,
            info.actionClass,
            info.uiClass,
        )
    }

    private fun buildContentFunction(): FunSpec {
        return FunSpec.builder(Constants.Names.CONTENT_FUNCTION)
            .addModifiers(KModifier.OVERRIDE)
            .addAnnotation(Constants.ClassNames.COMPOSE_COMPOSABLE)
            .addParameter(Constants.Names.MODIFIER_PARAMETER, Constants.ClassNames.COMPOSE_MODIFIER)
            .returns(Constants.ClassNames.KOTLIN_UNIT)
            .addCode(buildContentFunctionBody())
            .build()
    }

    private fun buildContentFunctionBody(): com.squareup.kotlinpoet.CodeBlock {
        return com.squareup.kotlinpoet.CodeBlock.builder()
            .apply {
                if (info.savedStateClasses.isEmpty()) {
                    // No saved state - simple case
                    generateContentWithoutSavedState()
                } else {
                    // With saved state - generate inline implementation
                    generateContentWithSavedState()
                }
            }
            .build()
    }

    private fun com.squareup.kotlinpoet.CodeBlock.Builder.generateContentWithoutSavedState() {
        // @Suppress("ViewModelInjection")
        addStatement("@Suppress(\"ViewModelInjection\")")

        // val viewModel = viewModel(key = arguments.toString()) { viewModelFactory.create(...) }
        addStatement(
            "val viewModel = androidx.lifecycle.viewmodel.compose.viewModel(key = %L.toString()) {",
            if (info.isSingleton) info.argsClass.simpleName else "arguments",
        )
        indent()

        // Generate viewModelFactory.create(...) call
        if (info.isSingleton) {
            if (info.usesProps()) {
                addStatement("viewModelFactory.create(props)")
            } else {
                addStatement("viewModelFactory.create()")
            }
        } else {
            if (info.usesProps()) {
                addStatement("viewModelFactory.create(arguments, props)")
            } else {
                addStatement("viewModelFactory.create(arguments)")
            }
        }

        unindent()
        addStatement("}")
        addStatement("")

        // Generate Content call
        addStatement("com.sebastianvm.bgcomp.mvvm.Content(")
        indent()
        addStatement("viewModel = viewModel,")
        addStatement("ui = %T,", info.uiClass)
        addStatement("modifier = modifier,")
        unindent()
        addStatement(")")
    }

    private fun com.squareup.kotlinpoet.CodeBlock.Builder.generateContentWithSavedState() {
        // Generate currentSerializersModule and saveStateConfig
        addStatement(
            "val currentSerializersModule = com.sebastianvm.bgcomp.mvvm.LocalSerializersModule.current"
        )
        addStatement(
            "val saveStateConfig = androidx.savedstate.serialization.SavedStateConfiguration {"
        )
        indent()
        addStatement("this.serializersModule = currentSerializersModule")
        unindent()
        addStatement("}")
        addStatement("")

        // @Suppress("ViewModelInjection")
        addStatement("@Suppress(\"ViewModelInjection\")")

        // val viewModel = viewModel(key = arguments.toString()) { ... }
        addStatement(
            "val viewModel = androidx.lifecycle.viewmodel.compose.viewModel(key = %L.toString()) {",
            if (info.isSingleton) info.argsClass.simpleName else "arguments",
        )
        indent()

        // val handle = createSavedStateHandle()
        addStatement(
            "val handle = %M()",
            MemberName("androidx.lifecycle", "createSavedStateHandle"),
        )

        // Generate MutableStateFlow for each saved state
        info.savedStateClasses.forEachIndexed { index, savedStateClass ->
            addStatement("")
            addStatement("val savedState$index = kotlinx.coroutines.flow.MutableStateFlow(")
            indent()
            addStatement("handle.get<androidx.savedstate.SavedState>(\"savedState$index\")?.let {")
            indent()
            addStatement(
                "androidx.savedstate.serialization.decodeFromSavedState<%T>(it, configuration = saveStateConfig)",
                savedStateClass,
            )
            unindent()
            addStatement("} ?: %T()", savedStateClass)
            unindent()
            addStatement(")")

            // Set saved state provider
            addStatement("handle.setSavedStateProvider(\"savedState$index\") {")
            indent()
            addStatement(
                "androidx.savedstate.serialization.encodeToSavedState(savedState$index.value, configuration = saveStateConfig)"
            )
            unindent()
            addStatement("}")
        }

        addStatement("")

        // Generate viewModelFactory.create(...) call with all saved states
        val savedStateParams = info.savedStateClasses.indices.joinToString(", ") { "savedState$it" }
        if (info.isSingleton) {
            if (info.usesProps()) {
                addStatement("viewModelFactory.create(props, $savedStateParams)")
            } else {
                addStatement("viewModelFactory.create($savedStateParams)")
            }
        } else {
            if (info.usesProps()) {
                addStatement("viewModelFactory.create(arguments, props, $savedStateParams)")
            } else {
                addStatement("viewModelFactory.create(arguments, $savedStateParams)")
            }
        }

        unindent()
        addStatement("}")
        addStatement("")

        // Generate Content call
        addStatement("com.sebastianvm.bgcomp.mvvm.Content(")
        indent()
        addStatement("viewModel = viewModel,")
        addStatement("ui = %T,", info.uiClass)
        addStatement("modifier = modifier,")
        unindent()
        addStatement(")")
    }
}
