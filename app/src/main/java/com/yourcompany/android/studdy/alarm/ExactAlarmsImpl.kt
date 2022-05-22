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

package com.yourcompany.android.studdy.alarm

import android.app.AlarmManager
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

const val EXACT_ALARM_INTENT_REQUEST_CODE = 1001

class ExactAlarmsImpl(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : ExactAlarms {

  // TODO (1) Require an AlarmManager Instance

  private var exactAlarmState = mutableStateOf(ExactAlarm.NOT_SET)

  override fun getExactAlarmState(): State<ExactAlarm> = exactAlarmState

  override fun rescheduleAlarm() {
    val alarm: ExactAlarm = sharedPreferences.getExactAlarm()
    if (alarm.isSet() && alarm.isNotInPast() && canScheduleExactAlarms()) {
      scheduleExactAlarm(alarm)
    } else {
      clearExactAlarm()
    }
  }

  override fun scheduleExactAlarm(exactAlarm: ExactAlarm) {
    setExactAlarmSetAlarmClock(exactAlarm.triggerAtMillis)
    sharedPreferences.putExactAlarm(exactAlarm)
    exactAlarmState.value = exactAlarm
  }

  override fun clearExactAlarm() {
    // TODO (11) Cancel an exact alarm, clear preferences and update state
  }

  override fun canScheduleExactAlarms(): Boolean {
    // TODO (3) Check if app has a permission to schedule an exact alarm
    return true
  }

  private fun setExactAlarmSetExact(triggerAtMillis: Long) {
    // TODO (9) Schedule an exact alarm using setExact()
  }

  private fun setExactAlarmSetExactAndAllowWhileIdle(triggerAtMillis: Long) {
    // TODO (10) Schedule an exact alarm using setExactAndAllowWhileIdle()
  }

  private fun setExactAlarmSetAlarmClock(triggerAtMillis: Long) {
    // TODO (8) Schedule an exact alarm using setAlarmClock()
  }

  private fun createExactAlarmIntent() {
    // TODO (7) Create PendingIntent for an exact alarm
  }
}