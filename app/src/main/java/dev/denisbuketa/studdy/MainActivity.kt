package dev.denisbuketa.studdy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.denisbuketa.studdy.ui.screens.HomeScreen
import dev.denisbuketa.studdy.ui.theme.StuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StuddyTheme {
                HomeScreen()
            }
        }
    }
}