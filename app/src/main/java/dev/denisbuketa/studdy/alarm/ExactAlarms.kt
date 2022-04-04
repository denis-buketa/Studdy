package dev.denisbuketa.studdy.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

    init {
        val prefAlarm: Long = sharedPreferences.getLong(EXACT_ALARM_KEY, -1L)
        val alarmIntent: PendingIntent? = getExactAlarmIntentOrNull()
        if (prefAlarm != -1L && alarmIntent != null) {
            exactAlarmState.value = prefAlarm
        } else {
            clearExactAlarm()
        }
    }

    fun setExactAlarmSetExact(triggerAtMillis: Long) {
        val pendingIntent = createExactAlarmIntent()
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )

        sharedPreferences.edit().putLong(EXACT_ALARM_KEY, triggerAtMillis).apply()
        exactAlarmState.value = triggerAtMillis
    }

    fun setExactAlarmSetExactAndAllowWhileIdle(triggerAtMillis: Long) {
        val pendingIntent = createExactAlarmIntent()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )

        sharedPreferences.edit().putLong(EXACT_ALARM_KEY, triggerAtMillis).apply()
        exactAlarmState.value = triggerAtMillis
    }

    fun setExactAlarmSetAlarmClock(triggerAtMillis: Long) {
        val pendingIntent = createExactAlarmIntent()
        val alarmClockInfo = AlarmManager.AlarmClockInfo(triggerAtMillis, pendingIntent)
        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)

        sharedPreferences.edit().putLong(EXACT_ALARM_KEY, triggerAtMillis).apply()
        exactAlarmState.value = triggerAtMillis
    }

    fun clearExactAlarm() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            EXACT_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        sharedPreferences.edit().putLong(EXACT_ALARM_KEY, -1L).apply()
        exactAlarmState.value = -1L
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

    @SuppressLint("UnspecifiedImmutableFlag")
    fun getExactAlarmIntentOrNull(): PendingIntent? {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            EXACT_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
    }
}

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Alarm", Toast.LENGTH_SHORT).show()
        debugLog("Alarm Received")
        (context.applicationContext as StuddyApplication).exactAlarms.clearExactAlarm()
    }
}