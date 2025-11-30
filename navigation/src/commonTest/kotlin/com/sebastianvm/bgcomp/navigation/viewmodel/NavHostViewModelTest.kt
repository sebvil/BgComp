package com.sebastianvm.bgcomp.navigation.viewmodel

import com.sebastianvm.bgcomp.navigation.NavDestination
import com.sebastianvm.bgcomp.navigation.di.FeatureTestAppGraph
import com.sebastianvm.bgcomp.navigationTest.viewModelTest
import com.sebastianvm.bgcomp.testing.di.FakeMvvmComponent
import com.sebastianvm.bgcomp.testing.di.TestAppGraph
import com.sebastianvm.bgcomp.testing.runCurrent
import de.infix.testBalloon.framework.core.TestExecutionScope
import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import dev.zacsweers.metro.createGraphFactory
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.collections.set
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalCoroutinesApi::class)
val NavHostViewModelTest by testSuite {
    vmTest("childrenProps provides navigation functions") {
        runCurrent()

        val newDestination = NavDestination(TestArguments2)

        @Suppress("UNCHECKED_CAST")
        val firstScreen = state.value.backStack[0].component as FakeMvvmComponent<NavigationProps>
        firstScreen.props.value.push(newDestination, false)
        runCurrent()

        state.value.backStack shouldHaveSize 2
        state.value.backStack[1].component.arguments shouldBe TestArguments2
    }

    val dest1 = NavDestination(TestArguments1)
    val dest2 = NavDestination(TestArguments2)
    vmTest(
        "childrenProps pop works correctly",
        initialSavedState = NavHostSavedState(backStack = persistentListOf(dest1, dest2)),
    ) {
        runCurrent()

        @Suppress("UNCHECKED_CAST")
        val firstScreen = state.value.backStack[0].component as FakeMvvmComponent<NavigationProps>
        firstScreen.props.value.pop()
        runCurrent()

        state.value.backStack shouldHaveSize 1
        state.value.backStack[0].component.arguments shouldBe TestArguments1
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@TestRegistering
private fun TestSuite.vmTest(
    name: String,
    arguments: NavHostArguments = NavHostArguments(args = TestArguments1),
    initialSavedState: NavHostSavedState = NavHostSavedState(backStack = persistentListOf()),
    configure:
        suspend context(TestExecutionScope, TestAppGraph)
        () -> Unit =
        {},
    action:
        suspend context(TestExecutionScope, TestAppGraph)
        NavHostViewModel.() -> Unit,
) {
    viewModelTest(
        name,
        configure = configure,
        createGraphFactory = { createGraphFactory<FeatureTestAppGraph.Factory>() },
        vmFactory = { graph, _ ->
            NavHostViewModel(
                arguments = arguments,
                savedStateFlow = MutableStateFlow(initialSavedState),
                viewModelScope = graph.testScope,
                recompositionMode = graph.recompositionMode,
                mvvmComponentInitializer = graph.mvvmComponentInitializer(),
            )
        },
        action = { action() },
    )
}
