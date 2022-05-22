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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.text.DecimalFormat
import java.util.*

/*
 * Contains useful util methods that makes the code more readable.
 */

fun String.isValidHour(is24HourFormat: Boolean = true): Boolean = this.toIntOrNull().let {
  if (is24HourFormat) {
    it != null && it >= 0 && it <= 23
  } else {
    it != null && it > 0 && it <= 12
  }
}

fun String.isValidMinute(): Boolean = this.toIntOrNull().let {
  it != null && it >= 0 && it <= 59
}

fun String.isValidWindowLength(): Boolean = this.toIntOrNull().let {
  it != null && it >= 10 && it <= 60
}

fun String.isNotZero(): Boolean = this.toIntOrNull().let {
  it != null && it > 0
}

fun Calendar.setHourAndMinute(hour: Int, minute: Int): Calendar {
  set(Calendar.HOUR_OF_DAY, hour)
  set(Calendar.MINUTE, minute)
  set(Calendar.SECOND, 0)
  set(Calendar.MILLISECOND, 0)

  return this
}

fun Long.plusOneDay(): Long = this + 24 * 60 * 60 * 1000

fun Int.toMillis(): Long = this * 60 * 1000L

fun toUserFriendlyText(millis: Long, is24HourFormat: Boolean = true): String {
  val decimalFormat = DecimalFormat("00")
  val calendar = Calendar.getInstance()
  calendar.timeInMillis = millis

  if (is24HourFormat) {
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    return "${decimalFormat.format(hour)}:${decimalFormat.format(minute)}"
  } else {
    val amPm = calendar.get(Calendar.AM_PM)
    val calendarHour = calendar.get(Calendar.HOUR)
    val hour: Int = if (calendarHour == 0) {
      12
    } else {
      calendarHour
    }
    val minute = calendar.get(Calendar.MINUTE)
    val amPmString = if (amPm == Calendar.AM) "AM" else "PM"
    return "${decimalFormat.format(hour)}:${decimalFormat.format(minute)} ($amPmString)"
  }
}

fun toUserFriendlyText(millis: Long, intervalMillis: Long, is24HourFormat: Boolean = true): String {
  val decimalFormat = DecimalFormat("00")
  val calendar = Calendar.getInstance()
  calendar.timeInMillis = millis

  val intervalMinute: Int = (intervalMillis / 1000 / 60).toInt()

  if (is24HourFormat) {
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    return "${decimalFormat.format(hour)}:${decimalFormat.format(minute)} " +
        "(${decimalFormat.format(intervalMinute)})"
  } else {
    val amPm = calendar.get(Calendar.AM_PM)
    val calendarHour = calendar.get(Calendar.HOUR)
    val hour: Int = if (calendarHour == 0) {
      12
    } else {
      calendarHour
    }
    val minute = calendar.get(Calendar.MINUTE)
    val amPmString = if (amPm == Calendar.AM) "AM" else "PM"
    return "${decimalFormat.format(hour)}:${decimalFormat.format(minute)} " +
        "(${decimalFormat.format(intervalMinute)}) ($amPmString)"
  }
}

fun currentTimeMillis(): Long = Calendar.getInstance().timeInMillis

fun Int.toHour24Format(isAm: Boolean): Int {
  return if (isAm) {
    if (this == 12) {
      0
    } else {
      this
    }
  } else {
    if (this == 12) {
      12
    } else {
      this + 12
    }
  }
}

object TimeFormat {

  var is24HourFormat by mutableStateOf(false)
}