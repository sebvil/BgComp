package com.sebastianvm.bgcomp.navigation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.sebastianvm.bgcomp.mvvm.Ui
import com.sebastianvm.bgcomp.navigation.BottomSheetScene
import com.sebastianvm.bgcomp.navigation.NavMetadata
import com.sebastianvm.bgcomp.navigation.PopUpScene
import com.sebastianvm.bgcomp.navigation.PresentationMode
import com.sebastianvm.bgcomp.navigation.metadata
import com.sebastianvm.bgcomp.navigation.presentationMode
import com.sebastianvm.bgcomp.navigation.toContentTransform
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostState
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostUserAction
import com.sebastianvm.bgcomp.navigation.viewmodel.Pop
import kotlinx.collections.immutable.toImmutableList

object NavHostUi : Ui<NavHostState, NavHostUserAction> {
    @Composable
    override fun invoke(
        state: NavHostState,
        handle: (NavHostUserAction) -> Unit,
        modifier: Modifier,
    ) {
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        var backstack by remember(state.backStack) { mutableStateOf(state.backStack) }

        if (backstack.isEmpty()) {
            // We need this to make sure animations work when navigating to a new NavHost
            Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {}
            return
        }
        val onBack = {
            backstack = backstack.dropLast(1).toImmutableList()
            handle(Pop)
        }
        NavDisplay(
            backStack = backstack,
            entryDecorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(removeViewModelStoreOnPop = { true }),
                ),
            sceneStrategy = BottomSheetScene.Strategy() then PopUpScene.Strategy(),
            onBack = onBack,
            entryProvider = { destination ->
                NavEntry(
                    key = destination,
                    contentKey = destination.component.arguments.toString(),
                    metadata =
                        (destination.presentationModes.presentationMode(windowSizeClass)
                                as? PresentationMode.Screen)
                            ?.transition
                            ?.let {
                                NavDisplay.transitionSpec {
                                    it.toContentTransform(isForward = true)
                                } +
                                    NavDisplay.popTransitionSpec {
                                        it.toContentTransform(isForward = false)
                                    } +
                                    NavDisplay.predictivePopTransitionSpec { _ ->
                                        it.toContentTransform(isForward = false)
                                    }
                            }
                            .orEmpty() +
                            mapOf(
                                "metadata" to
                                    NavMetadata(
                                        presentationMode =
                                            destination.presentationModes.presentationMode(
                                                windowSizeClass
                                            )
                                    )
                            ),
                ) {
                    val mvvmComponent = destination.component
                    mvvmComponent.Content(modifier = modifier)
                }
            },
        )
    }
}
