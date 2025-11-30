package com.sebastianvm.bgcomp.mvvm

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.molecule.RecompositionMode
import app.cash.molecule.moleculeFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

abstract class BaseViewModel<P : Props, S : UiState, A : UserAction>(
    viewModelScope: CloseableCoroutineScope,
    recompositionMode: RecompositionMode,
    initialState: S,
) : ViewModel(viewModelScope = viewModelScope) {

    @Composable protected abstract fun presenter(): S

    abstract fun handle(action: A)

    val state: StateFlow<S> =
        moleculeFlow(recompositionMode) { presenter() }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(DEFAULT_WHILE_SUBSCRIBED_TIMEOUT),
                initialState,
            )

    val uiEvents: UiEvents<A> = UiEvents()

    protected fun <T, U> StateFlow<T>.mapAsStateFlow(transform: (T) -> U): StateFlow<U> =
        map(transform)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(DEFAULT_WHILE_SUBSCRIBED_TIMEOUT),
                initialValue = transform(value),
            )

    companion object {
        private const val DEFAULT_WHILE_SUBSCRIBED_TIMEOUT = 5_000L
    }
}
