package com.dentalogic.app.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.dentalogic.app.R

/**
 * The 5 main navigation tabs for Dentalogic8.
 */
enum class NavTab(
    @param:StringRes val labelRes: Int,
    val icon: ImageVector? = null,
    @param:DrawableRes val drawableRes: Int? = null,
) {
    HOME(R.string.nav_home, drawableRes = R.drawable.ic_nav_home),
    SCAN(R.string.nav_scan, drawableRes = R.drawable.ic_scan_hero),
    HISTORY(R.string.nav_history, drawableRes = R.drawable.ic_nav_history),
    GUIDE(R.string.nav_guide, drawableRes = R.drawable.ic_nav_guide),
    PROFILE(R.string.nav_profile, icon = Icons.Rounded.Person),
}
