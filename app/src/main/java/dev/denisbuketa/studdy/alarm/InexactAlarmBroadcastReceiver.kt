package dev.denisbuketa.studdy.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import dev.denisbuketa.studdy.StuddyApplication
import dev.denisbuketa.studdy.debugLog

class InexactAlarmBroadcastReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    debugLog("InexactAlarmBroadcastReceiver onReceive() called")

    Toast.makeText(context, "Inexact Alarm Triggered", Toast.LENGTH_SHORT).show()
    val inexactAlarms = (context.applicationContext as StuddyApplication).inexactAlarms
    when (intent.getIntExtra(ALARM_REQUEST_CODE_EXTRA, -1)) {
      INEXACT_ALARM_REQUEST_CODE -> inexactAlarms.clearInexactAlarm()
      INEXACT_ALARM_WINDOW_REQUEST_CODE -> inexactAlarms.clearWindowAlarm()
      INEXACT_REPEATING_ALARM_REQUEST_CODE -> {
        // Do nothing
      }
    }
  }
}