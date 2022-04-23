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

@file:Suppress("FunctionName")

package com.yourcompany.android.studdy.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yourcompany.android.studdy.*
import com.yourcompany.android.studdy.alarm.ExactAlarm
import com.yourcompany.android.studdy.alarm.ExactAlarms
import com.yourcompany.android.studdy.alarm.PreviewExactAlarms
import com.yourcompany.android.studdy.alarm.convertToAlarmTimeMillis
import com.yourcompany.android.studdy.ui.composables.AlarmInput
import com.yourcompany.android.studdy.ui.composables.AlarmSetClearButtons

@SuppressLint("InlinedApi")
@Composable
fun StudyTab(
    exactAlarms: ExactAlarms,
    onSchedulingExactAlarmsNotAllowed: () -> Unit
) {

  val alarm by remember { exactAlarms.getExactAlarmState() }

  Box(modifier = Modifier.fillMaxSize()) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
      Text(
          text = "Set Study Alarm",
          modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp),
          fontSize = 18.sp
      )

      Row(modifier = Modifier.padding(top = 16.dp)) {

        var hourInput by remember { mutableStateOf("") }
        var minuteInput by remember { mutableStateOf("") }
        var showInputInvalidMessage by remember { mutableStateOf(false) }
        var isAm by remember { mutableStateOf(true) }
        val is24HourFormat = TimeFormat.is24HourFormat
        AlarmInput(
            hourInput = hourInput,
            minuteInput = minuteInput,
            onHourInputChanged = { hourInput = it },
            onMinuteInputChanged = { minuteInput = it },
            showInputInvalidMessage = showInputInvalidMessage,
            is24HourFormat = is24HourFormat,
            isAm = isAm,
            onIsAmEvent = { isAmValue -> isAm = isAmValue }
        )

        Spacer(Modifier.weight(1F, true))

        val focusManager = LocalFocusManager.current
        AlarmSetClearButtons(
            shouldShowClearButton = alarm.isSet(),
            onSetClicked = {
              if (hourInput.isValidHour(is24HourFormat) && minuteInput.isValidMinute()) {
                showInputInvalidMessage = false

                val hour: Int = if (is24HourFormat) {
                  hourInput.toInt()
                } else {
                  hourInput.toInt().toHour24Format(isAm)
                }
                scheduleAlarm(
                    exactAlarms,
                    hour,
                    minuteInput.toInt(),
                    onSchedulingExactAlarmsNotAllowed
                )
                focusManager.clearFocus()
              } else {
                showInputInvalidMessage = true
              }
            },
            onClearClicked = { exactAlarms.clearExactAlarm() }
        )
      }
    }


    if (alarm.isSet()) {
      Text(
          text = "Alarm set: ${toUserFriendlyText(alarm.triggerAtMillis)}",
          modifier = Modifier.align(Alignment.Center)
      )
    }
  }
}

private fun scheduleAlarm(
    exactAlarms: ExactAlarms,
    hour: Int,
    minute: Int,
    onSchedulingAlarmNotAllowed: () -> Unit
) {
  if (exactAlarms.canScheduleExactAlarms().not()) {
    onSchedulingAlarmNotAllowed.invoke()
    return
  }

  val alarmTimeMillis: Long = convertToAlarmTimeMillis(hour, minute)
  exactAlarms.scheduleExactAlarm(ExactAlarm(alarmTimeMillis))
}

@Preview(showBackground = true)
@Composable
fun StudyTabPreview() {
  StudyTab(PreviewExactAlarms) {}
}