package dev.denisbuketa.studdy.alarm

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf


interface InexactAlarms {

  fun getInexactAlarmState(): State<ExactAlarm>

  fun getWindowAlarmState(): State<WindowAlarm>

  fun getRepeatingAlarmState(): State<RepeatingAlarm>

  fun rescheduleAlarms()

  fun scheduleInexactAlarm(inexactAlarm: ExactAlarm)

  fun clearInexactAlarm()

  fun scheduleWindowAlarm(windowAlarm: WindowAlarm)

  fun clearWindowAlarm()

  fun scheduleRepeatingAlarm(repeatingAlarm: RepeatingAlarm)

  fun clearRepeatingAlarm()
}

object PreviewInexactAlarms : InexactAlarms {

  override fun getInexactAlarmState(): State<ExactAlarm> = mutableStateOf(ExactAlarm.NOT_SET)

  override fun getWindowAlarmState(): State<WindowAlarm> = mutableStateOf(WindowAlarm.NOT_SET)

  override fun getRepeatingAlarmState(): State<RepeatingAlarm> =
      mutableStateOf(RepeatingAlarm.NOT_SET)

  override fun rescheduleAlarms() {}

  override fun scheduleInexactAlarm(inexactAlarm: ExactAlarm) {}

  override fun clearInexactAlarm() {}

  override fun scheduleWindowAlarm(windowAlarm: WindowAlarm) {}

  override fun clearWindowAlarm() {}

  override fun scheduleRepeatingAlarm(repeatingAlarm: RepeatingAlarm) {}

  override fun clearRepeatingAlarm() {}
}