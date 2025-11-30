package com.sebastianvm.bgcomp.designsys.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RadioButton(selected: Boolean, onClick: (() -> Unit)?, modifier: Modifier = Modifier) {
    androidx.compose.material3.RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
    )
}
