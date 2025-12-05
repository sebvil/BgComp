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
        const val MVVM = "com.sebastianvm.bgcomp.mvvm"
        const val MVVM_CODEGEN = "com.sebastianvm.bgcomp.mvvm.codegen"

        // Dependency injection
        const val METRO = "dev.zacsweers.metro"

        // Compose
        const val COMPOSE_RUNTIME = "androidx.compose.runtime"
        const val COMPOSE_UI = "androidx.compose.ui"
    }

    /** Commonly used class names organized by library/module */
    object ClassNames {
        // Metro DI
        val METRO_CONTRIBUTES_BINDING = ClassName(Packages.METRO, "ContributesBinding")
        val METRO_APP_SCOPE = ClassName(Packages.METRO, "AppScope")
        val METRO_ASSISTED_INJECT = ClassName(Packages.METRO, "AssistedInject")
        val METRO_ASSISTED = ClassName(Packages.METRO, "Assisted")
        val METRO_ASSISTED_FACTORY = ClassName(Packages.METRO, "AssistedFactory")

        // Compose
        val COMPOSE_COMPOSABLE = ClassName(Packages.COMPOSE_RUNTIME, "Composable")
        val COMPOSE_MODIFIER = ClassName(Packages.COMPOSE_UI, "Modifier")

        val KOTLIN_UNIT = com.squareup.kotlinpoet.UNIT

        // Kotlinx
        val KOTLINX_STATE_FLOW = ClassName("kotlinx.coroutines.flow", "StateFlow")

        // MVVM Framework
        val MVVM_COMPONENT = ClassName(Packages.MVVM, "MvvmComponent")
        val MVVM_COMPONENT_FACTORY = ClassName(Packages.MVVM, "MvvmComponent", "Factory")
        val MVVM_COMPONENT_NO_PROPS_FACTORY =
            ClassName(Packages.MVVM, "MvvmComponent", "NoPropsFactory")
    }


    /** Function and property names used in generated code */
    object Names {
        const val CONTENT_FUNCTION = "Content"
        const val VIEW_MODEL_FACTORY_PROPERTY = "viewModelFactory"
        const val ARGUMENTS_PROPERTY = "arguments"
        const val PROPS_PARAMETER = "props"
        const val ARGS_PARAMETER = "arguments"
        const val MODIFIER_PARAMETER = "modifier"
        const val FACTORY_INTERFACE = "Factory"
        const val VIEW_MODEL_FACTORY_INTERFACE = "ViewModelFactory"
        const val CREATE_FUNCTION = "create"
    }

    /** Annotation and code generation constants */
    object CodeGen {
        const val ANNOTATION_QUALIFIED_NAME = "${Packages.MVVM_CODEGEN}.MvvmComponent"
        const val ANNOTATION_SHORT_NAME = "MvvmComponent"
        const val BASE_VIEW_MODEL_QUALIFIED_NAME = "${Packages.MVVM}.BaseViewModel"
        const val VIEW_MODEL_SUFFIX = "ViewModel"
        const val MVVM_COMPONENT_SUFFIX = "MvvmComponent"
    }
}
