package com.sebastianvm.bgcomp.features.kombio.newgame.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sebastianvm.bgcomp.designsys.components.Button
import com.sebastianvm.bgcomp.designsys.components.Card
import com.sebastianvm.bgcomp.designsys.components.IconButton
import com.sebastianvm.bgcomp.designsys.components.ListItem
import com.sebastianvm.bgcomp.designsys.components.OutlinedTextField
import com.sebastianvm.bgcomp.designsys.components.RadioButton
import com.sebastianvm.bgcomp.designsys.components.Scaffold
import com.sebastianvm.bgcomp.designsys.components.Text
import com.sebastianvm.bgcomp.designsys.icons.IconAdd
import com.sebastianvm.bgcomp.designsys.icons.IconRemove
import com.sebastianvm.bgcomp.designsys.icons.Icons
import com.sebastianvm.bgcomp.designsys.theme.BgCompTheme
import com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel.GameModeDataUpdated
import com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel.GameModeSelected
import com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel.GameModeType
import com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel.NewKombioGameState
import com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel.NewKombioGameUserAction
import com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel.PlayerAdded
import com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel.PlayerRemoved
import com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel.StartGame
import com.sebastianvm.bgcomp.model.GameMode
import com.sebastianvm.bgcomp.mvvm.Ui
import com.sebastianvm.bgcomp.resources.Res
import com.sebastianvm.bgcomp.resources.add_player
import com.sebastianvm.bgcomp.resources.allowed_reentries
import com.sebastianvm.bgcomp.resources.first_to_exceed
import com.sebastianvm.bgcomp.resources.first_to_exceed_description
import com.sebastianvm.bgcomp.resources.game_mode
import com.sebastianvm.bgcomp.resources.last_to_reach
import com.sebastianvm.bgcomp.resources.last_to_reach_description
import com.sebastianvm.bgcomp.resources.number_of_rounds
import com.sebastianvm.bgcomp.resources.player_number
import com.sebastianvm.bgcomp.resources.players_count
import com.sebastianvm.bgcomp.resources.points_configuration
import com.sebastianvm.bgcomp.resources.points_mode
import com.sebastianvm.bgcomp.resources.points_mode_description
import com.sebastianvm.bgcomp.resources.remove_player
import com.sebastianvm.bgcomp.resources.rounds_configuration
import com.sebastianvm.bgcomp.resources.rounds_mode
import com.sebastianvm.bgcomp.resources.rounds_mode_description
import com.sebastianvm.bgcomp.resources.start_game
import com.sebastianvm.bgcomp.resources.target_points
import com.sebastianvm.bgcomp.resources.win_condition
import com.sebastianvm.bgcomp.ui.UiString
import org.jetbrains.compose.resources.stringResource

object NewKombioGameUi : Ui<NewKombioGameState, NewKombioGameUserAction> {
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun invoke(
        state: NewKombioGameState,
        handle: (NewKombioGameUserAction) -> Unit,
        modifier: Modifier,
    ) {
        Scaffold(modifier = modifier.fillMaxSize().padding(vertical = 16.dp)) { paddingValues ->
            Column(
                modifier =
                    Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Game mode selection
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = UiString(Res.string.game_mode),
                                style = BgCompTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp),
                            )

                            ListItem(
                                headlineContent = { Text(UiString(Res.string.points_mode)) },
                                supportingContent = {
                                    Text(UiString(Res.string.points_mode_description))
                                },
                                leadingContent = {
                                    RadioButton(
                                        selected = state.gameMode is GameMode.Points,
                                        onClick = { handle(GameModeSelected(GameModeType.Points)) },
                                    )
                                },
                            )

                            ListItem(
                                headlineContent = { Text(UiString(Res.string.rounds_mode)) },
                                supportingContent = {
                                    Text(UiString(Res.string.rounds_mode_description))
                                },
                                leadingContent = {
                                    RadioButton(
                                        selected = state.gameMode is GameMode.Rounds,
                                        onClick = { handle(GameModeSelected(GameModeType.Rounds)) },
                                    )
                                },
                            )
                        }
                    }

                    // Game mode configuration
                    item {
                        when (val mode = state.gameMode) {
                            is GameMode.Points -> {
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = UiString(Res.string.points_configuration),
                                        style = BgCompTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 8.dp),
                                    )

                                    val points = remember { mutableIntStateOf(mode.points) }
                                    OutlinedTextField(
                                        value = points.intValue.toString(),
                                        onValueChange = { newValue ->
                                            points.intValue = newValue.toIntOrNull() ?: mode.points
                                            newValue.toIntOrNull()?.let { points ->
                                                handle(
                                                    GameModeDataUpdated(mode.copy(points = points))
                                                )
                                            }
                                        },
                                        label = { Text(UiString(Res.string.target_points)) },
                                        keyboardOptions =
                                            KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                        singleLine = true,
                                    )

                                    OutlinedTextField(
                                        value = mode.allowedReentries.toString(),
                                        onValueChange = { newValue ->
                                            newValue.toIntOrNull()?.let { reentries ->
                                                handle(
                                                    GameModeDataUpdated(
                                                        mode.copy(allowedReentries = reentries)
                                                    )
                                                )
                                            }
                                        },
                                        label = { Text(UiString(Res.string.allowed_reentries)) },
                                        keyboardOptions =
                                            KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                                        singleLine = true,
                                    )

                                    Text(
                                        text = UiString(Res.string.win_condition),
                                        style = BgCompTheme.typography.labelLarge,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                    )

                                    ListItem(
                                        headlineContent = {
                                            Text(UiString(Res.string.first_to_exceed))
                                        },
                                        supportingContent = {
                                            Text(UiString(Res.string.first_to_exceed_description))
                                        },
                                        leadingContent = {
                                            RadioButton(
                                                selected = mode.winOnFirstOverflow,
                                                onClick = {
                                                    handle(
                                                        GameModeDataUpdated(
                                                            mode.copy(winOnFirstOverflow = true)
                                                        )
                                                    )
                                                },
                                            )
                                        },
                                    )

                                    ListItem(
                                        headlineContent = {
                                            Text(UiString(Res.string.last_to_reach))
                                        },
                                        supportingContent = {
                                            Text(UiString(Res.string.last_to_reach_description))
                                        },
                                        leadingContent = {
                                            RadioButton(
                                                selected = !mode.winOnFirstOverflow,
                                                onClick = {
                                                    handle(
                                                        GameModeDataUpdated(
                                                            mode.copy(winOnFirstOverflow = false)
                                                        )
                                                    )
                                                },
                                            )
                                        },
                                    )
                                }
                            }

                            is GameMode.Rounds -> {
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = UiString(Res.string.rounds_configuration),
                                        style = BgCompTheme.typography.titleMedium,
                                        modifier = Modifier.padding(bottom = 8.dp),
                                    )

                                    val rounds = remember { mutableIntStateOf(mode.rounds) }

                                    OutlinedTextField(
                                        value = rounds.intValue.toString(),
                                        onValueChange = { newValue ->
                                            rounds.intValue = newValue.toIntOrNull() ?: mode.rounds
                                            newValue.toIntOrNull()?.let { rounds ->
                                                handle(
                                                    GameModeDataUpdated(mode.copy(rounds = rounds))
                                                )
                                            }
                                        },
                                        label = { Text(UiString(Res.string.number_of_rounds)) },
                                        keyboardOptions =
                                            KeyboardOptions(keyboardType = KeyboardType.Number),
                                        modifier = Modifier.fillMaxWidth(),
                                        singleLine = true,
                                    )
                                }
                            }
                        }
                    }

                    // Players section
                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text =
                                        stringResource(
                                            Res.string.players_count,
                                            state.playerNames.size,
                                        ),
                                    style = BgCompTheme.typography.titleMedium,
                                )

                                Row {
                                    IconButton(
                                        icon = Icons.IconRemove,
                                        contentDescription =
                                            stringResource(Res.string.remove_player),
                                        onClick = { handle(PlayerRemoved) },
                                        enabled = state.playerNames.size > 2,
                                    )

                                    IconButton(
                                        icon = Icons.IconAdd,
                                        contentDescription = stringResource(Res.string.add_player),
                                        onClick = { handle(PlayerAdded) },
                                        enabled = state.playerNames.size < 6,
                                    )
                                }
                            }
                        }
                    }

                    itemsIndexed(
                        items = state.playerNames,
                        key = { index, _ -> "player_$index" },
                    ) { index, playerName ->
                        val focusManager = LocalFocusManager.current
                        OutlinedTextField(
                            state = playerName,
                            label = { Text(stringResource(Res.string.player_number, index + 1)) },
                            keyboardOptions =
                                KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences,
                                    imeAction =
                                        if (index == state.playerNames.lastIndex) ImeAction.Done
                                        else ImeAction.Next,
                                ),
                            onKeyboardAction = {
                                if (index == state.playerNames.lastIndex) {
                                    focusManager.clearFocus()
                                } else {
                                    focusManager.moveFocus(FocusDirection.Next)
                                }
                            },
                            modifier = Modifier.fillMaxWidth().animateItem(),
                            singleLine = true,
                        )
                    }
                }

                Button(
                    text = UiString(Res.string.start_game),
                    onClick = { handle(StartGame) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.playerNames.all { it.text.isNotBlank() },
                )
            }
        }
    }
}
