package com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel

import com.sebastianvm.bgcomp.mvvm.UserAction

sealed interface EnterPointsUserAction : UserAction

data class HandPointsChanged(val playerIndex: Int, val points: String) : EnterPointsUserAction
data class KombioCallerSelected(val playerIndex: Int) : EnterPointsUserAction
data object SubmitRound : EnterPointsUserAction
