package com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import app.cash.molecule.RecompositionMode
import com.sebastianvm.bgcomp.featureinterfaces.EnterPointsArguments
import com.sebastianvm.bgcomp.features.kombio.enterpoints.ui.EnterPointsUi
import com.sebastianvm.bgcomp.features.kombio.host.KombioGameHostProps
import com.sebastianvm.bgcomp.features.kombio.model.KombioGameState
import com.sebastianvm.bgcomp.features.kombio.model.Round
import com.sebastianvm.bgcomp.features.kombio.model.calculateRoundScores
import com.sebastianvm.bgcomp.model.GameMode
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
    argsClass = EnterPointsArguments::class,
    uiClass = EnterPointsUi::class,
    savedStateClasses = [EnterPointsState::class]
)
@AssistedInject
class EnterPointsViewModel(
    @Assisted private val props: StateFlow<KombioGameHostProps>,
    @Assisted private val savedEnterPointsState: MutableStateFlow<EnterPointsState>,
    viewModelScope: CloseableCoroutineScope,
    recompositionMode: RecompositionMode,
) :
    BaseViewModel<KombioGameHostProps, EnterPointsState, EnterPointsUserAction>(
        viewModelScope,
        recompositionMode,
        savedEnterPointsState.value,
    ),
    HasNavigationProps by HasNavigationProps.Default(props.value.navigationProps) {

    init {
        if (!savedEnterPointsState.value.isRestored) {
            savedEnterPointsState.update {
                val game = props.value.game
                it.copy(
                    playerNames = game.players.filter { player -> player.isActive }.map { it.name }.toPersistentList(),
                    handPoints = game.players.filter { player -> player.isActive }.map { "" }.toPersistentList(),
                    kombioCallerIndex = 0,
                    roundNumber = game.currentRound,
                    isRestored = true
                )
            }
        }
    }

    @Composable
    override fun presenter(): EnterPointsState {
        val state = savedEnterPointsState.collectAsState()
        return state.value
    }

    override fun handle(action: EnterPointsUserAction) {
        when (action) {
            is HandPointsChanged -> {
                savedEnterPointsState.update {
                    it.copy(
                        handPoints = it.handPoints.toPersistentList().mutate { list ->
                            list[action.playerIndex] = action.points
                        }
                    )
                }
            }

            is KombioCallerSelected -> {
                savedEnterPointsState.update {
                    it.copy(kombioCallerIndex = action.playerIndex)
                }
            }

            SubmitRound -> {
                val state = savedEnterPointsState.value
                val game = props.value.game

                val handPoints = state.handPoints.mapNotNull { it.toIntOrNull() }
                if (handPoints.size != state.playerNames.size) return

                val roundScores = calculateRoundScores(handPoints, state.kombioCallerIndex)

                val activePlayers = game.players.filter { it.isActive }
                val updatedPlayers = game.players.map { player ->
                    val activeIndex = activePlayers.indexOfFirst { it.name == player.name }
                    if (activeIndex >= 0 && activeIndex < roundScores.size) {
                        val roundScore = roundScores[activeIndex]
                        player.copy(totalScore = player.totalScore + roundScore.totalRoundScore)
                    } else {
                        player
                    }
                }.toPersistentList()

                val round = Round(
                    roundNumber = game.currentRound,
                    playerScores = roundScores.mapIndexed { index, score ->
                        score.copy(playerName = activePlayers[index].name)
                    }.toPersistentList()
                )

                var playersAfterReentries = updatedPlayers

                if (game.gameMode is GameMode.Points && !game.gameMode.winOnFirstOverflow) {
                    val maxValidScore =
                        updatedPlayers.filter { it.totalScore < game.gameMode.points }.maxOfOrNull { it.totalScore }
                    val remainingActivePlayers =
                        updatedPlayers.count { it.isActive && it.totalScore < game.gameMode.points }
                    if (maxValidScore != null && remainingActivePlayers > 1) {
                        playersAfterReentries = updatedPlayers.map { player ->
                            val reentriesUsed =
                                player.reentriesUsed + if (player.totalScore >= game.gameMode.points && player.isActive) 1 else 0
                            val isActive = player.isActive && reentriesUsed <= game.gameMode.allowedReentries
                            val totalScore = when {
                                !isActive -> player.totalScore
                                player.totalScore >= game.gameMode.points -> maxValidScore
                                else -> player.totalScore
                            }
                            player.copy(isActive = isActive, reentriesUsed = reentriesUsed, totalScore = totalScore)
                        }.toPersistentList()
                    }
                }

                val updatedGame = game.copy(
                    players = playersAfterReentries,
                    rounds = game.rounds.toPersistentList().mutate { it.add(round) },
                    currentRound = game.currentRound + 1
                )

                props.value.updateGame(updatedGame)

                navigateUp()
            }
        }
    }
}
