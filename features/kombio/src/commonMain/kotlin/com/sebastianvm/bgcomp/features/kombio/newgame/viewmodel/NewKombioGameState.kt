package com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel


import com.sebastianvm.bgcomp.common.serialization.SerializableList
import com.sebastianvm.bgcomp.model.GameMode
import com.sebastianvm.bgcomp.mvvm.SavedState
import com.sebastianvm.bgcomp.mvvm.UiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class NewKombioGameState(
    val playerNames: SerializableList<String> = persistentListOf("", ""),
    val gameMode: GameMode = GameMode.Points(),
) : UiState, SavedState

