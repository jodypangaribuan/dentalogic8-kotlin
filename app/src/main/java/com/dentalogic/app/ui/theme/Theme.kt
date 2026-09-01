package com.dentalogic.app.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * App theme for Dentalogic.
 * Supports Material You dynamic color on Android 12+ (API 31+) and falls back
 * to a cohesive dental medical brand scheme on older platforms.
 */
@Composable
fun DentalogicTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val darkTheme = appTheme.isDark()
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)

        darkTheme -> darkColorScheme(
            primary = SEED_PRIMARY,
            secondary = SEED_SECONDARY,
            tertiary = SEED_TERTIARY,
            background = DARK_BACKGROUND,
            surface = DARK_SURFACE,
            surfaceContainer = DARK_SURFACE_CONTAINER,
        )

        else -> lightColorScheme(
            primary = SEED_PRIMARY,
            secondary = SEED_SECONDARY,
            tertiary = SEED_TERTIARY,
            background = LIGHT_BACKGROUND,
            surface = LIGHT_SURFACE,
            surfaceContainer = LIGHT_SURFACE_CONTAINER,
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = APP_TYPOGRAPHY,
        content = content,
    )
}
