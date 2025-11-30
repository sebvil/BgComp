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

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import com.sebastianvm.bgcomp.designsys.components.ModalBottomSheet
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostState

class BottomSheetScene(
    override val key: Any,
    private val entry: NavEntry<NavHostState.ComponentWithPresentationModes>,
    override val previousEntries: List<NavEntry<NavHostState.ComponentWithPresentationModes>>,
    override val overlaidEntries: List<NavEntry<NavHostState.ComponentWithPresentationModes>>,
    private val onBack: () -> Unit,
) : OverlayScene<NavHostState.ComponentWithPresentationModes> {

    override val entries: List<NavEntry<NavHostState.ComponentWithPresentationModes>> =
        listOf(entry)

    override val content: @Composable (() -> Unit) = {
        ModalBottomSheet(onDismissRequest = onBack) { entry.Content() }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BottomSheetScene

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
        return "BottomSheetScene(key=$key, entry=$entry, previousEntries=$previousEntries, overlaidEntries=$overlaidEntries)"
    }

    class Strategy : SceneStrategy<NavHostState.ComponentWithPresentationModes> {

        override fun SceneStrategyScope<NavHostState.ComponentWithPresentationModes>.calculateScene(
            entries: List<NavEntry<NavHostState.ComponentWithPresentationModes>>
        ): Scene<NavHostState.ComponentWithPresentationModes>? {
            val lastEntry = entries.lastOrNull()
            val isBottomSheet =
                lastEntry?.metadata()?.presentationMode is PresentationMode.BottomSheet
            if (!isBottomSheet) return null
            return BottomSheetScene(
                key = lastEntry.contentKey,
                entry = lastEntry,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                onBack = onBack,
            )
        }
    }
}

data class NavMetadata(val presentationMode: PresentationMode)

fun NavEntry<*>.metadata(): NavMetadata = metadata["metadata"] as NavMetadata
