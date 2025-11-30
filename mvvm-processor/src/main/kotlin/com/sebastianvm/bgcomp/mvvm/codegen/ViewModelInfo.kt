package com.sebastianvm.bgcomp.mvvm.codegen

import com.squareup.kotlinpoet.ClassName

/**
 * Data class holding information about a ViewModel being processed.
 *
 * @property viewModelClass The ViewModel class name
 * @property argsClass The navigation arguments class name
 * @property uiClass The UI implementation class name
 * @property propsClass The props class name (extracted from BaseViewModel)
 * @property stateClass The UI state class name (extracted from BaseViewModel)
 * @property actionClass The user action class name (extracted from BaseViewModel)
 * @property savedStateClass The saved state class name (nullable, for state restoration)
 * @property isSingleton Whether this is a singleton component (object args) or assisted injection
 *   (data class args)
 * @property packageName The package where the component should be generated
 * @property componentName The name of the generated component class
 */
data class ViewModelInfo(
    val viewModelClass: ClassName,
    val argsClass: ClassName,
    val uiClass: ClassName,
    val propsClass: ClassName,
    val stateClass: ClassName,
    val actionClass: ClassName,
    val savedStateClasses: List<ClassName>,
    val isSingleton: Boolean,
    val packageName: String,
    val componentName: String,
) {
    /**
     * Returns the property name for this component in the initializer. Converts "HomeMvvmComponent"
     * to "homeMvvmComponent"
     */
    fun componentPropertyName(): String {
        return componentName.replaceFirstChar { it.lowercase() }
    }

    /** Returns true if this component uses props (props type is not Nothing) */
    fun usesProps(): Boolean {
        return propsClass.simpleName != "Nothing"
    }
}
