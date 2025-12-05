package com.sebastianvm.bgcomp.features.kombio.host

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.sebastianvm.bgcomp.featureinterfaces.KombioGameHostArguments
import com.sebastianvm.bgcomp.featureinterfaces.KombioGameSummaryArguments
import com.sebastianvm.bgcomp.features.kombio.model.KombioGame
import com.sebastianvm.bgcomp.features.kombio.model.PlayerState
import com.sebastianvm.bgcomp.mvvm.MvvmComponentInitializer
import com.sebastianvm.bgcomp.mvvm.codegen.MvvmComponent
import com.sebastianvm.bgcomp.mvvm.rememberSerializable
import com.sebastianvm.bgcomp.navigation.ui.NavHostUi
import com.sebastianvm.bgcomp.navigation.viewmodel.BaseNavHostViewModel
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostArguments
import com.sebastianvm.bgcomp.navigation.viewmodel.NavigationProps
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@MvvmComponent(argsClass = KombioGameHostArguments::class, uiClass = NavHostUi::class)
@AssistedInject
class KombioGameHostViewModel(
    @Assisted private val arguments: KombioGameHostArguments,
    @Assisted private val props: StateFlow<NavigationProps>,
    mvvmComponentInitializer: MvvmComponentInitializer<KombioGameHostProps>,
) :
    BaseNavHostViewModel<NavigationProps, KombioGameHostProps>(
        arguments = NavHostArguments(args = KombioGameSummaryArguments),
        mvvmComponentInitializer = mvvmComponentInitializer,
        parentNavProps = props.value,
    ) {

    @Composable
    override fun childrenProps(navigationProps: NavigationProps): StateFlow<KombioGameHostProps> {
        val savedGameState = rememberSerializable {
            mutableStateOf(
                KombioGame(
                    players =
                        arguments.playerNames.map { name -> PlayerState(name) }.toPersistentList(),
                    gameMode = arguments.gameMode,
                )
            )
        }

        val stateFlow = remember {
            MutableStateFlow(
                KombioGameHostProps(
                    game = savedGameState.value,
                    navigationProps = navigationProps,
                    updateGame = { newSavedGame -> savedGameState.value = newSavedGame },
                )
            )
        }
        LaunchedEffect(savedGameState.value) {
            stateFlow.update { it.copy(game = savedGameState.value) }
        }

        return stateFlow
    }
}
