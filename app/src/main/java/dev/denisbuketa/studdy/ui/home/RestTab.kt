@file:Suppress("FunctionName")

package dev.denisbuketa.studdy.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.denisbuketa.studdy.*
import dev.denisbuketa.studdy.alarm.*
import dev.denisbuketa.studdy.ui.composables.AlarmInput
import dev.denisbuketa.studdy.ui.composables.AlarmSetClearButtons
import dev.denisbuketa.studdy.ui.composables.AlarmWithIntervalInput

@Composable
fun RestTab(inexactAlarms: InexactAlarms) {

  val inexactAlarm by remember { inexactAlarms.getInexactAlarmState() }
  val windowAlarm by remember { inexactAlarms.getWindowAlarmState() }
  val repeatingAlarm by remember { inexactAlarms.getRepeatingAlarmState() }

  Box(
      modifier = Modifier.fillMaxSize()
  ) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
      InexactAlarmInput(inexactAlarms, inexactAlarm)
      Spacer(modifier = Modifier.height(16.dp))
      WindowAlarmInput(inexactAlarms, windowAlarm)
      Spacer(modifier = Modifier.height(16.dp))
      RepeatingAlarmInput(inexactAlarms, repeatingAlarm)

      Column(
          modifier = Modifier
              .weight(1f, true)
              .fillMaxWidth()
      ) {
        if (inexactAlarm.isSet()) {
          Text(
              text = "Inexact alarm set: ${toUserFriendlyText(inexactAlarm.triggerAtMillis)}",
              modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
          )
        }

        if (windowAlarm.isSet()) {
          Text(
              text = "Window alarm set: ${
                toUserFriendlyText(windowAlarm.triggerAtMillis, windowAlarm.windowLengthMillis)
              }",
              modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
          )
        }

        if (repeatingAlarm.isSet()) {
          Text(
              text = "Repeating alarm set: ${
                toUserFriendlyText(
                    repeatingAlarm.triggerAtMillis,
                    repeatingAlarm.intervalMillis
                )
              }",
              modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
          )
        }
      }
    }
  }
}

@Composable
private fun InexactAlarmInput(
    inexactAlarms: InexactAlarms,
    inexactAlarm: ExactAlarm
) {
  Text(
      text = "Set Rest Alarm",
      modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
      fontSize = 18.sp
  )

  var hourInput by remember { mutableStateOf("") }
  var minuteInput by remember { mutableStateOf("") }
  var showInputInvalidMessage by remember { mutableStateOf(false) }

  Row(modifier = Modifier.padding(top = 16.dp)) {
    AlarmInput(
        hourInput = hourInput,
        minuteInput = minuteInput,
        onHourInputChanged = { hourInput = it },
        onMinuteInputChanged = { minuteInput = it },
        showInputInvalidMessage = showInputInvalidMessage
    )

    Spacer(Modifier.weight(1F, true))

    AlarmSetClearButtons(
        shouldShowClearButton = inexactAlarm.isSet(),
        onSetClicked = {
          if (hourInput.isValidHour() && minuteInput.isValidMinute()) {
            showInputInvalidMessage = false
            scheduleAlarm(inexactAlarms, hourInput.toInt(), minuteInput.toInt())
          } else {
            showInputInvalidMessage = true
          }
        },
        onClearClicked = { inexactAlarms.clearInexactAlarm() }
    )
  }
}

@Composable
private fun WindowAlarmInput(
    inexactAlarms: InexactAlarms,
    windowAlarm: WindowAlarm
) {
  Text(
      text = "Set Rest Window (min 10m)",
      modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
      fontSize = 18.sp
  )

  var hourInput by remember { mutableStateOf("") }
  var minuteInput by remember { mutableStateOf("") }
  var windowInput by remember { mutableStateOf("") }
  var showInputInvalidMessage by remember { mutableStateOf(false) }
  Row(modifier = Modifier.padding(top = 16.dp)) {
    AlarmWithIntervalInput(
        hourInput = hourInput,
        minuteInput = minuteInput,
        intervalInput = windowInput,
        onHourInputChanged = { hourInput = it },
        onMinuteInputChanged = { minuteInput = it },
        onIntervalInputChanged = { windowInput = it },
        showInputInvalidMessage = showInputInvalidMessage
    )

    Spacer(Modifier.weight(1F, true))

    AlarmSetClearButtons(
        shouldShowClearButton = windowAlarm.isSet(),
        onSetClicked = {
          if (hourInput.isValidHour()
              && minuteInput.isValidMinute()
              && windowInput.isValidWindowLength()) {
            showInputInvalidMessage = false
            scheduleWindowAlarm(
                inexactAlarms,
                hourInput.toInt(),
                minuteInput.toInt(),
                windowInput.toInt()
            )
          } else {
            showInputInvalidMessage = true
          }
        },
        onClearClicked = { inexactAlarms.clearWindowAlarm() }
    )
  }
}

@Composable
private fun RepeatingAlarmInput(
    inexactAlarms: InexactAlarms,
    repeatingAlarm: RepeatingAlarm
) {
  Text(
      text = "Set Repeating Rest Alarm",
      modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
      fontSize = 18.sp
  )

  var hourInput by remember { mutableStateOf("") }
  var minuteInput by remember { mutableStateOf("") }
  var intervalInput by remember { mutableStateOf("") }
  var showInputInvalidMessage by remember { mutableStateOf(false) }
  Row(modifier = Modifier.padding(top = 16.dp)) {
    AlarmWithIntervalInput(
        hourInput = hourInput,
        minuteInput = minuteInput,
        intervalInput = intervalInput,
        onHourInputChanged = { hourInput = it },
        onMinuteInputChanged = { minuteInput = it },
        onIntervalInputChanged = { intervalInput = it },
        showInputInvalidMessage = showInputInvalidMessage
    )

    Spacer(Modifier.weight(1F, true))

    AlarmSetClearButtons(
        shouldShowClearButton = repeatingAlarm.isSet(),
        onSetClicked = {
          if (hourInput.isValidHour() && minuteInput.isValidHour() && intervalInput.isNotZero()) {
            showInputInvalidMessage = false
            setRepeatingAlarm(
                inexactAlarms,
                hourInput.toInt(),
                minuteInput.toInt(),
                intervalInput.toInt()
            )
          } else {
            showInputInvalidMessage = true
          }
        },
        onClearClicked = { inexactAlarms.clearRepeatingAlarm() }
    )
  }
}

private fun scheduleAlarm(inexactAlarms: InexactAlarms, hour: Int, minute: Int) {
  val alarmTimeMillis: Long = convertToAlarmTimeMillis(hour, minute)
  inexactAlarms.scheduleInexactAlarm(ExactAlarm(alarmTimeMillis))
}

private fun scheduleWindowAlarm(
    inexactAlarms: InexactAlarms,
    hour: Int,
    minute: Int,
    minuteWindowLength: Int
) {
  val alarmTimeMillis: Long = convertToAlarmTimeMillis(hour, minute)
  val windowLengthMillis: Long = minuteWindowLength.toMillis()
  inexactAlarms.scheduleWindowAlarm(WindowAlarm(alarmTimeMillis, windowLengthMillis))
}

private fun setRepeatingAlarm(
    inexactAlarms: InexactAlarms,
    hour: Int,
    minute: Int,
    minuteInterval: Int
) {
  val alarmTimeMillis: Long = convertToAlarmTimeMillis(hour, minute)
  val intervalMillis: Long = minuteInterval.toMillis()
  inexactAlarms.scheduleRepeatingAlarm(RepeatingAlarm(alarmTimeMillis, intervalMillis))
}

@Preview(showBackground = true)
@Composable
fun RestTabPreview() {
  RestTab(PreviewInexactAlarms)
}