package com.sebastianvm.bgcomp.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun PredictiveBackHandler(
    enabled: Boolean,
    onIsInPredictiveBackChange: (isInPredictiveBack: Boolean) -> Unit,
    onBack: () -> Unit,
    onProgress: (progress: Float) -> Unit,
) {
    // No-op on JVM - predictive back is Android-specific
}
