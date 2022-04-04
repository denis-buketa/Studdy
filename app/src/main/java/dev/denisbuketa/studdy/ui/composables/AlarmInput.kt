package dev.denisbuketa.studdy.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun AlarmInput(
    hourInput: String,
    minuteInput: String,
    onHourInputChanged: (String) -> Unit,
    onMinuteInputChanged: (String) -> Unit,
    showInputInvalidMessage: Boolean
) {
    Column {
        Row {
            TimeInput(
                inputState = hourInput,
                onInputChanged = { onHourInputChanged.invoke(it) },
                placeHolderValue = "19"
            )
            Spacer(Modifier.width(8.dp))
            Text(modifier = Modifier.align(alignment = Alignment.CenterVertically), text = ":")
            Spacer(Modifier.width(8.dp))
            TimeInput(
                inputState = minuteInput,
                onInputChanged = { onMinuteInputChanged.invoke(it) },
                placeHolderValue = "00"
            )
        }
        if (showInputInvalidMessage) {
            Text(text = "Enter a valid time")
        } else {
            Text(text = "")
        }
    }
}

@Composable
fun AlarmWithIntervalInput(
    hourInput: String,
    minuteInput: String,
    intervalInput: String,
    onHourInputChanged: (String) -> Unit,
    onMinuteInputChanged: (String) -> Unit,
    onIntervalInputChanged: (String) -> Unit,
    showInputInvalidMessage: Boolean
) {
    Column {
        Row {
            TimeInput(
                inputState = hourInput,
                onInputChanged = { onHourInputChanged.invoke(it) },
                placeHolderValue = "19"
            )
            Spacer(Modifier.width(8.dp))
            Text(modifier = Modifier.align(alignment = Alignment.CenterVertically), text = ":")
            Spacer(Modifier.width(8.dp))
            TimeInput(
                inputState = minuteInput,
                onInputChanged = { onMinuteInputChanged.invoke(it) },
                placeHolderValue = "00"
            )
            Spacer(Modifier.width(8.dp))
            Text(modifier = Modifier.align(alignment = Alignment.CenterVertically), text = "+")
            Spacer(Modifier.width(8.dp))
            TimeInput(
                inputState = intervalInput,
                onInputChanged = { onIntervalInputChanged.invoke(it) },
                placeHolderValue = "10"
            )
        }
        if (showInputInvalidMessage) {
            Text(text = "Enter a valid time")
        } else {
            Text(text = "")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlarmInputPreview() {
    AlarmInput(
        hourInput = "",
        minuteInput = "",
        onHourInputChanged = {},
        onMinuteInputChanged = {},
        true
    )
}

@Preview(showBackground = true)
@Composable
fun AlarmWindowInputPreview() {
    AlarmWithIntervalInput(
        hourInput = "",
        minuteInput = "",
        intervalInput = "",
        onHourInputChanged = {},
        onMinuteInputChanged = {},
        onIntervalInputChanged = {},
        true
    )
}