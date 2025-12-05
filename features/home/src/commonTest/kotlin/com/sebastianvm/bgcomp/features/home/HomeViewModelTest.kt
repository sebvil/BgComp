package com.sebastianvm.bgcomp.features.home

import com.sebastianvm.bgcomp.featureinterfaces.NewKombioGameArguments
import com.sebastianvm.bgcomp.features.di.FeatureTestAppGraph
import com.sebastianvm.bgcomp.features.home.viewmodel.GameClicked
import com.sebastianvm.bgcomp.features.home.viewmodel.HomeViewModel
import com.sebastianvm.bgcomp.model.Game
import com.sebastianvm.bgcomp.navigationTest.NavigationPropsInvocations
import com.sebastianvm.bgcomp.navigationTest.pushInvocations
import com.sebastianvm.bgcomp.navigationTest.viewModelTest
import com.sebastianvm.bgcomp.testing.di.TestAppGraph
import de.infix.testBalloon.framework.core.TestExecutionScope
import de.infix.testBalloon.framework.core.TestSuite
import de.infix.testBalloon.framework.core.testSuite
import de.infix.testBalloon.framework.shared.TestRegistering
import dev.zacsweers.metro.createGraphFactory
import io.kotest.matchers.collections.shouldHaveSize
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
val HomeViewModelTest by testSuite {
    vmTest("Kombio GameClicked navigates to new kombio game screen", setUp = {}) {
        handle(GameClicked(Game.Kombio))
        pushInvocations shouldHaveSize 1
        pushInvocations[0].destination.args == NewKombioGameArguments
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@TestRegistering
inline fun TestSuite.vmTest(
    name: String,
    crossinline setUp:
        suspend context(TestExecutionScope, TestAppGraph)
        () -> Unit,
    crossinline action:
        suspend context(TestExecutionScope, TestAppGraph, NavigationPropsInvocations)
        HomeViewModel.() -> Unit,
) =
    viewModelTest(
        name = name,
        configure = setUp,
        createGraphFactory = { createGraphFactory<FeatureTestAppGraph.Factory>() },
        vmFactory = { graph, navigationInvocations ->
            HomeViewModel(
                props = navigationInvocations.createNavigationProps(),
                viewModelScope = graph.testScope,
                recompositionMode = graph.recompositionMode,
            )
        },
        action = action,
    )
