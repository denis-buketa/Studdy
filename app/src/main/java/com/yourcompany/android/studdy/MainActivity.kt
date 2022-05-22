/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yourcompany.android.studdy

import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.yourcompany.android.R
import com.yourcompany.android.studdy.ui.home.HomeScreen
import com.yourcompany.android.studdy.ui.theme.StuddyTheme

/**
 * Main activity that holds the UI and servers as a starting activity.
 */
class MainActivity : ComponentActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    setTheme(R.style.Theme_Studdy)
    super.onCreate(savedInstanceState)
    val exactAlarms = (application as StuddyApplication).exactAlarms.apply {
      rescheduleAlarm()
    }
    val inexactAlarms = (application as StuddyApplication).inexactAlarms.apply {
      rescheduleAlarms()
    }
    setContent {
      StuddyTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
          val alarmRingtoneState = (application as StuddyApplication).alarmRingtoneState
          HomeScreen(
              exactAlarms = exactAlarms,
              inexactAlarms = inexactAlarms,
              onSchedulingAlarmNotAllowed = { openSettings() },
              showStopAlarmButton = alarmRingtoneState.value != null,
              onStopAlarmClicked = {
                  alarmRingtoneState.value?.stop()
                  alarmRingtoneState.value = null
              })
        }
      }
    }
  }

  private fun openSettings() {
      // TODO (4) Open Alarms & Reminders Screen
  }

  override fun onResume() {
    super.onResume()
    TimeFormat.is24HourFormat = DateFormat.is24HourFormat(this)
  }
}