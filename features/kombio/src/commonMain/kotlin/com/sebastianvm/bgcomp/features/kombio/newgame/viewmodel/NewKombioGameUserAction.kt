package com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel

import com.sebastianvm.bgcomp.model.GameMode
import com.sebastianvm.bgcomp.mvvm.UserAction

sealed interface NewKombioGameUserAction : UserAction

data class GameModeSelected(val mode: GameModeType) : NewKombioGameUserAction

data class GameModeDataUpdated(val newGameModeData: GameMode) : NewKombioGameUserAction

data object PlayerAdded : NewKombioGameUserAction

data object PlayerRemoved : NewKombioGameUserAction

data object StartGame : NewKombioGameUserAction
