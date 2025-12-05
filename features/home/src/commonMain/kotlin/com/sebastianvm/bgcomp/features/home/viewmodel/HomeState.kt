package com.sebastianvm.bgcomp.features.home.viewmodel

import com.sebastianvm.bgcomp.model.Game
import com.sebastianvm.bgcomp.mvvm.UiState
import kotlinx.collections.immutable.ImmutableList

// All UI state classes should use Immutable collections.
data class HomeState(val games: ImmutableList<Game>) : UiState
