/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sebastianvm.bgcomp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import com.sebastianvm.bgcomp.designsys.theme.BgCompTheme
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostState

class PopUpScene(
    override val key: Any,
    private val entry: NavEntry<NavHostState.ComponentWithPresentationModes>,
    override val previousEntries: List<NavEntry<NavHostState.ComponentWithPresentationModes>>,
    override val overlaidEntries: List<NavEntry<NavHostState.ComponentWithPresentationModes>>,
    private val onBack: () -> Unit,
) : OverlayScene<NavHostState.ComponentWithPresentationModes> {

    override val entries: List<NavEntry<NavHostState.ComponentWithPresentationModes>> =
        listOf(entry)

    override val content: @Composable (() -> Unit) = {
        val density = LocalDensity.current
        Popup(
            onDismissRequest = { onBack() },
            popupPositionProvider =
                object : PopupPositionProvider {
                    override fun calculatePosition(
                        anchorBounds: IntRect,
                        windowSize: IntSize,
                        layoutDirection: LayoutDirection,
                        popupContentSize: IntSize,
                    ): IntOffset {
                        val desiredPosition =
                            (entry.metadata().presentationMode as PresentationMode.PopUp).position

                        // Convert 48dp to pixels
                        val minPaddingPx = with(density) { 48.dp.roundToPx() }

                        // Constrain x position
                        val constrainedX =
                            desiredPosition.x.coerceIn(
                                minimumValue = minPaddingPx,
                                maximumValue =
                                    (windowSize.width - popupContentSize.width - minPaddingPx)
                                        .coerceAtLeast(minPaddingPx),
                            )

                        // Constrain y position
                        val constrainedY =
                            desiredPosition.y.coerceIn(
                                minimumValue = minPaddingPx,
                                maximumValue =
                                    (windowSize.height - popupContentSize.height - minPaddingPx)
                                        .coerceAtLeast(minPaddingPx),
                            )

                        return IntOffset(constrainedX, constrainedY)
                    }
                },
        ) {
            Box(
                modifier =
                    Modifier.background(
                            BgCompTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(16.dp),
                        )
                        .width(IntrinsicSize.Min)
            ) {
                entry.Content()
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PopUpScene

        return key == other.key &&
            previousEntries == other.previousEntries &&
            overlaidEntries == other.overlaidEntries &&
            entry == other.entry
    }

    override fun hashCode(): Int {
        return key.hashCode() * 31 +
            previousEntries.hashCode() * 31 +
            overlaidEntries.hashCode() * 31 +
            entry.hashCode() * 31
    }

    override fun toString(): String {
        return "PopUpScene(key=$key, entry=$entry, previousEntries=$previousEntries, overlaidEntries=$overlaidEntries)"
    }

    class Strategy : SceneStrategy<NavHostState.ComponentWithPresentationModes> {

        override fun SceneStrategyScope<NavHostState.ComponentWithPresentationModes>.calculateScene(
            entries: List<NavEntry<NavHostState.ComponentWithPresentationModes>>
        ): Scene<NavHostState.ComponentWithPresentationModes>? {
            val lastEntry = entries.lastOrNull()
            val isBottomSheet = lastEntry?.metadata()?.presentationMode is PresentationMode.PopUp
            if (!isBottomSheet) return null
            return PopUpScene(
                key = lastEntry.contentKey,
                entry = lastEntry,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                onBack = onBack,
            )
        }
    }
}
