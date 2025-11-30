package com.sebastianvm.bgcomp.mvvm.codegen

import kotlin.reflect.KClass

/**
 * Annotation to mark ViewModels for automatic MvvmComponent generation.
 *
 * The KSP processor will generate:
 * - An MvvmComponent implementation
 * - A ViewModelFactory (for assisted injection when args are present)
 * - Registration in the MvvmComponentInitializer
 *
 * @param argsClass The NavRouteArguments class for this component
 * @param uiClass The Ui implementation class for this component
 * @param savedStateClass Optional saved state class for state restoration (nullable)
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class MvvmComponent(
    val argsClass: KClass<*>,
    val uiClass: KClass<*>,
    val savedStateClasses: Array<KClass<*>> = [],
)
