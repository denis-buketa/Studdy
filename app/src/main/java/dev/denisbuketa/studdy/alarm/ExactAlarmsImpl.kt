package dev.denisbuketa.studdy.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dev.denisbuketa.studdy.debugLog

const val EXACT_ALARM_INTENT_REQUEST_CODE = 1001

class ExactAlarmsImpl(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) : ExactAlarms {

  private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
  private var exactAlarmState = mutableStateOf(ExactAlarm.NOT_SET)

  override fun getExactAlarmState(): State<ExactAlarm> = exactAlarmState

  override fun rescheduleAlarm() {
    debugLog("ExactAlarms rescheduleAlarm called()")

    val alarm: ExactAlarm = sharedPreferences.getExactAlarm()
    if (alarm.isSet() && alarm.isNotInPast() && canScheduleExactAlarms()) {
      scheduleExactAlarm(alarm)
    } else {
      clearExactAlarm()
    }
  }

  override fun scheduleExactAlarm(exactAlarm: ExactAlarm) {
    debugLog("ExactAlarms scheduleExactAlarm() called")

    setExactAlarmSetAlarmClock(exactAlarm.triggerAtMillis)
    sharedPreferences.putExactAlarm(exactAlarm)
    exactAlarmState.value = exactAlarm
  }

  override fun clearExactAlarm() {
    debugLog("ExactAlarms clearExactAlarm() called")

    val pendingIntent = createExactAlarmIntent()

    alarmManager.cancel(pendingIntent)
    sharedPreferences.clearExactAlarm()
    exactAlarmState.value = ExactAlarm.NOT_SET
  }

  override fun canScheduleExactAlarms(): Boolean =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        alarmManager.canScheduleExactAlarms()
      } else {
        true
      }

  private fun setExactAlarmSetExact(triggerAtMillis: Long) {
    val pendingIntent = createExactAlarmIntent()
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
  }

  private fun setExactAlarmSetExactAndAllowWhileIdle(triggerAtMillis: Long) {
    val pendingIntent = createExactAlarmIntent()
    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
  }

  private fun setExactAlarmSetAlarmClock(triggerAtMillis: Long) {
    val pendingIntent = createExactAlarmIntent()
    val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerAtMillis, pendingIntent)
    alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
  }

  private fun createExactAlarmIntent(): PendingIntent {
    val intent = Intent(context, ExactAlarmBroadcastReceiver::class.java)
    return PendingIntent.getBroadcast(
        context,
        EXACT_ALARM_INTENT_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )
  }
}