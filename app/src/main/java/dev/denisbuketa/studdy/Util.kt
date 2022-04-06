package dev.denisbuketa.studdy

import android.util.Log
import java.text.DecimalFormat
import java.util.*

fun debugLog(value: String) {
    Log.d("debug_log", value)
}

fun printMillisToTime(millis: Long) {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val seconds = calendar.get(Calendar.SECOND)
    val milliss = calendar.get(Calendar.MILLISECOND)
    debugLog("$hour:$minute:$seconds:$milliss (hour:minute:seconds:millis)")
}

fun millisToHourAndMinute(millis: Long): String {
    val decimalFormat = DecimalFormat("00")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    return "${decimalFormat.format(hour)}:${decimalFormat.format(minute)}"
}

fun Calendar.setHourAndMinute(hour: Int, minute: Int) {
    set(Calendar.HOUR_OF_DAY, hour)
    set(Calendar.MINUTE, minute)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

fun minuteToMillis(minute: Int): Long = minute * 60 * 1000L

fun currentTimeMillis(): Long = Calendar.getInstance().timeInMillis

fun inputIsValid(hourInput: String, minuteInput: String): Boolean {
    val hour = hourInput.toIntOrNull()
    val isValidHour = hour.let { hourValue ->
        hourValue != null && hourValue >= 0 && hourValue <= 23
    }

    val minute = minuteInput.toIntOrNull()
    val isValidMinute = minute.let { minutesValue ->
        minutesValue != null && minutesValue >= 0 && minutesValue <= 59
    }

    return isValidHour && isValidMinute
}

fun inputIsValid(hourInput: String, minuteInput: String, minuteWindowInput: String): Boolean {
    val hour = hourInput.toIntOrNull()
    val isValidHour = hour != null && hour >= 0 && hour <= 23

    val minute = minuteInput.toIntOrNull()
    val isValidMinute = minute != null && minute >= 0 && minute <= 59

    val minuteWindow = minuteWindowInput.toIntOrNull()
    val isValidMinuteWindow = minuteWindow != null && minuteWindow >= 10 && minuteWindow <= 60

    return isValidHour && isValidMinute && isValidMinuteWindow
}