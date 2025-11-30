package com.sebastianvm.bgcomp.features.kombio.model

import com.sebastianvm.bgcomp.mvvm.SavedState
import kotlinx.serialization.Serializable

@Serializable
data class KombioGameState(
    val game: KombioGame? = null
) : SavedState
