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

import android.content.SharedPreferences
import com.yourcompany.android.studdy.currentTimeMillis
import com.yourcompany.android.studdy.plusOneDay
import com.yourcompany.android.studdy.setHourAndMinute
import java.util.*

private const val EXACT_ALARM_PREFERENCES_KEY = "exact_alarm"
private const val INEXACT_ALARM_PREFERENCES_KEY = "inexact_alarm"
private const val INEXACT_WINDOW_ALARM_PREFERENCES_KEY = "inexact_window_alarm"
private const val INEXACT_REPEATING_ALARM_PREFERENCES_KEY = "inexact_repeating_alarm"

private const val ALARM_NOT_SET = -1L

fun SharedPreferences.getExactAlarm(): ExactAlarm {
  val triggerAtMillis = getLong(EXACT_ALARM_PREFERENCES_KEY, ALARM_NOT_SET)
  return ExactAlarm(triggerAtMillis)
}

fun SharedPreferences.putExactAlarm(exactAlarm: ExactAlarm) {
  edit().putLong(EXACT_ALARM_PREFERENCES_KEY, exactAlarm.triggerAtMillis).apply()
}

fun SharedPreferences.clearExactAlarm() {
  edit().putLong(EXACT_ALARM_PREFERENCES_KEY, ALARM_NOT_SET).apply()
}

fun SharedPreferences.getInexactAlarm(): ExactAlarm {
  val triggerAtMillis = getLong(INEXACT_ALARM_PREFERENCES_KEY, ALARM_NOT_SET)
  return ExactAlarm(triggerAtMillis)
}

fun SharedPreferences.putInexactAlarm(inexactAlarm: ExactAlarm) {
  edit().putLong(INEXACT_ALARM_PREFERENCES_KEY, inexactAlarm.triggerAtMillis).apply()
}

fun SharedPreferences.clearInexactAlarm() {
  edit().putLong(INEXACT_ALARM_PREFERENCES_KEY, ALARM_NOT_SET).apply()
}

fun SharedPreferences.getWindowAlarm(): WindowAlarm {
  return getStringSet(INEXACT_WINDOW_ALARM_PREFERENCES_KEY, setOf()).let {
    if (it.isNullOrEmpty()) {
      WindowAlarm.NOT_SET
    } else {
      val triggerAtMillis = it.elementAt(0).toLong()
      val windowLengthMillis = it.elementAt(1).toLong()
      WindowAlarm(triggerAtMillis, windowLengthMillis)
    }
  }
}

fun SharedPreferences.putWindowAlarm(windowAlarm: WindowAlarm) {
  edit()
      .putStringSet(
          INEXACT_WINDOW_ALARM_PREFERENCES_KEY,
          setOf(windowAlarm.triggerAtMillis.toString(), windowAlarm.windowLengthMillis.toString()))
      .apply()
}

fun SharedPreferences.clearWindowAlarm() {
  edit().putStringSet(INEXACT_WINDOW_ALARM_PREFERENCES_KEY, setOf()).apply()
}

fun SharedPreferences.getRepeatingAlarm(): RepeatingAlarm {
  return getStringSet(INEXACT_REPEATING_ALARM_PREFERENCES_KEY, setOf()).let {
    if (it.isNullOrEmpty()) {
      RepeatingAlarm.NOT_SET
    } else {
      val triggerAtMillis = it.elementAt(0).toLong()
      val intervalMillis = it.elementAt(1).toLong()
      RepeatingAlarm(triggerAtMillis, intervalMillis)
    }
  }
}

fun SharedPreferences.putRepeatingAlarm(repeatingAlarm: RepeatingAlarm) {
  edit()
      .putStringSet(
          INEXACT_REPEATING_ALARM_PREFERENCES_KEY,
          setOf(repeatingAlarm.triggerAtMillis.toString(),
              repeatingAlarm.intervalMillis.toString()))
      .apply()
}

fun SharedPreferences.clearRepeatingAlarm() {
  edit().putStringSet(INEXACT_REPEATING_ALARM_PREFERENCES_KEY, setOf()).apply()
}

fun convertToAlarmTimeMillis(hour: Int, minute: Int): Long {
  val calendar = Calendar.getInstance()
  val currentTimeMillis = calendar.timeInMillis
  val proposedTimeMillis = calendar.setHourAndMinute(hour, minute).timeInMillis

  val alarmTimeMillis: Long = if (proposedTimeMillis > currentTimeMillis) {
    proposedTimeMillis
  } else {
    proposedTimeMillis.plusOneDay()
  }

  return alarmTimeMillis
}

data class ExactAlarm(val triggerAtMillis: Long) {

  companion object {

    val NOT_SET = ExactAlarm(ALARM_NOT_SET)
  }

  fun isSet(): Boolean = triggerAtMillis != ALARM_NOT_SET

  fun isNotInPast(): Boolean = triggerAtMillis > currentTimeMillis()
}

data class WindowAlarm(val triggerAtMillis: Long, val windowLengthMillis: Long) {

  companion object {

    val NOT_SET = WindowAlarm(ALARM_NOT_SET, ALARM_NOT_SET)
  }

  fun isSet(): Boolean = triggerAtMillis != ALARM_NOT_SET && windowLengthMillis != ALARM_NOT_SET

  fun isNotInPast(): Boolean = triggerAtMillis > currentTimeMillis()
}

data class RepeatingAlarm(val triggerAtMillis: Long, val intervalMillis: Long) {

  companion object {

    val NOT_SET = RepeatingAlarm(ALARM_NOT_SET, ALARM_NOT_SET)
  }

  fun isSet(): Boolean = triggerAtMillis != ALARM_NOT_SET && intervalMillis != ALARM_NOT_SET
}