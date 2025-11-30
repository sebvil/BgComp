package com.sebastianvm.bgcomp.navigation.viewmodel

import app.cash.molecule.RecompositionMode
import com.sebastianvm.bgcomp.mvvm.CloseableCoroutineScope
import com.sebastianvm.bgcomp.mvvm.MvvmComponentArguments
import com.sebastianvm.bgcomp.mvvm.MvvmComponentInitializer
import com.sebastianvm.bgcomp.navigation.NavDestination
import com.sebastianvm.bgcomp.navigation.di.FeatureTestAppGraph
import com.sebastianvm.bgcomp.navigationTest.NavigationPropsInvocations
import com.sebastianvm.bgcomp.navigationTest.popInvocations
import com.sebastianvm.bgcomp.navigationTest.viewModelTest
import com.sebastianvm.bgcomp.testing.di.TestAppGraph
import com.sebastianvm.bgcomp.testing.runCurrent
import de.infix.testBalloon.framework.core.TestExecutionScope
import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import dev.zacsweers.metro.createGraphFactory
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TestNavHostViewModel(
    arguments: NavHostArguments,
    savedStateFlow: MutableStateFlow<NavHostSavedState>,
    viewModelScope: CloseableCoroutineScope,
    recompositionMode: RecompositionMode,
    mvvmComponentInitializer: MvvmComponentInitializer<NavigationProps>,
    parentNavProps: NavigationProps? = null,
) :
    BaseNavHostViewModel<NavigationProps, NavigationProps>(
        arguments,
        savedStateFlow,
        viewModelScope,
        recompositionMode,
        mvvmComponentInitializer,
        parentNavProps,
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

@OptIn(ExperimentalCoroutinesApi::class)
val BaseNavHostViewModelTest by testSuite {
    baseVmTest("Push adds destination to backstack") { savedState ->
        runCurrent()

        // Initial destination should be added automatically
        savedState.value.backStack shouldHaveSize 1
        // args are provided via baseVmTest default. We don't assert equality to a fresh instance.

        // Push new destination
        val newDestination = NavDestination(TestArguments1)
        handle(Push(newDestination))
        runCurrent()

        savedState.value.backStack shouldHaveSize 2
        savedState.value.backStack[1] shouldBe newDestination
    }

    baseVmTest("Push with popCurrent replaces current destination") { savedState ->
        runCurrent()

        // Initial destination
        savedState.value.backStack shouldHaveSize 1

        // Push with popCurrent
        val newDestination = NavDestination(TestArguments1)
        handle(Push(newDestination, popCurrent = true))
        runCurrent()

        savedState.value.backStack shouldHaveSize 1
        savedState.value.backStack[0] shouldBe newDestination
    }

    val initialDest = NavDestination(TestArguments1)
    val secondDest = NavDestination(TestArguments1)
    baseVmTest(
        "Pop removes last destination from backstack",
        initialSavedState =
            NavHostSavedState(backStack = persistentListOf(initialDest, secondDest)),
        args = initialDest.args,
    ) { savedState ->
        runCurrent()

        handle(Pop)
        runCurrent()

        savedState.value.backStack shouldHaveSize 1
        savedState.value.backStack[0] shouldBe initialDest
    }

    baseVmTest(
        "Pop with single destination and parent delegates to parent",
        hasParentProps = true,
    ) { savedState ->
        runCurrent()

        handle(Pop)
        runCurrent()

        // Backstack should remain unchanged
        savedState.value.backStack shouldHaveSize 1
        // Parent pop should be called
        popInvocations shouldHaveSize 1
    }

    baseVmTest(
        "Pop with single destination and no parent keeps backstack",
        hasParentProps = false,
    ) { savedState ->
        runCurrent()

        handle(Pop)
        runCurrent()

        savedState.value.backStack shouldHaveSize 1
    }

    baseVmTest("Push multiple destinations builds correct backstack") { savedState ->
        runCurrent()

        // Push several destinations
        val dest1 = NavDestination(TestArguments1)
        val dest2 = NavDestination(TestArguments1)
        val dest3 = NavDestination(TestArguments1)

        handle(Push(dest1))
        runCurrent()
        handle(Push(dest2))
        runCurrent()
        handle(Push(dest3))
        runCurrent()

        savedState.value.backStack shouldHaveSize 4 // initial + 3 pushes
        savedState.value.backStack[1] shouldBe dest1
        savedState.value.backStack[2] shouldBe dest2
        savedState.value.backStack[3] shouldBe dest3
    }

    val d1 = NavDestination(TestArguments1)
    val d2 = NavDestination(TestArguments1)
    val d3 = NavDestination(TestArguments1)
    baseVmTest(
        "Pop removes destinations in correct order",
        initialSavedState = NavHostSavedState(backStack = persistentListOf(d1, d2, d3)),
        args = d1.args,
    ) { savedState ->
        runCurrent()

        handle(Pop)
        runCurrent()
        savedState.value.backStack shouldHaveSize 2
        savedState.value.backStack.last() shouldBe d2

        handle(Pop)
        runCurrent()
        savedState.value.backStack shouldHaveSize 1
        savedState.value.backStack.last() shouldBe d1
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@TestRegistering
private fun TestSuite.baseVmTest(
    name: String,
    initialSavedState: NavHostSavedState = NavHostSavedState(backStack = persistentListOf()),
    args: MvvmComponentArguments = TestArguments1,
    hasParentProps: Boolean = true,
    action:
        suspend context(TestExecutionScope, TestAppGraph, NavigationPropsInvocations)
        TestNavHostViewModel.(savedState: MutableStateFlow<NavHostSavedState>) -> Unit,
) {
    val savedState = MutableStateFlow(initialSavedState)
    viewModelTest(
        name = name,
        configure = {},
        createGraphFactory = { createGraphFactory<FeatureTestAppGraph.Factory>() },
        vmFactory = { graph, parentInvocations ->
            val parentProps =
                if (hasParentProps) parentInvocations.createNavigationProps().value else null
            TestNavHostViewModel(
                arguments = NavHostArguments(args),
                savedStateFlow = savedState,
                viewModelScope = graph.testScope,
                recompositionMode = graph.recompositionMode,
                mvvmComponentInitializer = graph.mvvmComponentInitializer(),
                parentNavProps = parentProps,
            )
        },
        action = { action(savedState) },
    )
}
