package com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import app.cash.molecule.RecompositionMode
import com.sebastianvm.bgcomp.featureinterfaces.KombioGameHostArguments
import com.sebastianvm.bgcomp.featureinterfaces.KombioGameSummaryArguments
import com.sebastianvm.bgcomp.featureinterfaces.NewKombioGameArguments
import com.sebastianvm.bgcomp.features.kombio.model.KombioGame
import com.sebastianvm.bgcomp.features.kombio.model.KombioGameState
import com.sebastianvm.bgcomp.features.kombio.model.PlayerState
import com.sebastianvm.bgcomp.features.kombio.newgame.ui.NewKombioGameUi
import com.sebastianvm.bgcomp.model.GameMode
import com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel.GameModeType
import com.sebastianvm.bgcomp.mvvm.BaseViewModel
import com.sebastianvm.bgcomp.mvvm.CloseableCoroutineScope
import com.sebastianvm.bgcomp.mvvm.codegen.MvvmComponent
import com.sebastianvm.bgcomp.navigation.viewmodel.HasNavigationProps
import com.sebastianvm.bgcomp.navigation.viewmodel.NavigationProps
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@MvvmComponent(
    argsClass = NewKombioGameArguments::class,
    uiClass = NewKombioGameUi::class,
    savedStateClasses = [NewKombioGameState::class]
)
@AssistedInject
class NewKombioGameViewModel(
    @Assisted private val props: StateFlow<NavigationProps>,
    @Assisted private val savedNewKombioGameState: MutableStateFlow<NewKombioGameState>,
    viewModelScope: CloseableCoroutineScope,
    recompositionMode: RecompositionMode,
) :
    BaseViewModel<NavigationProps, NewKombioGameState, NewKombioGameUserAction>(
        viewModelScope,
        recompositionMode,
        savedNewKombioGameState.value,
    ),
    HasNavigationProps by HasNavigationProps.Default(props.value) {

    @Composable
    override fun presenter(): NewKombioGameState {
        val state = savedNewKombioGameState.collectAsState()
        return state.value
    }


    override fun handle(action: NewKombioGameUserAction) {
        when (action) {
            is GameModeDataUpdated -> {
                savedNewKombioGameState.update {
                    it.copy(gameMode = action.newGameModeData)
                }
            }

            is GameModeSelected -> {
                savedNewKombioGameState.update {
                    it.copy(
                        gameMode = when (action.mode) {
                            GameModeType.Points -> GameMode.Points()
                            GameModeType.Rounds -> GameMode.Rounds()
                        }
                    )
                }
            }

            PlayerAdded -> {
                savedNewKombioGameState.update {
                    it.copy(
                        playerNames = it.playerNames.toPersistentList().mutate { players -> players.add("") }
                    )
                }
            }

            is PlayerNameChanged -> {
                savedNewKombioGameState.update {
                    it.copy(
                        playerNames = it.playerNames.toPersistentList()
                            .mutate { players -> players[action.index] = action.newName }
                    )
                }
            }

            PlayerRemoved -> {
                savedNewKombioGameState.update {
                    it.copy(
                        playerNames = it.playerNames.toPersistentList()
                            .mutate { players -> players.removeAt(players.lastIndex) })
                }
            }

            StartGame -> {
                val state = savedNewKombioGameState.value
                navigateTo(KombioGameHostArguments(gameMode = state.gameMode, playerNames = state.playerNames))
            }
        }
    }
}
