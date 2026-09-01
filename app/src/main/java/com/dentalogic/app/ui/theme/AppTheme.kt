package com.dentalogic.app.ui.theme

import androidx.annotation.StringRes
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.dentalogic.app.R

/**
 * The user's chosen color scheme. [SYSTEM] follows the device's light/dark setting.
 */
enum class AppTheme(@param:StringRes val labelRes: Int) {
    SYSTEM(R.string.theme_system),
    LIGHT(R.string.theme_light),
    DARK(R.string.theme_dark);

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
