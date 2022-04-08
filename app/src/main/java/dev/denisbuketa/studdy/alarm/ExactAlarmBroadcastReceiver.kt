package dev.denisbuketa.studdy.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import dev.denisbuketa.studdy.StuddyApplication
import dev.denisbuketa.studdy.debugLog

class ExactAlarmBroadcastReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    debugLog("ExactAlarmBroadcastReceiver onReceive() called")

    Toast.makeText(context, "Exact Alarm Triggered", Toast.LENGTH_SHORT).show()
    (context.applicationContext as StuddyApplication).exactAlarms.clearExactAlarm()
  }
}