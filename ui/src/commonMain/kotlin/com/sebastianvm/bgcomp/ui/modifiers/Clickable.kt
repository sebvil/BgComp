package com.sebastianvm.bgcomp.ui.modifiers

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset

@Composable
expect fun Modifier.combinedClickable(
    onClick: () -> Unit,
    onSecondaryClickOrLongPress: ((IntOffset) -> Unit)?,
): Modifier
