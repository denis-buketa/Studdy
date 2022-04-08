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

package com.raywenderlich.android.studdy.alarm

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

const val INEXACT_ALARM_REQUEST_CODE = 1002
const val INEXACT_ALARM_WINDOW_REQUEST_CODE = 1003
const val INEXACT_REPEATING_ALARM_REQUEST_CODE = 1004

const val ALARM_REQUEST_CODE_EXTRA = "alarm_request_code_extra"

class InexactAlarmsImpl(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : InexactAlarms {

  private var inexactAlarmState = mutableStateOf(ExactAlarm.NOT_SET)
  private var windowAlarmState = mutableStateOf(WindowAlarm.NOT_SET)
  private var repeatingAlarmState = mutableStateOf(RepeatingAlarm.NOT_SET)

  override fun getInexactAlarmState(): State<ExactAlarm> = inexactAlarmState

  override fun getWindowAlarmState(): State<WindowAlarm> = windowAlarmState

  override fun getRepeatingAlarmState(): State<RepeatingAlarm> = repeatingAlarmState

  override fun rescheduleAlarms() {
    val inexactAlarm = sharedPreferences.getInexactAlarm()
    if (inexactAlarm.isSet() && inexactAlarm.isNotInPast()) {
      scheduleInexactAlarm(inexactAlarm)
    } else {
      clearInexactAlarm()
    }

    val windowAlarm = sharedPreferences.getWindowAlarm()
    if (windowAlarm.isSet() && windowAlarm.isNotInPast()) {
      scheduleWindowAlarm(windowAlarm)
    } else {
      clearWindowAlarm()
    }

    val repeatingAlarm = sharedPreferences.getRepeatingAlarm()
    if (repeatingAlarm.isSet()) {
      scheduleRepeatingAlarm(repeatingAlarm)
    } else {
      clearRepeatingAlarm()
    }
  }

  override fun scheduleInexactAlarm(inexactAlarm: ExactAlarm) {
    // TODO Add logic for scheduling inexact alarm

    sharedPreferences.putInexactAlarm(inexactAlarm)
    inexactAlarmState.value = inexactAlarm
  }

  override fun clearInexactAlarm() {
    // TODO Add logic for clearing inexact alarm

    sharedPreferences.clearInexactAlarm()
    inexactAlarmState.value = ExactAlarm.NOT_SET
  }

  override fun scheduleWindowAlarm(windowAlarm: WindowAlarm) {
    // TODO Add logic for scheduling window alarm

    sharedPreferences.putWindowAlarm(windowAlarm)
    windowAlarmState.value = windowAlarm
  }

  override fun clearWindowAlarm() {
    // TODO Add logic for clearing window alarm

    sharedPreferences.clearWindowAlarm()
    windowAlarmState.value = WindowAlarm.NOT_SET
  }

  override fun scheduleRepeatingAlarm(repeatingAlarm: RepeatingAlarm) {
    // TODO Add logic for scheduling repeating alarm

    sharedPreferences.putRepeatingAlarm(repeatingAlarm)
    repeatingAlarmState.value = repeatingAlarm
  }

  override fun clearRepeatingAlarm() {
    // TODO Add logic for clearing repeating alarm

    sharedPreferences.clearRepeatingAlarm()
    repeatingAlarmState.value = RepeatingAlarm.NOT_SET
  }

  private fun createInexactAlarmIntent(alarmRequestCode: Int) {
    // TODO Add logic for creating alarm intent
  }
}