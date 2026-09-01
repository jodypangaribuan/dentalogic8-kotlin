package com.dentalogic.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.dentalogic.app.ui.MainScreen
import com.dentalogic.app.ui.theme.AppTheme
import com.dentalogic.app.ui.theme.DentalogicTheme

/**
 * Main Activity hosting the Jetpack Compose edge-to-edge UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var currentTheme by remember { mutableStateOf(AppTheme.LIGHT) }

            DentalogicTheme(appTheme = currentTheme) {
                MainScreen(
                    currentTheme = currentTheme,
                    onThemeChange = { currentTheme = it },
                )
            }
        }
    }
}
