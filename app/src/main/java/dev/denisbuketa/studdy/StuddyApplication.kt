package dev.denisbuketa.studdy

import android.app.Application
import dev.denisbuketa.studdy.alarm.ExactAlarms
import dev.denisbuketa.studdy.alarm.ExactAlarmsImpl
import dev.denisbuketa.studdy.alarm.InexactAlarmsImpl

const val SHARED_PREFS = "alarms"

class StuddyApplication : Application() {

  lateinit var exactAlarms: ExactAlarms
  lateinit var inexactAlarms: InexactAlarmsImpl

  override fun onCreate() {
    super.onCreate()
    val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
    exactAlarms = ExactAlarmsImpl(this, sharedPreferences)
    inexactAlarms = InexactAlarmsImpl(this, sharedPreferences)
  }
}