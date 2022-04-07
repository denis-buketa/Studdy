package dev.denisbuketa.studdy.alarm

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
import dev.denisbuketa.studdy.currentTimeMillis
import dev.denisbuketa.studdy.debugLog

const val INEXACT_ALARM_KEY = "inexact_alarm"
const val INEXACT_ALARM_WINDOW_KEY = "inexact_alarm_window"
const val INEXACT_REPEATING_ALARM_KEY = "inexact_repeating_alarm"

const val INEXACT_ALARM_REQUEST_CODE = 1002
const val INEXACT_ALARM_WINDOW_REQUEST_CODE = 1003
const val INEXACT_REPEATING_ALARM_REQUEST_CODE = 1004

const val ALARM_REQUEST_CODE_EXTRA = "alarm_request_code_extra"

class InexactAlarms(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    var inexactAlarmState: MutableState<Long> = mutableStateOf(-1L)
    var inexactAlarmWindowState: MutableState<Pair<Long, Long>> = mutableStateOf(Pair(-1, -1))
    var inexactAlarmRepeatingState: MutableState<Pair<Long, Long>> = mutableStateOf(Pair(-1, -1))

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun ensureAlarmSet() {
        val inexactAlarm = sharedPreferences.getLong(INEXACT_ALARM_KEY, -1L)
        if (inexactAlarm != -1L && inexactAlarm > currentTimeMillis()) {
            setInexactAlarmSet(inexactAlarm)
        } else {
            clearInexactAlarm()
        }

        val inexactAlarmWindow =
            sharedPreferences.getStringSet(INEXACT_ALARM_WINDOW_KEY, setOf()).let {
                if (it.isNullOrEmpty()) {
                    Pair(-1L, -1L)
                } else {
                    Pair(it.elementAt(0).toLong(), it.elementAt(1).toLong())
                }
            }
        if (inexactAlarmWindow.first != -1L
            && inexactAlarmWindow.second != -1L
            && inexactAlarmWindow.first > currentTimeMillis()
        ) {
            setInexactAlarmWindow(inexactAlarmWindow.first, inexactAlarmWindow.second)
        } else {
            clearInexactAlarmWindow()
        }

        val inexactAlarmRepeating =
            sharedPreferences.getStringSet(INEXACT_REPEATING_ALARM_KEY, setOf()).let {
                if (it.isNullOrEmpty()) {
                    Pair(-1L, -1L)
                } else {
                    Pair(it.elementAt(0).toLong(), it.elementAt(1).toLong())
                }
            }
        if (inexactAlarmRepeating.first != -1L
            && inexactAlarmRepeating.second != -1L
        ) {
            setInexactRepeatingAlarm(inexactAlarmRepeating.first, inexactAlarmRepeating.second)
        } else {
            clearInexactRepeatingAlarm()
        }
    }

    fun setInexactAlarmSet(triggerAtMillis: Long) {
        val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_REQUEST_CODE)
        alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)

        sharedPreferences.edit().putLong(INEXACT_ALARM_KEY, triggerAtMillis).apply()
        inexactAlarmState.value = triggerAtMillis
    }

    fun clearInexactAlarm() {
        debugLog("clearInexactAlarm called")
        val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_REQUEST_CODE)

        alarmManager.cancel(pendingIntent)
        sharedPreferences.edit().putLong(INEXACT_ALARM_KEY, -1L).apply()
        inexactAlarmState.value = -1L
    }

    fun setInexactAlarmWindow(windowStartMillis: Long, windowLengthMillis: Long) {
        val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_WINDOW_REQUEST_CODE)
        alarmManager.setWindow(
            AlarmManager.RTC_WAKEUP,
            windowStartMillis,
            windowLengthMillis,
            pendingIntent
        )

        val alarmWindow = setOf(windowStartMillis.toString(), windowLengthMillis.toString())
        sharedPreferences.edit().putStringSet(INEXACT_ALARM_WINDOW_KEY, alarmWindow).apply()
        inexactAlarmWindowState.value = Pair(windowStartMillis, windowLengthMillis)
    }

    fun clearInexactAlarmWindow() {
        debugLog("clearInexactAlarmWindow called")
        val pendingIntent = createInexactAlarmIntent(INEXACT_ALARM_WINDOW_REQUEST_CODE)

        alarmManager.cancel(pendingIntent)
        sharedPreferences.edit().putStringSet(INEXACT_ALARM_WINDOW_KEY, emptySet()).apply()
        inexactAlarmWindowState.value = Pair(-1, -1)
    }

    fun setInexactRepeatingAlarm(triggerAtMillis: Long, intervalMillis: Long) {
        val pendingIntent = createInexactAlarmIntent(INEXACT_REPEATING_ALARM_REQUEST_CODE)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            intervalMillis,
            pendingIntent
        )

        val repeatingAlarm = setOf(triggerAtMillis.toString(), intervalMillis.toString())
        sharedPreferences.edit().putStringSet(INEXACT_REPEATING_ALARM_KEY, repeatingAlarm).apply()
        inexactAlarmRepeatingState.value = Pair(triggerAtMillis, intervalMillis)
    }

    fun clearInexactRepeatingAlarm() {
        debugLog("clearInexactRepeatingAlarm called")
        val pendingIntent = createInexactAlarmIntent(INEXACT_REPEATING_ALARM_REQUEST_CODE)

        alarmManager.cancel(pendingIntent)
        sharedPreferences.edit().putStringSet(INEXACT_REPEATING_ALARM_KEY, emptySet()).apply()
        inexactAlarmRepeatingState.value = Pair(-1, -1)
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

class InexactAlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context, "Inexact Alarm Triggered", Toast.LENGTH_SHORT).show()
        debugLog("InexactAlarmBroadcastReceiver onReceive() called")

        val inexactAlarms = (context.applicationContext as StuddyApplication).inexactAlarms
        when (intent.getIntExtra(ALARM_REQUEST_CODE_EXTRA, -1)) {
            INEXACT_ALARM_REQUEST_CODE -> inexactAlarms.clearInexactAlarm()
            INEXACT_ALARM_WINDOW_REQUEST_CODE -> inexactAlarms.clearInexactAlarmWindow()
            INEXACT_REPEATING_ALARM_REQUEST_CODE -> {
                // Do nothing
            }
        }
    }
}