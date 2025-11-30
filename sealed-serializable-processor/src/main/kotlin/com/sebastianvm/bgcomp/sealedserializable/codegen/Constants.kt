package com.sebastianvm.bgcomp.sealedserializable.codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName

/** Constants used throughout the sealed serializable code generation process. */
object Constants {

    /** Package names for generated code */
    object Packages {
        // Dependency injection
        const val METRO = "dev.zacsweers.metro"

        // Serialization
        const val KOTLINX_SERIALIZATION = "kotlinx.serialization"
        const val KOTLINX_SERIALIZATION_MODULES = "kotlinx.serialization.modules"

        // Annotations
        const val CODEGEN_ANNOTATIONS = "com.sebastianvm.bgcomp.mvvm.codegen"
    }

    /** Commonly used class names */
    object ClassNames {
        // Metro DI
        val METRO_CONTRIBUTES_TO = ClassName(Packages.METRO, "ContributesTo")
        val METRO_APP_SCOPE = ClassName(Packages.METRO, "AppScope")
        val METRO_PROVIDES = ClassName(Packages.METRO, "Provides")
        val METRO_INTO_SET = ClassName(Packages.METRO, "IntoSet")

        // Serialization
        val SERIALIZERS_MODULE =
            ClassName(Packages.KOTLINX_SERIALIZATION_MODULES, "SerializersModule")
    }

    /** Member names (functions/properties) for imports */
    object MemberNames {
        val SERIALIZATION_POLYMORPHIC =
            MemberName(Packages.KOTLINX_SERIALIZATION_MODULES, "polymorphic")
        val SERIALIZATION_SUBCLASS = MemberName(Packages.KOTLINX_SERIALIZATION_MODULES, "subclass")
    }

    /** Function and property names used in generated code */
    object Names {
        const val SERIALIZERS_MODULE_FUNCTION = "serializersModule"
    }

    /** Annotation and code generation constants */
    object CodeGen {
        const val SERIALIZABLE_ANNOTATION = "${Packages.KOTLINX_SERIALIZATION}.Serializable"
        const val SEALED_SERIALIZABLE_ANNOTATION =
            "${Packages.CODEGEN_ANNOTATIONS}.SealedSerializable"
    }
}
