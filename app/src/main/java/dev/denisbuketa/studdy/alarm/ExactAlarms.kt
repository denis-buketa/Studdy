package dev.denisbuketa.studdy.alarm

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

interface ExactAlarms {

  fun getExactAlarmState(): State<ExactAlarm>

  fun rescheduleAlarm()

  fun scheduleExactAlarm(exactAlarm: ExactAlarm)

  fun clearExactAlarm()

  fun canScheduleExactAlarms(): Boolean
}

object PreviewExactAlarms : ExactAlarms {

  override fun getExactAlarmState(): State<ExactAlarm> = mutableStateOf(ExactAlarm.NOT_SET)

  override fun rescheduleAlarm() {}

  override fun scheduleExactAlarm(exactAlarm: ExactAlarm) {}

  override fun clearExactAlarm() {}

  override fun canScheduleExactAlarms(): Boolean = true
}