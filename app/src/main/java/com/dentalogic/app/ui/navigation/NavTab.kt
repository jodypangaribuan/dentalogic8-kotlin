package com.dentalogic.app.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.dentalogic.app.R

/**
 * The 4 main navigation tabs for Dentalogic.
 */
enum class NavTab(
    @param:StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    HOME(R.string.nav_home, Icons.Rounded.Home),
    SCAN(R.string.nav_scan, Icons.Rounded.CameraAlt),
    HISTORY(R.string.nav_history, Icons.Rounded.History),
    GUIDE(R.string.nav_guide, Icons.AutoMirrored.Rounded.MenuBook),
}
