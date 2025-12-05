package com.sebastianvm.bgcomp.features.home.viewmodel

import com.sebastianvm.bgcomp.model.Game
import com.sebastianvm.bgcomp.mvvm.UserAction

sealed interface HomeUserAction : UserAction

data class GameClicked(val game: Game) : HomeUserAction
