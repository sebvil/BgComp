package com.sebastianvm.bgcomp.navigation

import androidx.activity.compose.PredictiveBackHandler as AndroidPredictiveBackHandler
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CancellationException

@Composable
actual fun PredictiveBackHandler(
    enabled: Boolean,
    onIsInPredictiveBackChange: (isInPredictiveBack: Boolean) -> Unit,
    onBack: () -> Unit,
    onProgress: (progress: Float) -> Unit,
) {
    onProgress(0f)
    AndroidPredictiveBackHandler(enabled = enabled) { backEvent ->
        try {
            backEvent.collect { event ->
                onIsInPredictiveBackChange(true)
                onProgress(event.progress)
            }
            // Back gesture completed - signal completion with progress 1.0
            onIsInPredictiveBackChange(false)
            onBack()
        } catch (_: CancellationException) {
            // Back gesture cancelled - reset to 0
            onIsInPredictiveBackChange(false)
        }
    }
}
