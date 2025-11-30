package com.sebastianvm.bgcomp.designsys.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun BgCompTheme(content: @Composable () -> Unit) {
    val isDarkMode = isSystemInDarkMode()

    val colors =
        if (isDarkMode) {
            darkColorScheme()
        } else {
            lightColorScheme()
        }
    MaterialTheme(colorScheme = colors) { content() }
}

object BgCompTheme {

    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() =
            ColorScheme(
                surface = MaterialTheme.colorScheme.surface,
                surfaceVariant = MaterialTheme.colorScheme.surfaceVariant,
                onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant,
                onSurface = MaterialTheme.colorScheme.onSurface,
                background = MaterialTheme.colorScheme.background,
            )

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() =
            Typography(
                headlineMedium = MaterialTheme.typography.headlineMedium,
                headlineSmall = MaterialTheme.typography.headlineSmall,
                labelLarge = MaterialTheme.typography.labelLarge,
                titleLarge = MaterialTheme.typography.titleLarge,
                titleMedium = MaterialTheme.typography.titleMedium,
                titleSmall = MaterialTheme.typography.titleSmall,
                bodyLarge = MaterialTheme.typography.bodyLarge,
                bodyMedium = MaterialTheme.typography.bodyMedium,
                bodySmall = MaterialTheme.typography.bodySmall,
            )
}
