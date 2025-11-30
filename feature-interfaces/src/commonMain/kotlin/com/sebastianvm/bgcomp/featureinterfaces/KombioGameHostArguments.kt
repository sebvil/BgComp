package com.sebastianvm.bgcomp.featureinterfaces

import com.sebastianvm.bgcomp.model.GameMode
import com.sebastianvm.bgcomp.mvvm.MvvmComponentArguments
import kotlinx.serialization.Serializable

@Serializable
data class KombioGameHostArguments(val gameMode: GameMode, val playerNames: List<String>) : MvvmComponentArguments

