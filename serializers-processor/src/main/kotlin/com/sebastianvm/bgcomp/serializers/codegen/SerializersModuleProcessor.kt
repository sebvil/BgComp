package com.sebastianvm.bgcomp.serializers.codegen

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ksp.toClassName

class SerializersModuleProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return SerializersModuleProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
        )
    }
}

/**
 * KSP processor that generates SerializersModuleProvider based on classes annotated with
 *
 * @Serializable that extend NavRouteArguments.
 *
 * Generated code includes:
 * - SerializersModuleProvider interface with @ContributesTo(AppScope::class)
 * - @Provides function that returns a SerializersModule with all argument classes registered
 */
class SerializersModuleProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private val serializableArgumentClasses = mutableListOf<ClassName>()
    private val sourceFiles = mutableSetOf<KSFile>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val serializableSymbols =
            resolver
                .getSymbolsWithAnnotation(Constants.CodeGen.SERIALIZABLE_ANNOTATION)
                .filterIsInstance<KSClassDeclaration>()

        val validSymbols = serializableSymbols.filter { it.validate() }.toList()

        // Filter only those that extend NavRouteArguments
        validSymbols.forEach { classDeclaration ->
            if (extendsNavRouteArguments(classDeclaration)) {
                serializableArgumentClasses.add(classDeclaration.toClassName())
                classDeclaration.containingFile?.let { sourceFiles.add(it) }
            }
        }

        return serializableSymbols.filterNot { it.validate() }.toList()
    }

    override fun finish() {
        if (serializableArgumentClasses.isNotEmpty()) {
            generateSerializersModuleProvider()
        }
    }

    /**
     * Determines if a class declaration extends NavRouteArguments by checking all its super types.
     */
    private fun extendsNavRouteArguments(classDeclaration: KSClassDeclaration): Boolean {
        val superTypes = classDeclaration.superTypes.map { it.resolve() }
        return superTypes.any { superType ->
            superType.declaration.qualifiedName?.asString() ==
                Constants.CodeGen.NAV_ROUTE_ARGUMENTS_QUALIFIED_NAME
        }
    }

    private fun generateSerializersModuleProvider() {
        val fileSpec = SerializersModuleBuilder(serializableArgumentClasses).build()

        val dependencies = Dependencies(aggregating = true, sources = sourceFiles.toTypedArray())

        codeGenerator
            .createNewFile(
                dependencies = dependencies,
                packageName = Constants.Packages.FEATURE_INTERFACES,
                fileName = Constants.CodeGen.GENERATED_FILE_NAME,
            )
            .use { outputStream ->
                outputStream.writer().use { writer -> fileSpec.writeTo(writer) }
            }
    }
}
