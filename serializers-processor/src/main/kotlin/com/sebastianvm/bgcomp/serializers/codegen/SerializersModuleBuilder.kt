package com.sebastianvm.bgcomp.serializers.codegen

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

/**
 * Builder for generating SerializersModuleProvider interface file.
 *
 * Creates an interface with proper Metro DI annotations that provides a SerializersModule
 * containing all NavRouteArguments implementations registered for polymorphic serialization.
 */
class SerializersModuleBuilder(private val argumentClasses: List<ClassName>) {

    fun build(): FileSpec {
        return FileSpec.builder(
                Constants.Packages.FEATURE_INTERFACES,
                Constants.CodeGen.GENERATED_FILE_NAME,
            )
            .addImport(
                Constants.Packages.KOTLINX_SERIALIZATION_MODULES,
                Constants.MemberNames.SERIALIZATION_POLYMORPHIC.simpleName,
            )
            .addImport(
                Constants.Packages.KOTLINX_SERIALIZATION_MODULES,
                Constants.MemberNames.SERIALIZATION_SUBCLASS.simpleName,
            )
            .addType(buildProviderInterface())
            .build()
    }

    private fun buildProviderInterface(): TypeSpec {
        return TypeSpec.interfaceBuilder(Constants.Names.PROVIDER_INTERFACE)
            .addAnnotation(buildContributesToAnnotation())
            .addFunction(buildSerializersModuleFunction())
            .build()
    }

    private fun buildContributesToAnnotation(): AnnotationSpec {
        return AnnotationSpec.builder(Constants.ClassNames.METRO_CONTRIBUTES_TO)
            .addMember("%T::class", Constants.ClassNames.METRO_APP_SCOPE)
            .build()
    }

    private fun buildSerializersModuleFunction(): FunSpec {
        return FunSpec.builder(Constants.Names.SERIALIZERS_MODULE_FUNCTION)
            .addAnnotation(Constants.ClassNames.METRO_PROVIDES)
            .addAnnotation(Constants.ClassNames.METRO_INTO_SET)
            .returns(Constants.ClassNames.SERIALIZERS_MODULE)
            .addCode(buildFunctionBody())
            .build()
    }

    private fun buildFunctionBody(): CodeBlock {
        return CodeBlock.builder()
            .beginControlFlow("return %T", Constants.ClassNames.SERIALIZERS_MODULE)
            // Add polymorphic block for NavRouteArguments
            .beginControlFlow(
                "%M(%T::class)",
                Constants.MemberNames.SERIALIZATION_POLYMORPHIC,
                Constants.ClassNames.ARGUMENTS,
            )
            .apply {
                // Add each argument class
                argumentClasses.forEach { argClass ->
                    addStatement(
                        "%M(%T::class)",
                        Constants.MemberNames.SERIALIZATION_SUBCLASS,
                        argClass,
                    )
                }
            }
            .endControlFlow()
            .endControlFlow()
            .build()
    }
}
