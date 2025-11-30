package com.sebastianvm.bgcomp.designsys.theme

import androidx.compose.runtime.Composable

/**
 * Detects if the system is currently in dark mode. Platform-specific implementation that checks
 * system dark mode preference.
 *
 * @return true if dark mode is enabled, false otherwise
 */
@Composable expect fun isSystemInDarkMode(): Boolean
