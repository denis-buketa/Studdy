package dev.denisbuketa.studdy.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dev.denisbuketa.studdy.StuddyApplication
import dev.denisbuketa.studdy.debugLog

class ResetAlarmsBroadcastReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    debugLog("ResetAlarmsBroadcastReceiver onReceive() called")

    val action = intent.action
    if (action != null) {
      if (action == Intent.ACTION_BOOT_COMPLETED) {
        (context.applicationContext as StuddyApplication).exactAlarms.rescheduleAlarm()
        (context.applicationContext as StuddyApplication).inexactAlarms.rescheduleAlarms()
      }
    }
  }
}