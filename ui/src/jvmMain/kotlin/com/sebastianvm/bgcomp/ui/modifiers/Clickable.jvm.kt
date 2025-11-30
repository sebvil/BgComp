package com.sebastianvm.bgcomp.ui.modifiers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.input.pointer.pointerInput
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
    return this.then(
            if (onSecondaryClickOrLongPress != null) {
                Modifier.pointerInput(Unit) {
                    detectTapGestures(matcher = PointerMatcher.mouse(PointerButton.Secondary)) {
                        onSecondaryClickOrLongPress(
                            position.value + IntOffset(it.x.toInt(), it.y.toInt())
                        )
                    }

                    detectTapGestures(
                        matcher = PointerMatcher.touch,
                        onLongPress = {
                            onSecondaryClickOrLongPress(
                                position.value + IntOffset(it.x.toInt(), it.y.toInt())
                            )
                        },
                    )
                }
            } else Modifier
        )
        .clickable(onClick = onClick)
        .onGloballyPositioned { coordinates ->
            position.value =
                coordinates.positionInWindow().let { IntOffset(it.x.toInt(), it.y.toInt()) }
        }
}
