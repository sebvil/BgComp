package com.sebastianvm.bgcomp.sealedserializable.codegen

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
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName

class SealedSerializableProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return SealedSerializableProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
    }
}

class SealedSerializableProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    data class SealedInterfaceInfo(
        val declaration: KSClassDeclaration,
        val subclasses: List<ClassName>,
        val sourceFile: KSFile,
    )

    private val sealedInterfaces = mutableListOf<SealedInterfaceInfo>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val sealedSerializableSymbols =
            resolver
                .getSymbolsWithAnnotation(Constants.CodeGen.SEALED_SERIALIZABLE_ANNOTATION)
                .filterIsInstance<KSClassDeclaration>()

        val validSymbols = sealedSerializableSymbols.filter { it.validate() }.toList()

        validSymbols.forEach { classDeclaration ->
            if (isSealedInterface(classDeclaration)) {
                val subclasses = findSealedSubclasses(classDeclaration)
                if (subclasses.isNotEmpty()) {
                    sealedInterfaces.add(
                        SealedInterfaceInfo(
                            declaration = classDeclaration,
                            subclasses = subclasses,
                            sourceFile = classDeclaration.containingFile!!,
                        )
                    )
                } else {
                    logger.warn(
                        "@SealedSerializable on ${classDeclaration.qualifiedName?.asString()} " +
                            "has no @Serializable subclasses"
                    )
                }
            } else {
                logger.warn(
                    "@SealedSerializable should only be applied to sealed interfaces, " +
                        "but was applied to ${classDeclaration.qualifiedName?.asString()}"
                )
            }
        }

        return sealedSerializableSymbols.filterNot { it.validate() }.toList()
    }

    override fun finish() {
        sealedInterfaces.forEach { info -> generateSerializerModuleProvider(info) }
    }

    /** Checks if a class declaration is a sealed interface. */
    private fun isSealedInterface(classDeclaration: KSClassDeclaration): Boolean {
        return classDeclaration.classKind == ClassKind.INTERFACE &&
            classDeclaration.modifiers.contains(Modifier.SEALED)
    }

    /** Finds all direct subclasses of a sealed interface that are annotated with @Serializable. */
    private fun findSealedSubclasses(sealedInterface: KSClassDeclaration): List<ClassName> {
        val subclasses = mutableListOf<ClassName>()

        sealedInterface.getSealedSubclasses().forEach { subclass ->
            // Check if the subclass is annotated with @Serializable
            val hasSerializable =
                subclass.annotations.any { annotation ->
                    annotation.annotationType.resolve().declaration.qualifiedName?.asString() ==
                        Constants.CodeGen.SERIALIZABLE_ANNOTATION
                }

            if (hasSerializable) {
                subclasses.add(subclass.toClassName())
            }
        }

        return subclasses
    }

    private fun generateSerializerModuleProvider(info: SealedInterfaceInfo) {
        val fileSpec =
            SealedSerializableBuilder(
                    sealedInterface = info.declaration.toClassName(),
                    subclasses = info.subclasses,
                )
                .build()

        val dependencies = Dependencies(aggregating = false, sources = arrayOf(info.sourceFile))

        val packageName = info.declaration.packageName.asString()
        val fileName = "${info.declaration.simpleName.asString()}SerializerModuleProvider"

        codeGenerator
            .createNewFile(
                dependencies = dependencies,
                packageName = packageName,
                fileName = fileName,
            )
            .use { outputStream ->
                outputStream.writer().use { writer -> fileSpec.writeTo(writer) }
            }
    }
}
