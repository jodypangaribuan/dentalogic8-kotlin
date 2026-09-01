package com.dentalogic.app.ui.theme

import androidx.compose.ui.graphics.Color

/** Seed primary palette used when dynamic color is unavailable (Android 11 and below). */
val SEED_PRIMARY = Color(0xFF006494) // Deep Teal / Dental Blue
val SEED_SECONDARY = Color(0xFF00A896) // Fresh Mint / Health Green
val SEED_TERTIARY = Color(0xFF028090) // Cyan Accent

/** Surface and background colors for light and dark themes. */
val LIGHT_BACKGROUND = Color(0xFFF8FAFC)
val LIGHT_SURFACE = Color(0xFFFFFFFF)
val LIGHT_SURFACE_CONTAINER = Color(0xFFEDF2F7)

val DARK_BACKGROUND = Color(0xFF0F172A)
val DARK_SURFACE = Color(0xFF1E293B)
val DARK_SURFACE_CONTAINER = Color(0xFF1A2234)

/** Clinical severity indicator colors for caries classification (D0 - D6). */
val ColorHealthy = Color(0xFF10B981) // D0 - Normal / Healthy (Emerald)
val ColorMild = Color(0xFFF59E0B) // D1..D2 - Enamel / Mild (Amber)
val ColorModerate = Color(0xFFF97316) // D3..D4 - Dentin / Moderate (Orange)
val ColorSevere = Color(0xFFEF4444) // D5..D6 - Pulp / Severe (Red)
