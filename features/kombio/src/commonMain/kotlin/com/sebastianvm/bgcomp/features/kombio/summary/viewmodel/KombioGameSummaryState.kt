package com.sebastianvm.bgcomp.features.kombio.summary.viewmodel

import com.sebastianvm.bgcomp.features.kombio.model.KombioGame
import com.sebastianvm.bgcomp.mvvm.UiState

data class KombioGameSummaryState(
    val game: KombioGame?
) : UiState
