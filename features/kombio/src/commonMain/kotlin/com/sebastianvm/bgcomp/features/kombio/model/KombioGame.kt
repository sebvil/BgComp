package com.sebastianvm.bgcomp.features.kombio.model

import androidx.compose.runtime.Immutable
import com.sebastianvm.bgcomp.common.serialization.SerializableList
import com.sebastianvm.bgcomp.model.GameMode
import com.sebastianvm.bgcomp.mvvm.SavedState
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class KombioGame(
    val players: List<PlayerState> = emptyList(),
    val gameMode: GameMode? = null,
    val rounds: List<Round> = emptyList(),
    val currentRound: Int = 1,
    val isRestored: Boolean = false,
) : SavedState {
    val isGameOver: Boolean
        get() =
            when (gameMode) {
                is GameMode.Points -> {
                    val activePlayers = players.filter { it.isActive }
                    when {
                        activePlayers.size <= 1 -> true
                        gameMode.winOnFirstOverflow ->
                            activePlayers.any { it.totalScore >= gameMode.points }

                        else -> activePlayers.count { it.totalScore < gameMode.points } <= 1
                    }
                }

                is GameMode.Rounds -> currentRound > gameMode.rounds
                null -> false
            }

    val winner: PlayerState?
        get() =
            when {
                !isGameOver -> null
                gameMode is GameMode.Points && gameMode.winOnFirstOverflow -> {
                    players.filter { it.isActive }.firstOrNull { it.totalScore >= gameMode.points }
                }

                else -> players.filter { it.isActive }.minByOrNull { it.totalScore }
            }
}

@Serializable
data class PlayerState(
    val name: String,
    val totalScore: Int = 0,
    val isActive: Boolean = true,
    val reentriesUsed: Int = 0,
)

@Serializable
data class Round(val roundNumber: Int, val playerScores: SerializableList<PlayerRoundScore>)

@Serializable
data class PlayerRoundScore(
    val playerName: String,
    val handPoints: Int,
    val calledKombio: Boolean,
    val penalty: Int,
    val totalRoundScore: Int,
)

fun calculateRoundScores(handPoints: List<Int>, kombioCallerIndex: Int): List<PlayerRoundScore> {
    // TODO
    //  require(handPoints.all { it >= -3 }) { "Hand points cannot be less than -3" }

    val playerNames = handPoints.indices.map { "Player ${it + 1}" }
    val minHandPoints = handPoints.minOrNull() ?: 0
    val playersWithMinPoints = handPoints.count { it == minHandPoints }
    val didKombioCallerWin =
        playersWithMinPoints == 1 && handPoints[kombioCallerIndex] == minHandPoints

    val penalties =
        handPoints.mapIndexed { index, _ ->
            when {
                kombioCallerIndex == index -> {
                    if (didKombioCallerWin) 0 else 15
                }

                didKombioCallerWin -> 10
                else -> 0
            }
        }

    return handPoints.indices
        .map { index ->
            val handScore = handPoints[index]
            val penalty = penalties[index]
            PlayerRoundScore(
                playerName = playerNames[index],
                handPoints = handScore,
                calledKombio = kombioCallerIndex == index,
                penalty = penalty,
                totalRoundScore = handScore + penalty,
            )
        }
        .toPersistentList()
}
