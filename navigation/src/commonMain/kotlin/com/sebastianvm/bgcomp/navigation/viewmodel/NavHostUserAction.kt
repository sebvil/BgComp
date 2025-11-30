package com.sebastianvm.bgcomp.navigation.viewmodel

import com.sebastianvm.bgcomp.mvvm.UserAction
import com.sebastianvm.bgcomp.navigation.NavDestination

sealed interface NavHostUserAction : UserAction

data class Push(val destination: NavDestination, val popCurrent: Boolean = false) :
    NavHostUserAction

data object Pop : NavHostUserAction
