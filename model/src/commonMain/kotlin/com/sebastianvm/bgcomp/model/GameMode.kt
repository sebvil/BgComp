package com.sebastianvm.bgcomp.model

import kotlinx.serialization.Serializable

@Serializable
sealed class GameMode {
    @Serializable
    data class Points(
        val points: Int = 100,
        val allowedReentries: Int = 0,
        val winOnFirstOverflow: Boolean = false,
    ) : GameMode()

    @Serializable data class Rounds(val rounds: Int = 5) : GameMode()
}
