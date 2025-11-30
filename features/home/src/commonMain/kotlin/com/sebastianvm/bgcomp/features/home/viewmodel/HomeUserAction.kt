package com.sebastianvm.bgcomp.features.home.viewmodel

import androidx.compose.ui.unit.IntOffset
import com.sebastianvm.bgcomp.model.Game
import com.sebastianvm.bgcomp.mvvm.UserAction
import kotlin.uuid.Uuid

sealed interface HomeUserAction : UserAction

data class GameClicked(val game: Game) : HomeUserAction
