package com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.sebastianvm.bgcomp.model.GameMode
import com.sebastianvm.bgcomp.mvvm.SavedState
import com.sebastianvm.bgcomp.mvvm.UiState

data class NewKombioGameState(
    val playerNames: SnapshotStateList<TextFieldState>,
    val gameMode: GameMode = GameMode.Points(),
) : UiState, SavedState
