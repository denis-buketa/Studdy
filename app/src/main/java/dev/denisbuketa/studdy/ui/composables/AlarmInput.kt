package dev.denisbuketa.studdy.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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

@Composable
fun TimeInput(
    inputState: String,
    onInputChanged: (String) -> Unit,
    placeHolderValue: String
) {
  TextField(
      modifier = Modifier.width(52.dp),
      value = inputState,
      onValueChange = {
        if (it.isEmpty() || (it.length <= 2 && it.matches("\\d+(\\.\\d+)?".toRegex()))) {
          onInputChanged.invoke(it)
        }
      },
      placeholder = { Text(text = placeHolderValue) },
      singleLine = true,
      keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
      colors = TextFieldDefaults.textFieldColors(
          backgroundColor = MaterialTheme.colors.background
      )
  )
}

@Composable
fun AlarmSetClearButtons(
    shouldShowClearButton: Boolean,
    onSetClicked: () -> Unit,
    onClearClicked: () -> Unit
) {
  Row {
    Button(
        modifier = Modifier.align(Alignment.CenterVertically),
        onClick = { onSetClicked.invoke() }) {
      Text("Set")
    }

    if (shouldShowClearButton) {
      Spacer(modifier = Modifier.width(8.dp))
      Button(
          modifier = Modifier.align(Alignment.CenterVertically),
          onClick = { onClearClicked.invoke() }) {
        Text("Clear")
      }
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

@Preview
@Composable
fun TimeInputPreview() {
  TimeInput("19", {}, "10")
}

@Preview(showBackground = true)
@Composable
fun AlarmSetClearButtonsPreview() {
  AlarmSetClearButtons(true, {}, {})
}