package com.sebastianvm.bgcomp.features.kombio.host

import com.sebastianvm.bgcomp.features.kombio.model.KombioGame
import com.sebastianvm.bgcomp.model.Game
import com.sebastianvm.bgcomp.mvvm.Props
import com.sebastianvm.bgcomp.navigation.viewmodel.NavigationProps

data class KombioGameHostProps(val navigationProps: NavigationProps, val game: KombioGame, val updateGame: (KombioGame) -> Unit) : Props
