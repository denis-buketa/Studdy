@file:Suppress("FunctionName")

package dev.denisbuketa.studdy.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.denisbuketa.studdy.alarm.ExactAlarm
import dev.denisbuketa.studdy.alarm.ExactAlarms
import dev.denisbuketa.studdy.alarm.PreviewExactAlarms
import dev.denisbuketa.studdy.alarm.convertToAlarmTimeMillis
import dev.denisbuketa.studdy.isValidHour
import dev.denisbuketa.studdy.isValidMinute
import dev.denisbuketa.studdy.toUserFriendlyText
import dev.denisbuketa.studdy.ui.composables.AlarmInput
import dev.denisbuketa.studdy.ui.composables.AlarmSetClearButtons

@SuppressLint("InlinedApi")
@Composable
fun StudyTab(
    exactAlarms: ExactAlarms,
    onSchedulingExactAlarmsNotAllowed: () -> Unit
) {

  val alarm by remember { exactAlarms.getExactAlarmState() }

  Box(modifier = Modifier.fillMaxSize()) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
      Text(
          text = "Set Study Alarm",
          modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp),
          fontSize = 18.sp
      )

      Row(modifier = Modifier.padding(top = 16.dp)) {

        var hourInput by remember { mutableStateOf("") }
        var minuteInput by remember { mutableStateOf("") }
        var showInputInvalidMessage by remember { mutableStateOf(false) }
        AlarmInput(
            hourInput = hourInput,
            minuteInput = minuteInput,
            onHourInputChanged = { hourInput = it },
            onMinuteInputChanged = { minuteInput = it },
            showInputInvalidMessage = showInputInvalidMessage
        )

        Spacer(Modifier.weight(1F, true))

        AlarmSetClearButtons(
            shouldShowClearButton = alarm.isSet(),
            onSetClicked = {
              if (hourInput.isValidHour() && minuteInput.isValidMinute()) {
                showInputInvalidMessage = false
                scheduleAlarm(
                    exactAlarms,
                    hourInput.toInt(),
                    minuteInput.toInt(),
                    onSchedulingExactAlarmsNotAllowed
                )
              } else {
                showInputInvalidMessage = true
              }
            },
            onClearClicked = { exactAlarms.clearExactAlarm() }
        )
      }
    }


    if (alarm.isSet()) {
      Text(
          text = "Alarm set: ${toUserFriendlyText(alarm.triggerAtMillis)}",
          modifier = Modifier.align(Alignment.Center)
      )
    }
  }
}

private fun scheduleAlarm(
    exactAlarms: ExactAlarms,
    hour: Int,
    minute: Int,
    onSchedulingAlarmNotAllowed: () -> Unit
) {
  if (exactAlarms.canScheduleExactAlarms().not()) {
    onSchedulingAlarmNotAllowed.invoke()
    return
  }

  val alarmTimeMillis: Long = convertToAlarmTimeMillis(hour, minute)
  exactAlarms.scheduleExactAlarm(ExactAlarm(alarmTimeMillis))
}

@Preview(showBackground = true)
@Composable
fun StudyTabPreview() {
  StudyTab(PreviewExactAlarms) {}
}