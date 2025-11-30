package com.sebastianvm.bgcomp.navigation

import androidx.compose.runtime.Composable

@Composable
expect fun PredictiveBackHandler(
    enabled: Boolean,
    onIsInPredictiveBackChange: (isInPredictiveBack: Boolean) -> Unit,
    onBack: () -> Unit,
    onProgress: (progress: Float) -> Unit,
)
