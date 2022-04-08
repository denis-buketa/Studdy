package dev.denisbuketa.studdy

import android.util.Log
import java.text.DecimalFormat
import java.util.*

fun String.isValidHour(): Boolean = this.toIntOrNull().let {
  it != null && it >= 0 && it <= 23
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

fun toUserFriendlyText(millis: Long): String {
  val decimalFormat = DecimalFormat("00")
  val calendar = Calendar.getInstance()
  calendar.timeInMillis = millis
  val hour = calendar.get(Calendar.HOUR_OF_DAY)
  val minute = calendar.get(Calendar.MINUTE)
  return "${decimalFormat.format(hour)}:${decimalFormat.format(minute)}"
}

fun toUserFriendlyText(millis: Long, intervalMillis: Long): String {
  val decimalFormat = DecimalFormat("00")
  val calendar = Calendar.getInstance()
  calendar.timeInMillis = millis
  val hour = calendar.get(Calendar.HOUR_OF_DAY)
  val minute = calendar.get(Calendar.MINUTE)

  val intervalMinute: Int = (intervalMillis / 1000 / 60).toInt()

  return "${decimalFormat.format(hour)}:${decimalFormat.format(minute)} " +
      "(${decimalFormat.format(intervalMinute)})"
}

fun debugLog(value: String) {
  Log.d("debug_log", value)
}

fun currentTimeMillis(): Long = Calendar.getInstance().timeInMillis