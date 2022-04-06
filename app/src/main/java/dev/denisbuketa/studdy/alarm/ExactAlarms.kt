package dev.denisbuketa.studdy.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import dev.denisbuketa.studdy.StuddyApplication
import dev.denisbuketa.studdy.debugLog

const val EXACT_ALARM_KEY = "exact_alarm"
const val EXACT_ALARM_REQUEST_CODE = 1001

class ExactAlarms(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    var exactAlarmState: MutableState<Long> = mutableStateOf(-1L)

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun ensureAlarmSet() {
        debugLog("ensureAlarmSet called()")
        val alarm: Long = sharedPreferences.getLong(EXACT_ALARM_KEY, -1L)
        if (alarm != -1L && canScheduleExactAlarms()) {
            setExactAlarm(alarm)
        } else {
            clearExactAlarm()
        }
    }

    fun setExactAlarm(triggerAtMillis: Long) {
        debugLog("setExactAlarm() called")
        setExactAlarmSetAlarmClock(triggerAtMillis)
        sharedPreferences.edit().putLong(EXACT_ALARM_KEY, triggerAtMillis).apply()
        exactAlarmState.value = triggerAtMillis
    }

    fun clearExactAlarm() {
        debugLog("clearExactAlarm() called")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = createExactAlarmIntent()

        alarmManager.cancel(pendingIntent)
        sharedPreferences.edit().putLong(EXACT_ALARM_KEY, -1L).apply()
        exactAlarmState.value = -1L
    }

    fun canScheduleExactAlarms(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    private fun setExactAlarmSetExact(triggerAtMillis: Long) {
        val pendingIntent = createExactAlarmIntent()
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    private fun setExactAlarmSetExactAndAllowWhileIdle(triggerAtMillis: Long) {
        val pendingIntent = createExactAlarmIntent()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

    private fun setExactAlarmSetAlarmClock(triggerAtMillis: Long) {
        val pendingIntent = createExactAlarmIntent()
        val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerAtMillis, pendingIntent)
        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
    }

    private fun createExactAlarmIntent(): PendingIntent {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            EXACT_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show()
        debugLog("AlarmBroadcastReceiver onReceive() called")
        (context.applicationContext as StuddyApplication).exactAlarms.clearExactAlarm()
    }
}

class ResetAlarmsBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        debugLog("ResetAlarmsBroadcastReceiver onReceive() called")
        val action = intent.action
        if (action != null) {
            if (action == Intent.ACTION_BOOT_COMPLETED) {
                (context.applicationContext as StuddyApplication).exactAlarms.ensureAlarmSet()
            }
        }
    }
}