package dev.denisbuketa.studdy.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent

const val INEXACT_ALARM_REQUEST_CODE = 1002
const val INEXACT_WINDOW_ALARM_REQUEST_CODE = 1003
const val INEXACT_REPEATING_ALARM_REQUEST_CODE = 1004

fun setInexactAlarmSet(context: Context, triggerAtMillis: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, AlarmBroadcastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        INEXACT_ALARM_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
}

fun setInexactAlarmWindow(context: Context, windowStartMillis: Long, windowLengthMillis: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, AlarmBroadcastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        INEXACT_WINDOW_ALARM_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setWindow(
        AlarmManager.RTC_WAKEUP,
        windowStartMillis,
        windowLengthMillis,
        pendingIntent
    )
}

fun setInexactRepeatingAlarm(context: Context, windowStartMillis: Long, intervalMillis: Long) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, AlarmBroadcastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        INEXACT_REPEATING_ALARM_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setInexactRepeating(
        AlarmManager.RTC_WAKEUP,
        windowStartMillis,
        intervalMillis,
        pendingIntent
    )
}