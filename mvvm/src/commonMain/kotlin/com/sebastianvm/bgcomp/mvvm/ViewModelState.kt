package com.sebastianvm.bgcomp.mvvm

data class ViewModelState<S : UiState, A : UserAction>(
    val state: S,
    val handle: (A) -> Unit,
    val uiEvents: UiEvents<A>,
)
