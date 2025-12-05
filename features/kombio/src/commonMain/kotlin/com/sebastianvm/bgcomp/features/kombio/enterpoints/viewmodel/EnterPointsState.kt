package com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel

import androidx.compose.foundation.text.input.TextFieldState
import com.sebastianvm.bgcomp.mvvm.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class EnterPointsState(
    val scores: ImmutableList<TextFieldState> = persistentListOf(),
    val playerNames: ImmutableList<String> = persistentListOf(),
    val roundNumber: Int = 1,
) : UiState
