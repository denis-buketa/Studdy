@file:Suppress("FunctionName")

package dev.denisbuketa.studdy.ui.home

import android.annotation.SuppressLint
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
import dev.denisbuketa.studdy.alarm.ExactAlarms
import dev.denisbuketa.studdy.ui.composables.AlarmInput
import java.util.*

@SuppressLint("InlinedApi")
@Composable
fun StudyTab(
    exactAlarms: ExactAlarms,
    onSchedulingAlarmNotAllowed: () -> Unit
) {

    val alarm by remember { exactAlarms.exactAlarmState }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text(
                text = "Set Study Alarm",
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
                            Log.d("debug_log", "onClick set")
                            if (inputIsValid(hourInput, minuteInput)) {
                                showInputInvalidMessage = false
                                setAlarm(
                                    exactAlarms,
                                    hourInput.toInt(),
                                    minuteInput.toInt(),
                                    onSchedulingAlarmNotAllowed
                                )
                            } else {
                                showInputInvalidMessage = true
                            }
                        }) {
                        Text("Set")
                    }
                    if (alarm != -1L) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            onClick = {
                                Log.d("debug_log", "onClick clear")
                                exactAlarms.clearExactAlarm()
                            }) {
                            Text("Clear")
                        }
                    }
                }

            }
        }


        if (alarm != -1L) {
            Text(
                text = "Alarm set: ${millisToHourAndMinute(alarm)}",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

private fun setAlarm(
    exactAlarms: ExactAlarms,
    hour: Int,
    minute: Int,
    onSchedulingAlarmNotAllowed: () -> Unit
) {
    debugLog("setAlarm() called - hour: $hour, minute: $minute")

    if (exactAlarms.canScheduleExactAlarms().not()) {
        debugLog("scheduling not allowed")
        onSchedulingAlarmNotAllowed.invoke()
        return
    }

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

    exactAlarms.setExactAlarm(proposedTimeMillis)

    debugLog("Alarm Set")
}

@Preview(showBackground = true)
@Composable
fun StudyTabPreview() {
//    StudyTab(
//        ExactAlarms(
//            LocalContext.current,
//            LocalContext.current.getSharedPreferences("Preview", Context.MODE_PRIVATE)
//        )
//    )
}