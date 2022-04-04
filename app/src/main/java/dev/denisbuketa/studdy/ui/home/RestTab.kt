@file:Suppress("FunctionName")

package dev.denisbuketa.studdy.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.denisbuketa.studdy.*
import dev.denisbuketa.studdy.alarm.setInexactAlarmSet
import dev.denisbuketa.studdy.alarm.setInexactAlarmWindow
import dev.denisbuketa.studdy.ui.composables.AlarmInput
import dev.denisbuketa.studdy.ui.composables.AlarmWithIntervalInput
import java.util.*

@Composable
fun RestTab() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            MinimalTime()
            Spacer(modifier = Modifier.height(16.dp))
            WindowTime()
            Spacer(modifier = Modifier.height(16.dp))
            RepeatingTime()
        }
    }
}

@Composable
private fun MinimalTime() {
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
        val context = LocalContext.current
        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                Log.d("debug_log", "onClick")

                if (inputIsValid(hourInput, minuteInput)) {
                    showInputInvalidMessage = false
                    setAlarm(context, hourInput.toInt(), minuteInput.toInt())
                } else {
                    showInputInvalidMessage = true
                }
            }) {
            Text("Set")
        }
    }
}

@Composable
private fun WindowTime() {
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

        val context = LocalContext.current
        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                Log.d("debug_log", "onClick")

                if (inputIsValid(hourInput, minuteInput, windowInput)) {
                    showInputInvalidMessage = false
                    setAlarm(context, hourInput.toInt(), minuteInput.toInt())
                } else {
                    showInputInvalidMessage = true
                }
            }) {
            Text("Set")
        }
    }
}

@Composable
private fun RepeatingTime() {
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

        val context = LocalContext.current
        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = {
                Log.d("debug_log", "onClick")

                if (inputIsValid(hourInput, minuteInput, intervalInput)) {
                    showInputInvalidMessage = false
                    setAlarm(context, hourInput.toInt(), minuteInput.toInt())
                } else {
                    showInputInvalidMessage = true
                }
            }) {
            Text("Set")
        }
    }
}

private fun setAlarm(context: Context, hour: Int, minute: Int) {
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

    setInexactAlarmSet(context, proposedTimeMillis)

    debugLog("Alarm Set")
}

private fun setAlarmWindow(context: Context, hour: Int, minute: Int, minuteWindow: Int) {
    debugLog("setAlarmWindow() - hour: $hour, minute: $minute, minuteWindow: $minute")

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

    val windowLengthMillis: Long = minuteToMillis(minute)

    setInexactAlarmWindow(context, proposedTimeMillis, windowLengthMillis)

    debugLog("Alarm Window Set")
}

@Preview(showBackground = true)
@Composable
fun RestTabPreview() {
    RestTab()
}