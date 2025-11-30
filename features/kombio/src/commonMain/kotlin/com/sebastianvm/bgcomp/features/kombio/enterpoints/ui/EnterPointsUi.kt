package com.sebastianvm.bgcomp.features.kombio.enterpoints.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.sebastianvm.bgcomp.designsys.components.Button
import com.sebastianvm.bgcomp.designsys.components.Card
import com.sebastianvm.bgcomp.designsys.components.ListItem
import com.sebastianvm.bgcomp.designsys.components.OutlinedTextField
import com.sebastianvm.bgcomp.designsys.components.RadioButton
import com.sebastianvm.bgcomp.designsys.components.Scaffold
import com.sebastianvm.bgcomp.designsys.components.Text
import com.sebastianvm.bgcomp.designsys.theme.BgCompTheme
import com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel.EnterPointsState
import com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel.EnterPointsUserAction
import com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel.HandPointsChanged
import com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel.KombioCallerSelected
import com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel.SubmitRound
import com.sebastianvm.bgcomp.mvvm.Ui
import com.sebastianvm.bgcomp.resources.Res
import com.sebastianvm.bgcomp.resources.enter_points_for_round
import com.sebastianvm.bgcomp.resources.hand_points
import com.sebastianvm.bgcomp.resources.kombio_caller
import com.sebastianvm.bgcomp.resources.submit_round
import com.sebastianvm.bgcomp.ui.UiString
import org.jetbrains.compose.resources.stringResource

object EnterPointsUi : Ui<EnterPointsState, EnterPointsUserAction> {
    @Composable
    override fun invoke(state: EnterPointsState, handle: (EnterPointsUserAction) -> Unit, modifier: Modifier) {
        Scaffold(
            modifier = modifier.fillMaxSize().padding(vertical = 16.dp),
        ) { paddingValues ->
            Column(
                modifier = Modifier.fillMaxSize().padding(paddingValues).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(Res.string.enter_points_for_round, state.roundNumber),
                    style = BgCompTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(
                        items = state.playerNames,
                        key = { index, _ -> "player_$index" }
                    ) { index, playerName ->
                        val score = remember { mutableStateOf(state.handPoints[index]) }
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column {
                                Text(
                                    text = playerName,
                                    style = BgCompTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                val focusManager = LocalFocusManager.current

                                OutlinedTextField(
                                    value = score.value,
                                    onValueChange = { newValue ->
                                        score.value = newValue
                                        handle(HandPointsChanged(index, newValue))
                                    },
                                    label = { Text(UiString(Res.string.hand_points)) },
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = if (index == state.playerNames.lastIndex) ImeAction.Done else ImeAction.Next
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onNext = { focusManager.moveFocus(FocusDirection.Next) },
                                        onDone = { focusManager.clearFocus() }),
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                )
                            }
                        }
                    }

                    item {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = UiString(Res.string.kombio_caller),
                                style = BgCompTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            state.playerNames.forEachIndexed { index, playerName ->
                                ListItem(
                                    headlineContent = { Text(playerName) },
                                    leadingContent = {
                                        RadioButton(
                                            selected = state.kombioCallerIndex == index,
                                            onClick = { handle(KombioCallerSelected(index)) }
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                Button(
                    text = UiString(Res.string.submit_round),
                    onClick = { handle(SubmitRound) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.canSubmit
                )
            }
        }
    }
}
