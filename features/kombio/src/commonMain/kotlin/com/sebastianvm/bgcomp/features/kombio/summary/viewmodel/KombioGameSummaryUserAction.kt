package com.sebastianvm.bgcomp.features.kombio.summary.viewmodel

import com.sebastianvm.bgcomp.mvvm.UserAction

sealed interface KombioGameSummaryUserAction : UserAction

data object EnterPoints : KombioGameSummaryUserAction

data object StartNewGame : KombioGameSummaryUserAction
