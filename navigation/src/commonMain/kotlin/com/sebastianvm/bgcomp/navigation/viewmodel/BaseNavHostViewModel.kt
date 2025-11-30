package com.sebastianvm.bgcomp.navigation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import app.cash.molecule.RecompositionMode
import com.sebastianvm.bgcomp.mvvm.BaseViewModel
import com.sebastianvm.bgcomp.mvvm.CloseableCoroutineScope
import com.sebastianvm.bgcomp.mvvm.MvvmComponentArguments
import com.sebastianvm.bgcomp.mvvm.MvvmComponentInitializer
import com.sebastianvm.bgcomp.mvvm.Props
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class BaseNavHostViewModel<ParentProps : Props, ChildrenProps : Props>(
    private val arguments: NavHostArguments,
    private val savedStateFlow: MutableStateFlow<NavHostSavedState>,
    viewModelScope: CloseableCoroutineScope,
    recompositionMode: RecompositionMode,
    private val mvvmComponentInitializer: MvvmComponentInitializer<ChildrenProps>,
    private val parentNavProps: NavigationProps? = null,
) :
    BaseViewModel<ParentProps, NavHostState, NavHostUserAction>(
        viewModelScope,
        recompositionMode,
        NavHostState(key = arguments.initialDestination.args, backStack = persistentListOf()),
    ) {

    protected abstract val childrenProps: StateFlow<ChildrenProps>

    private val navHostKey = arguments.initialDestination.args

    protected val navigationProps: NavigationProps by lazy {
        NavigationProps(
            push = { nextDestination, popCurrent ->
                handle(Push(destination = nextDestination, popCurrent = popCurrent))
            },
            pop = { handle(Pop) },
        )
    }

    @Composable
    override fun presenter(): NavHostState {
        val savedState by savedStateFlow.collectAsState()
        val backstackMap = remember {
            mutableStateMapOf<MvvmComponentArguments, NavHostState.ComponentWithPresentationModes>()
        }
        val backstack = remember {
            mutableStateOf<ImmutableList<NavHostState.ComponentWithPresentationModes>>(
                persistentListOf()
            )
        }

        LaunchedEffect(savedState, arguments) {
            if (savedState.backStack.isEmpty()) {
                handle(Push(arguments.initialDestination))
                return@LaunchedEffect
            }
            backstack.value =
                savedState.backStack
                    .map { destination ->
                        backstackMap.getOrPut(destination.args) {
                            NavHostState.ComponentWithPresentationModes(
                                component =
                                    mvvmComponentInitializer.initialize(
                                        args = destination.args,
                                        props = childrenProps,
                                    ),
                                presentationModes = destination.presentationModes,
                            )
                        }
                    }
                    .toImmutableList()
        }

        return NavHostState(key = navHostKey, backStack = backstack.value)
    }

    override fun handle(action: NavHostUserAction) {
        when (action) {
            Pop -> {
                if (state.value.backStack.size == 1) {
                    parentNavProps?.pop()
                    return
                }
                savedStateFlow.update {
                    NavHostSavedState(backStack = it.backStack.dropLast(1).toImmutableList()).also {
                        println("Popped state: $it")
                    }
                }
            }

            is Push -> {
                savedStateFlow.update {
                    val newBackstack =
                        if (action.popCurrent) {
                            it.backStack.dropLast(1)
                        } else {
                            it.backStack
                        } + action.destination
                    NavHostSavedState(backStack = newBackstack.toImmutableList())
                }
            }
        }
    }
}
