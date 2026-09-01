package com.dentalogic.app.ui

import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dentalogic.app.R
import com.dentalogic.app.ui.components.BackNavBar
import com.dentalogic.app.ui.components.ExpressiveNavBar
import com.dentalogic.app.ui.components.NavBarItem
import com.dentalogic.app.ui.navigation.NavTab
import com.dentalogic.app.ui.screens.GuideScreen
import com.dentalogic.app.ui.screens.HistoryScreen
import com.dentalogic.app.ui.screens.HomeScreen
import com.dentalogic.app.ui.screens.ProfileRoute
import com.dentalogic.app.ui.screens.ProfileTab
import com.dentalogic.app.ui.screens.ScanScreen
import com.dentalogic.app.ui.theme.AppTheme
import com.dentalogic.app.ui.theme.isDark

/**
 * Root of the in-app UI: displays app_background wallpaper, top & bottom scrims, floating expressive nav bar & back bar.
 */
@Composable
fun MainScreen(
    currentTheme: AppTheme,
    onThemeChange: (AppTheme) -> Unit,
) {
    val isDark = currentTheme.isDark()
    val bgDrawableRes = if (isDark) R.drawable.app_background_dark else R.drawable.app_background

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    var profileRoute by rememberSaveable { mutableStateOf(ProfileRoute.List) }
    val tabs = NavTab.entries
    val currentTab = tabs[selectedIndex]
    val haptics = LocalHapticFeedback.current

    val inProfileSubScreen = currentTab == NavTab.PROFILE && profileRoute != ProfileRoute.List
    val isScanTab = currentTab == NavTab.SCAN
    val showBackNavBar = isScanTab || inProfileSubScreen

    val handleBack: () -> Unit = {
        if (inProfileSubScreen) {
            profileRoute = ProfileRoute.List
        } else if (isScanTab) {
            selectedIndex = NavTab.HOME.ordinal
        }
    }

    PredictiveBackHandler(enabled = showBackNavBar) { progress ->
        try {
            progress.collect { _ -> }
            handleBack()
        } catch (_: Exception) {
        }
    }

    Scaffold { _ ->
        val navBarBottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val statusBarTopInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val topScrimHeight = statusBarTopInset + 36.dp

        val contentPadding = PaddingValues(
            start = 0.dp,
            end = 0.dp,
            top = if (isScanTab) 0.dp else (topScrimHeight + 8.dp),
            // Clear the floating nav bar (approx 64dp tall) + bottom margin + system bar
            bottom = 96.dp + navBarBottomInset,
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            // Fullscreen App Wallpaper Background (non-scan screens)
            if (!isScanTab) {
                Image(
                    painter = painterResource(bgDrawableRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            // Main Tab Content with fluid transition
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    fadeIn(animationSpec = tween(280)) togetherWith fadeOut(animationSpec = tween(200))
                },
                label = "mainTabTransition",
            ) { tab ->
                when (tab) {
                    NavTab.HOME -> HomeScreen(
                        contentPadding = contentPadding,
                        onNavigateToScan = { selectedIndex = NavTab.SCAN.ordinal },
                    )
                    NavTab.SCAN -> ScanScreen(contentPadding = contentPadding)
                    NavTab.HISTORY -> HistoryScreen(contentPadding = contentPadding)
                    NavTab.GUIDE -> GuideScreen(contentPadding = contentPadding)
                    NavTab.PROFILE -> ProfileTab(
                        contentPadding = contentPadding,
                        route = profileRoute,
                        currentTheme = currentTheme,
                        onThemeChange = onThemeChange,
                        onOpenChangelog = { profileRoute = ProfileRoute.Changelog },
                    )
                }
            }

            // Bottom Gradient Scrim for smooth content scroll fade (non-scan screens)
            if (!isScanTab) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(
                            Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.7f to MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f),
                                1f to MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.85f),
                            ),
                        ),
                )
            }

            // Top Status Bar Gradient Scrim (No TopAppBar design, non-scan screens)
            if (!isScanTab) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(topScrimHeight)
                        .background(
                            Brush.verticalGradient(
                                0f to MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.85f),
                                0.4f to MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f),
                                1f to Color.Transparent,
                            ),
                        ),
                )
            }

            val barModifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp)

            if (showBackNavBar) {
                val subTitle = when {
                    isScanTab -> stringResource(R.string.nav_scan)
                    profileRoute == ProfileRoute.Changelog -> stringResource(R.string.changelog_title)
                    else -> stringResource(R.string.profile_version)
                }
                BackNavBar(
                    title = subTitle,
                    onBack = handleBack,
                    modifier = barModifier,
                )
            } else {
                ExpressiveNavBar(
                    items = tabs.map { NavBarItem(stringResource(it.labelRes), it.icon) },
                    selectedIndex = selectedIndex,
                    onSelect = { index ->
                        if (index != selectedIndex) {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                        selectedIndex = index
                    },
                    modifier = barModifier,
                )
            }
        }
    }
}
