package com.sebastianvm.bgcomp.navigation.viewmodel

import com.sebastianvm.bgcomp.common.serialization.SerializableList
import com.sebastianvm.bgcomp.mvvm.SavedState
import com.sebastianvm.bgcomp.navigation.NavDestination
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class NavHostSavedState(val backStack: SerializableList<NavDestination> = persistentListOf()) :
    SavedState
