package com.sebastianvm.bgcomp.features.kombio.summary.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sebastianvm.bgcomp.designsys.components.Button
import com.sebastianvm.bgcomp.designsys.components.Card
import com.sebastianvm.bgcomp.designsys.components.HorizontalDivider
import com.sebastianvm.bgcomp.designsys.components.ListItem
import com.sebastianvm.bgcomp.designsys.components.Scaffold
import com.sebastianvm.bgcomp.designsys.components.Text
import com.sebastianvm.bgcomp.designsys.theme.BgCompTheme
import com.sebastianvm.bgcomp.model.GameMode
import com.sebastianvm.bgcomp.features.kombio.summary.viewmodel.EnterPoints
import com.sebastianvm.bgcomp.features.kombio.summary.viewmodel.KombioGameSummaryState
import com.sebastianvm.bgcomp.features.kombio.summary.viewmodel.KombioGameSummaryUserAction
import com.sebastianvm.bgcomp.features.kombio.summary.viewmodel.StartNewGame
import com.sebastianvm.bgcomp.mvvm.Ui
import com.sebastianvm.bgcomp.resources.Res
import com.sebastianvm.bgcomp.resources.current_round
import com.sebastianvm.bgcomp.resources.enter_points
import com.sebastianvm.bgcomp.resources.game_over
import com.sebastianvm.bgcomp.resources.game_summary
import com.sebastianvm.bgcomp.resources.hand_points_value
import com.sebastianvm.bgcomp.resources.inactive
import com.sebastianvm.bgcomp.resources.new_game
import com.sebastianvm.bgcomp.resources.penalty
import com.sebastianvm.bgcomp.resources.player_scores
import com.sebastianvm.bgcomp.resources.points_mode_target
import com.sebastianvm.bgcomp.resources.round_details
import com.sebastianvm.bgcomp.resources.round_number
import com.sebastianvm.bgcomp.resources.round_total
import com.sebastianvm.bgcomp.resources.rounds_mode_total
import com.sebastianvm.bgcomp.resources.winner
import com.sebastianvm.bgcomp.ui.UiString
import org.jetbrains.compose.resources.stringResource

object KombioGameSummaryUi : Ui<KombioGameSummaryState, KombioGameSummaryUserAction> {
    @Composable
    override fun invoke(
        state: KombioGameSummaryState,
        handle: (KombioGameSummaryUserAction) -> Unit,
        modifier: Modifier
    ) {
        val game = state.game ?: return

        Scaffold(
            modifier = modifier.fillMaxSize().padding(vertical = 16.dp),
        ) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = UiString(Res.string.game_summary),
                    style = BgCompTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            when (val mode = game.gameMode) {
                                is GameMode.Points -> {
                                    Text(
                                        text = stringResource(Res.string.points_mode_target, mode.points),
                                        style = BgCompTheme.typography.titleMedium
                                    )
                                }
                                is GameMode.Rounds -> {
                                    Text(
                                        text = stringResource(Res.string.rounds_mode_total, mode.rounds),
                                        style = BgCompTheme.typography.titleMedium
                                    )
                                }
                                else -> Unit
                            }

                            if (game.isGameOver) {
                                Text(
                                    text = UiString(Res.string.game_over),
                                    style = BgCompTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                                game.winner?.let { winner ->
                                    Text(
                                        text = stringResource(Res.string.winner, winner.name),
                                        style = BgCompTheme.typography.titleLarge,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = stringResource(Res.string.current_round, game.currentRound),
                                    style = BgCompTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }

                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = UiString(Res.string.player_scores),
                                style = BgCompTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            game.players.sortedBy { it.totalScore }.forEach { player ->
                                ListItem(
                                    headlineContent = { Text(player.name) },
                                    trailingContent = {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = player.totalScore.toString(),
                                                style = BgCompTheme.typography.titleMedium
                                            )
                                            if (!player.isActive) {
                                                Text(
                                                    text = UiString(Res.string.inactive),
                                                    style = BgCompTheme.typography.bodySmall
                                                )
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }

                    items(game.rounds.reversed()) { round ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(Res.string.round_number, round.roundNumber),
                                style = BgCompTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = UiString(Res.string.round_details),
                                style = BgCompTheme.typography.labelLarge,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )

                            round.playerScores.forEach { score ->
                                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = score.playerName + if (score.calledKombio) " (K)" else "",
                                            style = BgCompTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = stringResource(Res.string.round_total, score.totalRoundScore),
                                            style = BgCompTheme.typography.bodyLarge
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = stringResource(Res.string.hand_points_value, score.handPoints),
                                            style = BgCompTheme.typography.bodySmall
                                        )
                                        if (score.penalty != 0) {
                                            Text(
                                                text = stringResource(Res.string.penalty, score.penalty),
                                                style = BgCompTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                    HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
                                }
                            }
                        }
                    }
                }

                if (!game.isGameOver) {
                    Button(
                        text = UiString(Res.string.enter_points),
                        onClick = { handle(EnterPoints) },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Button(
                        text = UiString(Res.string.new_game),
                        onClick = { handle(StartNewGame) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
