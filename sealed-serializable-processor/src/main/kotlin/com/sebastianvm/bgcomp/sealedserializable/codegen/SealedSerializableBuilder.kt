package com.sebastianvm.bgcomp.sealedserializable.codegen

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

class SealedSerializableBuilder(
    private val sealedInterface: ClassName,
    private val subclasses: List<ClassName>,
) {

    fun build(): FileSpec {
        val fileName = "${sealedInterface.simpleName}SerializerModuleProvider"

        return FileSpec.builder(sealedInterface.packageName, fileName)
            .addImport(
                Constants.Packages.KOTLINX_SERIALIZATION_MODULES,
                Constants.MemberNames.SERIALIZATION_POLYMORPHIC.simpleName,
            )
            .addImport(
                Constants.Packages.KOTLINX_SERIALIZATION_MODULES,
                Constants.MemberNames.SERIALIZATION_SUBCLASS.simpleName,
            )
            .addType(buildProviderInterface(fileName))
            .build()
    }

    private fun buildProviderInterface(interfaceName: String): TypeSpec {
        return TypeSpec.interfaceBuilder(interfaceName)
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
        return FunSpec.builder(
                "${sealedInterface.simpleName.lowercase()}${Constants.Names.SERIALIZERS_MODULE_FUNCTION}"
            )
            .addAnnotation(Constants.ClassNames.METRO_INTO_SET)
            .addAnnotation(Constants.ClassNames.METRO_PROVIDES)
            .returns(Constants.ClassNames.SERIALIZERS_MODULE)
            .addCode(buildFunctionBody())
            .build()
    }

    private fun buildFunctionBody(): CodeBlock {
        return CodeBlock.builder()
            .beginControlFlow("return %T", Constants.ClassNames.SERIALIZERS_MODULE)
            .beginControlFlow(
                "%M(%T::class)",
                Constants.MemberNames.SERIALIZATION_POLYMORPHIC,
                sealedInterface,
            )
            .apply {
                subclasses.forEach { subclass ->
                    addStatement(
                        "%M(%T::class)",
                        Constants.MemberNames.SERIALIZATION_SUBCLASS,
                        subclass,
                    )
                }
            }
            .endControlFlow()
            .endControlFlow()
            .build()
    }
}
