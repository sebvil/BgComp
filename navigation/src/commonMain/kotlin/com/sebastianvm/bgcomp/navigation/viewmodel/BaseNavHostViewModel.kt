package com.sebastianvm.bgcomp.navigation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.sebastianvm.bgcomp.mvvm.BaseViewModel
import com.sebastianvm.bgcomp.mvvm.MvvmComponentArguments
import com.sebastianvm.bgcomp.mvvm.MvvmComponentInitializer
import com.sebastianvm.bgcomp.mvvm.Props
import com.sebastianvm.bgcomp.mvvm.StateProducerScope
import com.sebastianvm.bgcomp.mvvm.UiEvents
import com.sebastianvm.bgcomp.mvvm.ViewModelState
import com.sebastianvm.bgcomp.mvvm.rememberSerializable
import com.sebastianvm.bgcomp.navigation.NavDestination
import jdk.internal.misc.Signal.handle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.StateFlow

abstract class BaseNavHostViewModel<ParentProps : Props, ChildrenProps : Props>(
    private val arguments: NavHostArguments,
    private val mvvmComponentInitializer: MvvmComponentInitializer<ChildrenProps>,
    private val parentNavProps: NavigationProps? = null,
) : BaseViewModel<ParentProps, NavHostState, NavHostUserAction>() {

    @Composable
    protected abstract fun childrenProps(navigationProps: NavigationProps): StateFlow<ChildrenProps>

    private val navHostKey = arguments.initialDestination.args

    @Composable
    override fun StateProducerScope<NavHostState, NavHostUserAction>.state():
        ViewModelState<NavHostState, NavHostUserAction> {
        val backstackMap = remember {
            mutableStateMapOf<MvvmComponentArguments, NavHostState.ComponentWithPresentationModes>()
        }

        val argsBackstack = rememberSerializable {
            mutableStateListOf(arguments.initialDestination)
        }
        val navigationProps =
            NavigationProps(
                push = { destination, popCurrent ->
                    handle(Push(destination = destination, popCurrent = popCurrent), argsBackstack)
                },
                pop = { handle(Pop, argsBackstack) },
            )
        val childrenProps = childrenProps(navigationProps)
        val backstack =
            argsBackstack
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
        val uiEvents = remember { UiEvents<NavHostUserAction>() }

        return ViewModelState(
            state = NavHostState(key = navHostKey, backStack = backstack),
            uiEvents = uiEvents,
            handle = { action -> handle(action = action, backstack = argsBackstack) },
        )
    }

    private fun handle(action: NavHostUserAction, backstack: SnapshotStateList<NavDestination>) {
        when (action) {
            Pop -> {
                if (backstack.size == 1) {
                    parentNavProps?.pop()
                    return
                }
                backstack.removeAt(backstack.lastIndex)
            }

            is Push -> {
                val destination = action.destination
                backstack.apply {
                    if (action.popCurrent) {
                        removeAt(lastIndex)
                    }

                    add(destination)
                }
            }
        }
    }
}
