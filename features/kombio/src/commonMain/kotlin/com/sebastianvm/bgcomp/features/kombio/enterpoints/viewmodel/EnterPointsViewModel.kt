package com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel

import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.sebastianvm.bgcomp.features.kombio.host.KombioGameHostProps
import com.sebastianvm.bgcomp.features.kombio.model.Round
import com.sebastianvm.bgcomp.features.kombio.model.calculateRoundScores
import com.sebastianvm.bgcomp.model.GameMode
import com.sebastianvm.bgcomp.mvvm.BaseViewModel
import com.sebastianvm.bgcomp.mvvm.ViewModelState
import com.sebastianvm.bgcomp.navigation.viewmodel.HasNavigationProps
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.StateFlow

@AssistedInject
class EnterPointsViewModel(@Assisted private val props: StateFlow<KombioGameHostProps>) :
    BaseViewModel<KombioGameHostProps, EnterPointsState, EnterPointsUserAction>(),
    HasNavigationProps by HasNavigationProps.Default(props.value.navigationProps) {

    @Composable
    override fun state(): ViewModelState<EnterPointsState, EnterPointsUserAction> {
        val propsValue by props.collectAsState()
        val playerNames =
            propsValue.game.players
                .filter { player -> player.isActive }
                .map { it.name }
                .toPersistentList()
        val scores = propsValue.game.players.map { rememberTextFieldState("") }.toPersistentList()
        val uiEvents = rememberUiEvents()
        return ViewModelState(
            state =
                EnterPointsState(
                    scores = scores,
                    playerNames = playerNames,
                    roundNumber = propsValue.game.currentRound,
                ),
            uiEvents = uiEvents,
            handle = { action ->
                when (action) {
                    is SubmitRound -> {
                        val game = props.value.game
                        val handPoints = scores.map { it.text.toString().toInt() }
                        if (handPoints.size != playerNames.size) return@ViewModelState

                        val roundScores = calculateRoundScores(handPoints, action.kombioCaller)

                        val activePlayers = game.players.filter { it.isActive }
                        val updatedPlayers =
                            game.players
                                .map { player ->
                                    val activeIndex =
                                        activePlayers.indexOfFirst { it.name == player.name }
                                    if (activeIndex >= 0 && activeIndex < roundScores.size) {
                                        val roundScore = roundScores[activeIndex]
                                        player.copy(
                                            totalScore =
                                                player.totalScore + roundScore.totalRoundScore
                                        )
                                    } else {
                                        player
                                    }
                                }
                                .toPersistentList()

                        val round =
                            Round(
                                roundNumber = game.currentRound,
                                playerScores =
                                    roundScores
                                        .mapIndexed { index, score ->
                                            score.copy(playerName = activePlayers[index].name)
                                        }
                                        .toPersistentList(),
                            )

                        var playersAfterReentries = updatedPlayers

                        if (game.gameMode is GameMode.Points && !game.gameMode.winOnFirstOverflow) {
                            val maxValidScore =
                                updatedPlayers
                                    .filter { it.totalScore < game.gameMode.points }
                                    .maxOfOrNull { it.totalScore }
                            val remainingActivePlayers =
                                updatedPlayers.count {
                                    it.isActive && it.totalScore < game.gameMode.points
                                }
                            if (maxValidScore != null && remainingActivePlayers > 1) {
                                playersAfterReentries =
                                    updatedPlayers
                                        .map { player ->
                                            val reentriesUsed =
                                                player.reentriesUsed +
                                                    if (
                                                        player.totalScore >= game.gameMode.points &&
                                                            player.isActive
                                                    )
                                                        1
                                                    else 0
                                            val isActive =
                                                player.isActive &&
                                                    reentriesUsed <= game.gameMode.allowedReentries
                                            val totalScore =
                                                when {
                                                    !isActive -> player.totalScore
                                                    player.totalScore >= game.gameMode.points ->
                                                        maxValidScore

                                                    else -> player.totalScore
                                                }
                                            player.copy(
                                                isActive = isActive,
                                                reentriesUsed = reentriesUsed,
                                                totalScore = totalScore,
                                            )
                                        }
                                        .toPersistentList()
                            }
                        }

                        val updatedGame =
                            game.copy(
                                players = playersAfterReentries,
                                rounds = game.rounds.toPersistentList().mutate { it.add(round) },
                                currentRound = game.currentRound + 1,
                            )
                        println(updatedGame)

                        props.value.updateGame(updatedGame)

                        navigateUp()
                    }
                }
            },
        )
    }
}
