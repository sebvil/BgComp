package com.sebastianvm.bgcomp.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface Ui<S : UiState, A : UserAction> {

    @Composable operator fun invoke(state: S, handle: (A) -> Unit, modifier: Modifier = Modifier)
}
