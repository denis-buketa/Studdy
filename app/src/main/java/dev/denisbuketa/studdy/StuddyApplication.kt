package dev.denisbuketa.studdy

import android.app.Application
import dev.denisbuketa.studdy.alarm.ExactAlarms

const val SHARED_PREFS = "alarms"

class StuddyApplication : Application() {

    lateinit var exactAlarms: ExactAlarms

    override fun onCreate() {
        super.onCreate()
        exactAlarms = ExactAlarms(this, getSharedPreferences(SHARED_PREFS, MODE_PRIVATE))
    }
}