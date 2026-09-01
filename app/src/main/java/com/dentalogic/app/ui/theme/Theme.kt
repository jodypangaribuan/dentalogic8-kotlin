package com.dentalogic.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SkyLightColorScheme = lightColorScheme(
    primary = SkyPrimaryLight,
    onPrimary = SkyOnPrimaryLight,
    primaryContainer = SkyPrimaryContainerLight,
    onPrimaryContainer = SkyOnPrimaryContainerLight,
    secondary = SkySecondaryLight,
    onSecondary = SkyOnSecondaryLight,
    secondaryContainer = SkySecondaryContainerLight,
    onSecondaryContainer = SkyOnSecondaryContainerLight,
    tertiary = SkyTertiaryLight,
    onTertiary = SkyOnTertiaryLight,
    tertiaryContainer = SkyTertiaryContainerLight,
    onTertiaryContainer = SkyOnTertiaryContainerLight,
    background = SkyBackgroundLight,
    onBackground = SkyOnBackgroundLight,
    surface = SkySurfaceLight,
    onSurface = SkyOnSurfaceLight,
    surfaceContainer = SkySurfaceContainerLight,
    surfaceContainerHighest = SkySurfaceContainerHighestLight,
    surfaceVariant = SkySurfaceVariantLight,
    onSurfaceVariant = SkyOnSurfaceVariantLight,
    outline = SkyOutlineLight,
)

private val SkyDarkColorScheme = darkColorScheme(
    primary = SkyPrimaryDark,
    onPrimary = SkyOnPrimaryDark,
    primaryContainer = SkyPrimaryContainerDark,
    onPrimaryContainer = SkyOnPrimaryContainerDark,
    secondary = SkySecondaryDark,
    onSecondary = SkyOnSecondaryDark,
    secondaryContainer = SkySecondaryContainerDark,
    onSecondaryContainer = SkyOnSecondaryContainerDark,
    tertiary = SkyTertiaryDark,
    onTertiary = SkyOnTertiaryDark,
    tertiaryContainer = SkyTertiaryContainerDark,
    onTertiaryContainer = SkyOnTertiaryContainerDark,
    background = SkyBackgroundDark,
    onBackground = SkyOnBackgroundDark,
    surface = SkySurfaceDark,
    onSurface = SkyOnSurfaceDark,
    surfaceContainer = SkySurfaceContainerDark,
    surfaceContainerHighest = SkySurfaceContainerHighestDark,
    surfaceVariant = SkySurfaceVariantDark,
    onSurfaceVariant = SkyOnSurfaceVariantDark,
    outline = SkyOutlineDark,
)

/**
 * App theme for Dentalogic8.
 * Utilizes Option 3: Tailwind Sky Blue palette (#0284C7 & #E0F2FE) across light and dark modes.
 */
@Composable
fun DentalogicTheme(
    appTheme: AppTheme = AppTheme.LIGHT,
    content: @Composable () -> Unit,
) {
    val darkTheme = appTheme.isDark()
    val colorScheme = if (darkTheme) SkyDarkColorScheme else SkyLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = APP_TYPOGRAPHY,
        content = content,
    )
}
