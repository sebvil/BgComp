package com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel

import com.sebastianvm.bgcomp.mvvm.UserAction

sealed interface EnterPointsUserAction : UserAction

data class SubmitRound(val scores: List<Int>, val kombioCaller: Int) : EnterPointsUserAction
