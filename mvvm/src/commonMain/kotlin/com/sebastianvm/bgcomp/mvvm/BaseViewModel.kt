package com.sebastianvm.bgcomp.mvvm

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<P : Props, S : UiState, A : UserAction>() : ViewModel() {

    @Composable abstract fun state(): ViewModelState<S, A>

    @Composable protected fun rememberUiEvents() = rememberUiEvents<A>()
}
