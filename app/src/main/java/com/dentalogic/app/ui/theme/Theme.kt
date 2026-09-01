package com.dentalogic.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BabyBlueLightColorScheme = lightColorScheme(
    primary = BabyBluePrimaryLight,
    onPrimary = BabyBlueOnPrimaryLight,
    primaryContainer = BabyBluePrimaryContainerLight,
    onPrimaryContainer = BabyBlueOnPrimaryContainerLight,
    secondary = BabyBlueSecondaryLight,
    onSecondary = BabyBlueOnSecondaryLight,
    secondaryContainer = BabyBlueSecondaryContainerLight,
    onSecondaryContainer = BabyBlueOnSecondaryContainerLight,
    tertiary = BabyBlueTertiaryLight,
    onTertiary = BabyBlueOnTertiaryLight,
    tertiaryContainer = BabyBlueTertiaryContainerLight,
    onTertiaryContainer = BabyBlueOnTertiaryContainerLight,
    background = BabyBlueBackgroundLight,
    onBackground = BabyBlueOnBackgroundLight,
    surface = BabyBlueSurfaceLight,
    onSurface = BabyBlueOnSurfaceLight,
    surfaceContainer = BabyBlueSurfaceContainerLight,
    surfaceVariant = BabyBlueSurfaceVariantLight,
    onSurfaceVariant = BabyBlueOnSurfaceVariantLight,
    outline = BabyBlueOutlineLight,
)

private val BabyBlueDarkColorScheme = darkColorScheme(
    primary = BabyBluePrimaryDark,
    onPrimary = BabyBlueOnPrimaryDark,
    primaryContainer = BabyBluePrimaryContainerDark,
    onPrimaryContainer = BabyBlueOnPrimaryContainerDark,
    secondary = BabyBlueSecondaryDark,
    onSecondary = BabyBlueOnSecondaryDark,
    secondaryContainer = BabyBlueSecondaryContainerDark,
    onSecondaryContainer = BabyBlueOnSecondaryContainerDark,
    tertiary = BabyBlueTertiaryDark,
    onTertiary = BabyBlueOnTertiaryDark,
    tertiaryContainer = BabyBlueTertiaryContainerDark,
    onTertiaryContainer = BabyBlueOnTertiaryContainerDark,
    background = BabyBlueBackgroundDark,
    onBackground = BabyBlueOnBackgroundDark,
    surface = BabyBlueSurfaceDark,
    onSurface = BabyBlueOnSurfaceDark,
    surfaceContainer = BabyBlueSurfaceContainerDark,
    surfaceVariant = BabyBlueSurfaceVariantDark,
    onSurfaceVariant = BabyBlueOnSurfaceVariantDark,
    outline = BabyBlueOutlineDark,
)

/**
 * App theme for Dentalogic.
 * Always utilizes the distinctive, fresh Baby Blue palette across light and dark modes.
 */
@Composable
fun DentalogicTheme(
    appTheme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit,
) {
    val darkTheme = appTheme.isDark()
    val colorScheme = if (darkTheme) BabyBlueDarkColorScheme else BabyBlueLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = APP_TYPOGRAPHY,
        content = content,
    )
}
