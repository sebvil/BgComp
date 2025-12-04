package com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel

import com.sebastianvm.bgcomp.common.serialization.SerializableList
import com.sebastianvm.bgcomp.mvvm.SavedState
import com.sebastianvm.bgcomp.mvvm.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class EnterPointsState(
    val playerNames: List<String> = persistentListOf(),
    val handPoints: List<String> = persistentListOf(),
    val kombioCallerIndex: Int = 0,
    val roundNumber: Int = 1,
    val isRestored: Boolean = false,
) : UiState, SavedState {
    val canSubmit: Boolean
        get() = handPoints.all { it.isNotBlank() && it.toIntOrNull() != null && it.toInt() >= -3 }
}
