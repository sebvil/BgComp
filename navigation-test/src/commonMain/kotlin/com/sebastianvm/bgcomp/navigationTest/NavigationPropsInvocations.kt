package com.sebastianvm.bgcomp.navigationTest

import com.sebastianvm.bgcomp.mvvm.BaseViewModel
import com.sebastianvm.bgcomp.mvvm.asCloseable
import com.sebastianvm.bgcomp.navigation.NavDestination
import com.sebastianvm.bgcomp.navigation.viewmodel.NavigationProps
import com.sebastianvm.bgcomp.testing.di.TestAppGraph
import de.infix.testBalloon.framework.core.TestExecutionScope
import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import dev.zacsweers.metro.createGraphFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn

data class PushInvocation(val destination: NavDestination, val popCurrent: Boolean)

class NavigationPropsInvocations {
    private val _pushInvocations = mutableListOf<PushInvocation>()
    private val _popInvocations = mutableListOf<Unit>()

    val pushInvocations: List<PushInvocation>
        get() = _pushInvocations

    val popInvocations: List<Unit>
        get() = _popInvocations

    fun createNavigationProps(): StateFlow<NavigationProps> =
        MutableStateFlow(
            NavigationProps(
                push = { destination, popCurrent ->
                    _pushInvocations.add(PushInvocation(destination, popCurrent))
                },
                pop = { _popInvocations.add(Unit) },
            )
        )
}

context(navPropsInvocations: NavigationPropsInvocations)
val pushInvocations: List<PushInvocation>
    get() = navPropsInvocations.pushInvocations

context(navPropsInvocations: NavigationPropsInvocations)
val popInvocations: List<Unit>
    get() = navPropsInvocations.popInvocations

@OptIn(ExperimentalCoroutinesApi::class)
@TestRegistering
inline fun <VM : BaseViewModel<*, *, *>> TestSuite.viewModelTest(
    name: String,
    crossinline configure:
        suspend context(TestExecutionScope, TestAppGraph)
        () -> Unit =
        {},
    crossinline createGraphFactory: () -> TestAppGraph.Factory<*>,
    crossinline vmFactory: (graph: TestAppGraph, NavigationPropsInvocations) -> VM,
    crossinline action:
        suspend context(TestExecutionScope, TestAppGraph, NavigationPropsInvocations)
        VM.() -> Unit,
) =
    test(name) {
        val graph = createGraphFactory().create(this.testScope.backgroundScope.asCloseable())
        val navigationInvocations = NavigationPropsInvocations()
        context(this, graph, navigationInvocations) {
            configure()
            val viewModel = vmFactory(graph, navigationInvocations)
            viewModel.state.launchIn(graph.testScope)
            viewModel.action()
        }
    }
