@file:Suppress("FunctionName")

package dev.denisbuketa.studdy.ui.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.denisbuketa.studdy.*
import dev.denisbuketa.studdy.alarm.InexactAlarms
import dev.denisbuketa.studdy.ui.composables.AlarmInput
import dev.denisbuketa.studdy.ui.composables.AlarmWithIntervalInput
import java.util.*

@Composable
fun RestTab(inexactAlarms: InexactAlarms) {

    val inexactAlarm by remember { inexactAlarms.inexactAlarmState }
    val inexactAlarmWindow by remember { inexactAlarms.inexactAlarmWindowState }
    val inexactRepeatingAlarm by remember { inexactAlarms.inexactAlarmRepeatingState }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            MinimalTime(inexactAlarms, inexactAlarm)
            Spacer(modifier = Modifier.height(16.dp))
            WindowTime(inexactAlarms, inexactAlarmWindow)
            Spacer(modifier = Modifier.height(16.dp))
            RepeatingTime(inexactAlarms, inexactRepeatingAlarm)

            Column(
                modifier = Modifier
                    .weight(1f, true)
                    .fillMaxWidth()
            ) {
                if (inexactAlarm != -1L) {
                    Text(
                        text = "Inexact alarm set: ${millisToHourAndMinute(inexactAlarm)}",
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                }

                if (inexactAlarmWindow.first != -1L && inexactAlarmWindow.second != -1L) {
                    Text(
                        text = "Inexact alarm window set: ${
                            alarmWithIntervalToHourAndMinute(
                                inexactAlarmWindow.first,
                                inexactAlarmWindow.second
                            )
                        }",
                        modifier = Modifier.align(alignment = Alignment.CenterHorizontally)
                    )
                }

                if (inexactRepeatingAlarm.first != -1L && inexactRepeatingAlarm.second != -1L) {
                    Text(
                        text = "Inexact repeating alarm set: ${
                            alarmWithIntervalToHourAndMinute(
                                inexactRepeatingAlarm.first,
                                inexactRepeatingAlarm.second
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
private fun MinimalTime(
    inexactAlarms: InexactAlarms,
    inexactAlarm: Long
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

        Row {
            Button(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    Log.d("debug_log", "onClick")
                    if (inputIsValid(hourInput, minuteInput)) {
                        showInputInvalidMessage = false
                        setAlarm(inexactAlarms, hourInput.toInt(), minuteInput.toInt())
                    } else {
                        showInputInvalidMessage = true
                    }
                }) {
                Text("Set")
            }
            if (inexactAlarm != -1L) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        Log.d("debug_log", "onClick clear")
                        inexactAlarms.clearInexactAlarm()
                    }) {
                    Text("Clear")
                }
            }
        }
    }
}

@Composable
private fun WindowTime(
    inexactAlarms: InexactAlarms,
    alarmWindow: Pair<Long, Long>
) {
    Text(
        text = "Set Rest Window",
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

        Row {
            Button(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    Log.d("debug_log", "onClick")

                    if (inputIsValid(hourInput, minuteInput, windowInput)) {
                        showInputInvalidMessage = false
                        setAlarmWindow(
                            inexactAlarms,
                            hourInput.toInt(),
                            minuteInput.toInt(),
                            windowInput.toInt()
                        )
                    } else {
                        showInputInvalidMessage = true
                    }
                }) {
                Text("Set")
            }
            if (alarmWindow.first != -1L && alarmWindow.second != -1L) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        Log.d("debug_log", "onClick clear")
                        inexactAlarms.clearInexactAlarmWindow()
                    }) {
                    Text("Clear")
                }
            }
        }
    }
}

@Composable
private fun RepeatingTime(
    inexactAlarms: InexactAlarms,
    repeatingAlarm: Pair<Long, Long>
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

        Row {
            Button(
                modifier = Modifier.align(Alignment.CenterVertically),
                onClick = {
                    Log.d("debug_log", "onClick")

                    if (inputIsValid2
                            (hourInput, minuteInput, intervalInput)) {
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
                }) {
                Text("Set")
            }
            if (repeatingAlarm.first != -1L && repeatingAlarm.second != -1L) {
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = {
                        Log.d("debug_log", "onClick clear")
                        inexactAlarms.clearInexactRepeatingAlarm()
                    }) {
                    Text("Clear")
                }
            }
        }

    }
}

private fun setAlarm(inexactAlarms: InexactAlarms, hour: Int, minute: Int) {
    debugLog("setAlarm() - hour: $hour, minute: $minute")

    val calendar = Calendar.getInstance()

    val currentTimeMillis = calendar.timeInMillis
    val proposedTimeMillis = calendar.apply { setHourAndMinute(hour, minute) }.timeInMillis

    val alarmTimeMillis: Long = if (proposedTimeMillis > currentTimeMillis) {
        proposedTimeMillis
    } else {
        val oneDayMillis: Int = 24 * 60 * 60 * 1000
        proposedTimeMillis + oneDayMillis
    }

    debugLog("alarmTimeMillis - $alarmTimeMillis")
    printMillisToTime(alarmTimeMillis)

    inexactAlarms.setInexactAlarmSet(proposedTimeMillis)

    debugLog("Alarm Set")
}

private fun setAlarmWindow(
    inexactAlarms: InexactAlarms,
    hour: Int,
    minute: Int,
    minuteWindowLength: Int
) {
    debugLog("setAlarmWindow() - hour: $hour, minute: $minute, minuteWindowLength: $minuteWindowLength")

    val calendar = Calendar.getInstance()

    val currentTimeMillis = calendar.timeInMillis
    val proposedTimeMillis = calendar.apply { setHourAndMinute(hour, minute) }.timeInMillis

    val alarmTimeMillis: Long = if (proposedTimeMillis > currentTimeMillis) {
        proposedTimeMillis
    } else {
        val oneDayMillis: Int = 24 * 60 * 60 * 1000
        proposedTimeMillis + oneDayMillis
    }

    debugLog("alarmTimeMillis - $alarmTimeMillis")
    printMillisToTime(alarmTimeMillis)

    val windowLengthMillis: Long = minuteToMillis(minuteWindowLength)

    inexactAlarms.setInexactAlarmWindow(proposedTimeMillis, windowLengthMillis)

    debugLog("Alarm Window Set")
}

private fun setRepeatingAlarm(
    inexactAlarms: InexactAlarms,
    hour: Int,
    minute: Int,
    minuteInterval: Int
) {
    debugLog("setRepeatingAlarm() - hour: $hour, minute: $minute, minuteInterval: $minuteInterval")

    val calendar = Calendar.getInstance()

    val currentTimeMillis = calendar.timeInMillis
    val proposedTimeMillis = calendar.apply { setHourAndMinute(hour, minute) }.timeInMillis

    val alarmTimeMillis: Long = if (proposedTimeMillis > currentTimeMillis) {
        proposedTimeMillis
    } else {
        val oneDayMillis: Int = 24 * 60 * 60 * 1000
        proposedTimeMillis + oneDayMillis
    }

    debugLog("alarmTimeMillis - $alarmTimeMillis")
    printMillisToTime(alarmTimeMillis)

    val intervalMillis: Long = minuteToMillis(minuteInterval)

    inexactAlarms.setInexactRepeatingAlarm(proposedTimeMillis, intervalMillis)

    debugLog("Repeating Alarm Set")
}

@Preview(showBackground = true)
@Composable
fun RestTabPreview() {
}