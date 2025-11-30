package com.sebastianvm.bgcomp.navigation.viewmodel

import com.sebastianvm.bgcomp.mvvm.GenericMvvmComponent
import com.sebastianvm.bgcomp.mvvm.MvvmComponentArguments
import com.sebastianvm.bgcomp.mvvm.UiState
import com.sebastianvm.bgcomp.navigation.PresentationModes
import kotlinx.collections.immutable.ImmutableList

data class NavHostState(
    val key: MvvmComponentArguments,
    val backStack: ImmutableList<ComponentWithPresentationModes>,
) : UiState {

    data class ComponentWithPresentationModes(
        val component: GenericMvvmComponent,
        val presentationModes: PresentationModes,
    )
}
