package dev.denisbuketa.studdy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
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
        val exactAlarms = (application as StuddyApplication).exactAlarms.apply {
            ensureAlarmSet()
        }
        val inexactAlarms = (application as StuddyApplication).inexactAlarms
        setContent {
            StuddyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    HomeScreen(exactAlarms, inexactAlarms) { openSettings() }
                }
            }
        }
    }

    private fun openSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            startActivity(Intent(ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
        }
    }
}