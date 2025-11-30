package com.sebastianvm.bgcomp.mvvm.codegen

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName

/**
 * Constants used throughout the code generation process. Centralizes all class names, member names,
 * and other constants to improve maintainability.
 */
object Constants {

    /** Package names for generated code */
    object Packages {
        // Project packages
        const val GENERATED_DI = "com.sebastianvm.bgcomp.di"
        const val MVVM = "com.sebastianvm.bgcomp.mvvm"
        const val MVVM_CODEGEN = "com.sebastianvm.bgcomp.mvvm.codegen"
        const val NAVIGATION = "com.sebastianvm.bgcomp.navigation"

        // Dependency injection
        const val METRO = "dev.zacsweers.metro"

        // Compose
        const val COMPOSE_RUNTIME = "androidx.compose.runtime"
        const val COMPOSE_UI = "androidx.compose.ui"

        // Navigation
        const val NAV3_RUNTIME = "androidx.navigation3.runtime"

        // Kotlin
        const val KOTLIN = "kotlin"

        // Serialization
        const val KOTLINX_SERIALIZATION_MODULES = "kotlinx.serialization.modules"
    }

    /** Commonly used class names organized by library/module */
    object ClassNames {
        // Metro DI
        val METRO_CONTRIBUTES_BINDING = ClassName(Packages.METRO, "ContributesBinding")
        val METRO_APP_SCOPE = ClassName(Packages.METRO, "AppScope")
        val METRO_INJECT = ClassName(Packages.METRO, "Inject")
        val METRO_ASSISTED_INJECT = ClassName(Packages.METRO, "AssistedInject")
        val METRO_ASSISTED = ClassName(Packages.METRO, "Assisted")
        val METRO_ASSISTED_FACTORY = ClassName(Packages.METRO, "AssistedFactory")
        val METRO_PROVIDER = ClassName(Packages.METRO, "Provider")

        // Compose
        val COMPOSE_COMPOSABLE = ClassName(Packages.COMPOSE_RUNTIME, "Composable")
        val COMPOSE_MODIFIER = ClassName(Packages.COMPOSE_UI, "Modifier")

        // Kotlin stdlib
        val KOTLIN_LAZY = ClassName(Packages.KOTLIN, "Lazy")
        val KOTLIN_UNIT = com.squareup.kotlinpoet.UNIT

        // Kotlinx
        val KOTLINX_MUTABLE_STATE_FLOW = ClassName("kotlinx.coroutines.flow", "MutableStateFlow")
        val KOTLINX_STATE_FLOW = ClassName("kotlinx.coroutines.flow", "StateFlow")

        // MVVM Framework
        val MVVM_COMPONENT = ClassName(Packages.MVVM, "MvvmComponent")
        val MVVM_COMPONENT_FACTORY = ClassName(Packages.MVVM, "MvvmComponent", "Factory")
        val MVVM_COMPONENT_NO_PROPS_FACTORY =
            ClassName(Packages.MVVM, "MvvmComponent", "NoPropsFactory")
        val MVVM_NAV_EVENT = ClassName(Packages.MVVM, "NavEvent")
        val MVVM_COMPONENT_INITIALIZER = ClassName(Packages.MVVM, "MvvmComponentInitializer")

        // Navigation
        val NAV_ROUTE_ARGUMENTS = ClassName(Packages.NAVIGATION, "NavRouteArguments")
        val NAV_DESTINATION = ClassName(Packages.NAVIGATION, "NavDestination")
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
        const val CONTENT_FUNCTION = "Content"
        const val INITIALIZE_FUNCTION = "initialize"
        const val VIEW_MODEL_PROPERTY = "viewModel"
        const val VIEW_MODEL_FACTORY_PROPERTY = "viewModelFactory"
        const val ARGUMENTS_PROPERTY = "arguments"
        const val PROPS_PARAMETER = "props"
        const val ARGS_PARAMETER = "arguments"
        const val SAVED_STATE_PARAMETER = "savedState"
        const val INITIAL_SAVED_STATE_PARAMETER = "initialSavedState"
        const val ON_NAV_EVENT_PARAMETER = "onNavEvent"
        const val MODIFIER_PARAMETER = "modifier"
        const val SERIALIZER_MODULE_PROPERTY = "serializerModule"
        const val FACTORY_INTERFACE = "Factory"
        const val NO_PROPS_FACTORY_INTERFACE = "NoPropsFactory"
        const val VIEW_MODEL_FACTORY_INTERFACE = "ViewModelFactory"
        const val CREATE_FUNCTION = "create"
    }

    /** Annotation and code generation constants */
    object CodeGen {
        const val GENERATED_CLASS_NAME = "GeneratedMvvmComponentInitializer"
        const val ANNOTATION_QUALIFIED_NAME = "${Packages.MVVM_CODEGEN}.MvvmComponent"
        const val ANNOTATION_SHORT_NAME = "MvvmComponent"
        const val BASE_VIEW_MODEL_QUALIFIED_NAME = "${Packages.MVVM}.BaseViewModel"
        const val VIEW_MODEL_SUFFIX = "ViewModel"
        const val MVVM_COMPONENT_SUFFIX = "MvvmComponent"
        const val ERROR_MESSAGE_NOT_REGISTERED = $$"$args not registered"
    }
}
