package com.sebastianvm.bgcomp.designsys.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

object DatePicker {
    /**
     * Design system wrapper for Material3 DatePickerState so app module doesn't depend on
     * Material3.
     */
    @ConsistentCopyVisibility
    data class State
    internal constructor(internal val material: androidx.compose.material3.DatePickerState) {
        val selectedDateMillis: Long?
            get() = material.selectedDateMillis
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    operator fun invoke(state: State, modifier: Modifier = Modifier) {
        androidx.compose.material3.DatePicker(state = state.material, modifier = modifier)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberDatePickerState(): DatePicker.State =
    DatePicker.State(androidx.compose.material3.rememberDatePickerState())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberDatePickerState(initialSelectedDateMillis: Long?): DatePicker.State =
    DatePicker.State(
        androidx.compose.material3.rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedDateMillis
        )
    )
