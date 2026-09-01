package com.dentalogic.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dentalogic.app.ui.MainScreen
import com.dentalogic.app.ui.theme.DentalogicTheme

/**
 * Main Activity hosting the Jetpack Compose edge-to-edge UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DentalogicTheme {
                MainScreen()
            }
        }
    }
}
