package com.sebastianvm.bgcomp.designsys.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

/**
 * Android implementation of dark mode detection. Uses the built-in Compose function to detect
 * system dark mode.
 */
@Composable
actual fun isSystemInDarkMode(): Boolean {
    return isSystemInDarkTheme()
}
