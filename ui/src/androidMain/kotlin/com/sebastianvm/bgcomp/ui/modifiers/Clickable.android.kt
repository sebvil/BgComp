package com.sebastianvm.bgcomp.ui.modifiers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun Modifier.combinedClickable(
    onClick: () -> Unit,
    onSecondaryClickOrLongPress: ((IntOffset) -> Unit)?,
): Modifier {
    val position = remember { mutableStateOf(IntOffset.Zero) }
    return this.combinedClickable(
            onClick = onClick,
            onLongClick = onSecondaryClickOrLongPress?.let { { it.invoke(IntOffset.Zero) } },
        )
        .onGloballyPositioned { coordinates ->
            position.value =
                coordinates.positionInWindow().let { IntOffset(it.x.toInt(), it.y.toInt()) }
        }
}
