package com.sebastianvm.bgcomp.serializers.codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName

/**
 * Constants used throughout the serializers module code generation process. Centralizes all class
 * names, member names, and other constants to improve maintainability.
 */
object Constants {

    /** Package names for generated code */
    object Packages {
        // Project packages
        const val FEATURE_INTERFACES = "com.sebastianvm.bgcomp.featureinterfaces"
        const val NAVIGATION = "com.sebastianvm.bgcomp.navigation"
        const val MVVM = "com.sebastianvm.bgcomp.mvvm"

        // Dependency injection
        const val METRO = "dev.zacsweers.metro"

        // Navigation
        const val NAV3_RUNTIME = "androidx.navigation3.runtime"

        // Serialization
        const val KOTLINX_SERIALIZATION = "kotlinx.serialization"
        const val KOTLINX_SERIALIZATION_MODULES = "kotlinx.serialization.modules"
    }

    /** Commonly used class names organized by library/module */
    object ClassNames {
        // Metro DI
        val METRO_CONTRIBUTES_TO = ClassName(Packages.METRO, "ContributesTo")
        val METRO_APP_SCOPE = ClassName(Packages.METRO, "AppScope")
        val METRO_PROVIDES = ClassName(Packages.METRO, "Provides")
        val METRO_INTO_SET = ClassName(Packages.METRO, "IntoSet")

        // Navigation
        val ARGUMENTS = ClassName(Packages.MVVM, "MvvmComponentArguments")
        val NAV3_NAV_KEY = ClassName(Packages.NAV3_RUNTIME, "NavKey")

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
        const val PROVIDER_INTERFACE = "SerializersModuleProvider"
    }

    /** Annotation and code generation constants */
    object CodeGen {
        const val GENERATED_FILE_NAME = "ArgumentsSerializersModule"
        const val SERIALIZABLE_ANNOTATION = "${Packages.KOTLINX_SERIALIZATION}.Serializable"
        const val NAV_ROUTE_ARGUMENTS_QUALIFIED_NAME = "${Packages.MVVM}.MvvmComponentArguments"
    }
}
