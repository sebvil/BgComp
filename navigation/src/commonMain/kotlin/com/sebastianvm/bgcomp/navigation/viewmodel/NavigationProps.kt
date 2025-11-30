package com.sebastianvm.bgcomp.navigation.viewmodel

import com.sebastianvm.bgcomp.mvvm.MvvmComponentArguments
import com.sebastianvm.bgcomp.mvvm.Props
import com.sebastianvm.bgcomp.navigation.NavDestination
import com.sebastianvm.bgcomp.navigation.PresentationModes

data class NavigationProps(val push: (NavDestination, Boolean) -> Unit, val pop: () -> Unit) : Props

interface HasNavigationProps {

    fun navigateTo(
        arguments: MvvmComponentArguments,
        presentationModes: PresentationModes = PresentationModes(),
        popCurrent: Boolean = false,
    )

    fun navigateUp()

    class Default(private val props: NavigationProps) : HasNavigationProps {
        override fun navigateTo(
            arguments: MvvmComponentArguments,
            presentationModes: PresentationModes,
            popCurrent: Boolean,
        ) = props.push(NavDestination(arguments, presentationModes), popCurrent)

        override fun navigateUp() = props.pop()
    }
}
