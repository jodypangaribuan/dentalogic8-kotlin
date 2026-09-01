package com.dentalogic.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

/**
 * User-selectable application theme modes.
 */
enum class AppTheme {
    SYSTEM,
    LIGHT,
    DARK;

    companion object {
        fun fromOrdinal(ordinal: Int): AppTheme = entries.getOrElse(ordinal) { SYSTEM }
    }
}

/**
 * Resolves the [AppTheme] to a boolean indicating whether dark theme should be applied.
 */
@Composable
fun AppTheme.isDark(): Boolean = when (this) {
    AppTheme.SYSTEM -> isSystemInDarkTheme()
    AppTheme.LIGHT -> false
    AppTheme.DARK -> true
}
