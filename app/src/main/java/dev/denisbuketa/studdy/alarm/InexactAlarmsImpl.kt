package dev.denisbuketa.studdy.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dev.denisbuketa.studdy.debugLog

const val INEXACT_ALARM_REQUEST_CODE = 1002
const val INEXACT_ALARM_WINDOW_REQUEST_CODE = 1003
const val INEXACT_REPEATING_ALARM_REQUEST_CODE = 1004

const val ALARM_REQUEST_CODE_EXTRA = "alarm_request_code_extra"

class InexactAlarmsImpl(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : InexactAlarms {

  private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
  private var inexactAlarmState = mutableStateOf(ExactAlarm.NOT_SET)
  private var windowAlarmState = mutableStateOf(WindowAlarm.NOT_SET)
  private var repeatingAlarmState = mutableStateOf(RepeatingAlarm.NOT_SET)

  override fun getInexactAlarmState(): State<ExactAlarm> = inexactAlarmState

  override fun getWindowAlarmState(): State<WindowAlarm> = windowAlarmState

  override fun getRepeatingAlarmState(): State<RepeatingAlarm> = repeatingAlarmState

  override fun rescheduleAlarms() {
    debugLog("InexactAlarms rescheduleAlarm called()")

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
    debugLog("InexactAlarms scheduleInexactAlarm() called")

    val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_REQUEST_CODE)
    alarmManager.set(AlarmManager.RTC_WAKEUP, inexactAlarm.triggerAtMillis, pendingIntent)
    sharedPreferences.putInexactAlarm(inexactAlarm)
    inexactAlarmState.value = inexactAlarm
  }

  override fun clearInexactAlarm() {
    debugLog("InexactAlarms clearInexactAlarm() called")

    val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_REQUEST_CODE)
    alarmManager.cancel(pendingIntent)
    sharedPreferences.clearInexactAlarm()
    inexactAlarmState.value = ExactAlarm.NOT_SET
  }

  override fun scheduleWindowAlarm(windowAlarm: WindowAlarm) {
    debugLog("InexactAlarms scheduleWindowAlarm called")

    val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_WINDOW_REQUEST_CODE)
    alarmManager.setWindow(
        AlarmManager.RTC_WAKEUP,
        windowAlarm.triggerAtMillis,
        windowAlarm.windowLengthMillis,
        pendingIntent
    )
    sharedPreferences.putWindowAlarm(windowAlarm)
    windowAlarmState.value = windowAlarm
  }

  override fun clearWindowAlarm() {
    debugLog("InexactAlarms clearWindowAlarm called")

    val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_WINDOW_REQUEST_CODE)
    alarmManager.cancel(pendingIntent)
    sharedPreferences.clearWindowAlarm()
    windowAlarmState.value = WindowAlarm.NOT_SET
  }

  override fun scheduleRepeatingAlarm(repeatingAlarm: RepeatingAlarm) {
    debugLog("InexactAlarms scheduleRepeatingAlarm called")

    val pendingIntent = createInexactAlarmIntent(INEXACT_REPEATING_ALARM_REQUEST_CODE)
    alarmManager.setInexactRepeating(
        AlarmManager.RTC_WAKEUP,
        repeatingAlarm.triggerAtMillis,
        repeatingAlarm.intervalMillis,
        pendingIntent
    )
    sharedPreferences.putRepeatingAlarm(repeatingAlarm)
    repeatingAlarmState.value = repeatingAlarm
  }

  override fun clearRepeatingAlarm() {
    debugLog("InexactAlarms clearRepeatingAlarm called")

    val pendingIntent = createInexactAlarmIntent(INEXACT_REPEATING_ALARM_REQUEST_CODE)
    alarmManager.cancel(pendingIntent)
    sharedPreferences.clearRepeatingAlarm()
    repeatingAlarmState.value = RepeatingAlarm.NOT_SET
  }

  private fun createInexactAlarmIntent(alarmRequestCode: Int): PendingIntent {
    val intent = Intent(context, InexactAlarmBroadcastReceiver::class.java).apply {
      putExtra(ALARM_REQUEST_CODE_EXTRA, alarmRequestCode)
    }
    return PendingIntent.getBroadcast(
        context,
        alarmRequestCode,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )
  }
}