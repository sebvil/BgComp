package com.sebastianvm.bgcomp.navigation.viewmodel

import com.sebastianvm.bgcomp.mvvm.MvvmComponentArguments
import com.sebastianvm.bgcomp.navigation.NavDestination
import com.sebastianvm.bgcomp.navigation.NavTransition
import com.sebastianvm.bgcomp.navigation.PresentationMode
import com.sebastianvm.bgcomp.navigation.PresentationModes

data class NavHostArguments(val initialDestination: NavDestination) : MvvmComponentArguments {
    constructor(
        args: MvvmComponentArguments
    ) : this(
        NavDestination(
            args,
            presentationModes =
                PresentationModes(
                    default = PresentationMode.Screen(transition = NavTransition.None)
                ),
        )
    )
}
