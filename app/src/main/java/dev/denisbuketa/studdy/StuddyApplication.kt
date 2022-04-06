package dev.denisbuketa.studdy

import android.app.Application
import dev.denisbuketa.studdy.alarm.ExactAlarms
import dev.denisbuketa.studdy.alarm.InexactAlarms

const val SHARED_PREFS = "alarms"

class StuddyApplication : Application() {

    lateinit var exactAlarms: ExactAlarms
    lateinit var inexactAlarms: InexactAlarms

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        exactAlarms = ExactAlarms(this, sharedPreferences)
        inexactAlarms = InexactAlarms(this, sharedPreferences)
    }
}