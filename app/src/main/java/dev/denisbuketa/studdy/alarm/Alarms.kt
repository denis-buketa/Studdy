package dev.denisbuketa.studdy.alarm

import android.content.SharedPreferences
import dev.denisbuketa.studdy.currentTimeMillis
import dev.denisbuketa.studdy.plusOneDay
import dev.denisbuketa.studdy.setHourAndMinute
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