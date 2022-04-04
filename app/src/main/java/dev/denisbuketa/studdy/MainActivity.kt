package dev.denisbuketa.studdy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import dev.denisbuketa.studdy.ui.home.HomeScreen
import dev.denisbuketa.studdy.ui.theme.StuddyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val exactAlarm = (application as StuddyApplication).exactAlarms
        setContent {
            StuddyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    HomeScreen(exactAlarm)
                }
            }
        }
    }
}