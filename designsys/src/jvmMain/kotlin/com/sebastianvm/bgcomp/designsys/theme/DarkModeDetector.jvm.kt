package com.sebastianvm.bgcomp.designsys.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.awt.Toolkit
import kotlin.math.pow
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * JVM/Desktop implementation of dark mode detection. Detects system dark mode on Windows, macOS,
 * and Linux.
 */
@Composable
actual fun isSystemInDarkMode(): Boolean {
    var isDarkMode by remember { mutableStateOf(detectSystemDarkMode()) }

    // Poll for theme changes every 100ms
    LaunchedEffect(Unit) {
        while (isActive) {
            delay(100)
            isDarkMode = detectSystemDarkMode()
        }
    }

    return isDarkMode
}

/** Detects if the system is in dark mode across different operating systems. */
private fun detectSystemDarkMode(): Boolean {
    return try {
        val osName = System.getProperty("os.name").lowercase()

        when {
            osName.contains("mac") -> detectMacOSDarkMode()
            osName.contains("win") -> detectWindowsDarkMode()
            osName.contains("linux") || osName.contains("nix") -> detectLinuxDarkMode()
            else -> false
        }
    } catch (_: Exception) {
        // Fallback to false if detection fails
        false
    }
}

/** Detects dark mode on macOS by checking the AppleInterfaceStyle. */
private fun detectMacOSDarkMode(): Boolean {
    return try {
        val process =
            Runtime.getRuntime().exec(arrayOf("defaults", "read", "-g", "AppleInterfaceStyle"))
        val output = process.inputStream.bufferedReader().readText().trim()
        process.waitFor()
        output.equals("Dark", ignoreCase = true)
    } catch (_: Exception) {
        // If the command fails, it means dark mode is not set
        false
    }
}

/** Detects dark mode on Windows by checking registry values. */
private fun detectWindowsDarkMode(): Boolean {
    return try {
        val process =
            Runtime.getRuntime()
                .exec(
                    arrayOf(
                        "reg",
                        "query",
                        "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                        "/v",
                        "AppsUseLightTheme",
                    )
                )
        val output = process.inputStream.bufferedReader().readText()
        process.waitFor()

        // If AppsUseLightTheme is 0x0, dark mode is enabled
        output.contains("0x0")
    } catch (_: Exception) {
        // Fallback: try to detect from system properties
        detectFromSystemProperties()
    }
}

/** Detects dark mode on Linux by checking GTK theme or environment variables. */
private fun detectLinuxDarkMode(): Boolean {
    return try {
        // Check GTK theme preference
        val gtkTheme = System.getenv("GTK_THEME") ?: ""
        if (gtkTheme.contains("dark", ignoreCase = true)) {
            return true
        }

        // Try gsettings for GNOME
        val process =
            Runtime.getRuntime()
                .exec(arrayOf("gsettings", "get", "org.gnome.desktop.interface", "gtk-theme"))
        val output = process.inputStream.bufferedReader().readText().trim()
        process.waitFor()

        output.contains("dark", ignoreCase = true)
    } catch (_: Exception) {
        // Fallback to system properties
        detectFromSystemProperties()
    }
}

/**
 * Attempts to detect dark mode from Java system properties. This is a fallback method that checks
 * system colors to infer dark mode.
 */
private fun detectFromSystemProperties(): Boolean {
    return try {
        // Try to determine dark mode by checking system colors
        // If the window background is darker than the text, we're likely in dark mode
        val toolkit = Toolkit.getDefaultToolkit()

        // Get system colors - try different properties depending on platform
        val windowBackground =
            toolkit.getDesktopProperty("win.frame.backgroundColor") as? java.awt.Color
                ?: toolkit.getDesktopProperty("win.3d.backgroundColor") as? java.awt.Color

        val textColor =
            toolkit.getDesktopProperty("win.text.textColor") as? java.awt.Color
                ?: toolkit.getDesktopProperty("win.frame.textColor") as? java.awt.Color

        // Prefer comparing background vs text if both are available
        if (windowBackground != null && textColor != null) {
            val bgLuminance = calculateLuminance(windowBackground)
            val textLuminance = calculateLuminance(textColor)
            // If background is darker than text, we're in dark mode
            return bgLuminance < textLuminance
        }

        // Fall back to just checking if background is dark
        if (windowBackground != null) {
            val luminance = calculateLuminance(windowBackground)
            // If luminance is below 0.5, it's a dark background
            return luminance < 0.5
        }

        // Default to false if we can't determine
        false
    } catch (_: Exception) {
        false
    }
}

/**
 * Calculates the relative luminance of a color. Uses the W3C formula: 0.2126*R + 0.7152*G +
 * 0.0722*B
 */
private fun calculateLuminance(color: java.awt.Color): Double {
    // Convert RGB to relative luminance (0.0 to 1.0)
    val r = color.red / 255.0
    val g = color.green / 255.0
    val b = color.blue / 255.0

    // Apply sRGB gamma correction
    val rLinear = if (r <= 0.03928) r / 12.92 else ((r + 0.055) / 1.055).pow(2.4)
    val gLinear = if (g <= 0.03928) g / 12.92 else ((g + 0.055) / 1.055).pow(2.4)
    val bLinear = if (b <= 0.03928) b / 12.92 else ((b + 0.055) / 1.055).pow(2.4)

    // Calculate luminance
    return 0.2126 * rLinear + 0.7152 * gLinear + 0.0722 * bLinear
}
