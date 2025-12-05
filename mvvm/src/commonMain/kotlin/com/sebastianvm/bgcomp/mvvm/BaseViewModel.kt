package com.sebastianvm.bgcomp.mvvm

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<P : Props, S : UiState, A : UserAction>() : ViewModel() {

    @Composable protected abstract fun StateProducerScope<S, A>.state(): ViewModelState<S, A>

    @Composable fun state(): ViewModelState<S, A> = StateProducerScope<S, A>().state()
}

class StateProducerScope<S : UiState, A : UserAction> {

    val uiEvents = UiEvents<A>()

    fun createState(state: S, handle: (A) -> Unit) = ViewModelState(state, handle, uiEvents)
}
