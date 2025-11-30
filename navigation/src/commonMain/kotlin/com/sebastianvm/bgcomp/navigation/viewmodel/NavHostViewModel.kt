package com.sebastianvm.bgcomp.navigation.viewmodel

import app.cash.molecule.RecompositionMode
import com.sebastianvm.bgcomp.mvvm.CloseableCoroutineScope
import com.sebastianvm.bgcomp.mvvm.MvvmComponentInitializer
import com.sebastianvm.bgcomp.navigation.ui.NavHostUi
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@com.sebastianvm.bgcomp.mvvm.codegen.MvvmComponent(
    argsClass = NavHostArguments::class,
    uiClass = NavHostUi::class,
    savedStateClasses = [NavHostSavedState::class],
)
@AssistedInject
class NavHostViewModel(
    @Assisted private val arguments: NavHostArguments,
    @Assisted private val savedStateFlow: MutableStateFlow<NavHostSavedState>,
    viewModelScope: CloseableCoroutineScope,
    recompositionMode: RecompositionMode,
    mvvmComponentInitializer: MvvmComponentInitializer<NavigationProps>,
) :
    BaseNavHostViewModel<Nothing, NavigationProps>(
        arguments,
        savedStateFlow,
        viewModelScope,
        recompositionMode,
        mvvmComponentInitializer,
    ) {

    override val childrenProps: StateFlow<NavigationProps> by lazy {
        MutableStateFlow(
            NavigationProps(
                push = { nextDestination, popCurrent ->
                    handle(Push(destination = nextDestination, popCurrent = popCurrent))
                },
                pop = { handle(Pop) },
            )
        )
    }
}
