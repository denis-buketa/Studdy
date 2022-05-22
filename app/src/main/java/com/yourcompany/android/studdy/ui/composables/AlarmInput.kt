/*
 * Copyright (c) 2022 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yourcompany.android.studdy.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AlarmInput(
    hourInput: String,
    minuteInput: String,
    onHourInputChanged: (String) -> Unit,
    onMinuteInputChanged: (String) -> Unit,
    showInputInvalidMessage: Boolean,
    is24HourFormat: Boolean = false,
    isAm: Boolean = true,
    onIsAmEvent: (Boolean) -> Unit = {}
) {
  Column {
    Row {
      TimeInput(
          inputState = hourInput,
          onInputChanged = { onHourInputChanged.invoke(it) },
          placeHolderValue = "12"
      )
      Text(modifier = Modifier.align(alignment = Alignment.CenterVertically), text = ":")
      TimeInput(
          inputState = minuteInput,
          onInputChanged = { onMinuteInputChanged.invoke(it) },
          placeHolderValue = "00"
      )
      if (is24HourFormat.not()) {
        AmPmSelector(
            modifier = Modifier.align(Alignment.CenterVertically),
            isAm = isAm,
            onIsAmEvent = onIsAmEvent
        )
      }
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
    showInputInvalidMessage: Boolean,
    is24HourFormat: Boolean = false,
    isAm: Boolean = true,
    onIsAmEvent: (Boolean) -> Unit = {}
) {
  Column {
    Row {
      TimeInput(
          inputState = hourInput,
          onInputChanged = { onHourInputChanged.invoke(it) },
          placeHolderValue = "12"
      )
      Text(modifier = Modifier.align(alignment = Alignment.CenterVertically), text = ":")
      TimeInput(
          inputState = minuteInput,
          onInputChanged = { onMinuteInputChanged.invoke(it) },
          placeHolderValue = "00"
      )
      Text(modifier = Modifier.align(alignment = Alignment.CenterVertically), text = "+")
      TimeInput(
          inputState = intervalInput,
          onInputChanged = { onIntervalInputChanged.invoke(it) },
          placeHolderValue = "10"
      )
      if (is24HourFormat.not()) {
        AmPmSelector(
            modifier = Modifier.align(Alignment.CenterVertically),
            isAm = isAm,
            onIsAmEvent = onIsAmEvent
        )
      }
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
        onClick = { onSetClicked.invoke() },
        contentPadding = PaddingValues(0.dp)) {
      Text("Set")
    }

    if (shouldShowClearButton) {
      Spacer(modifier = Modifier.width(2.dp))
      Button(
          modifier = Modifier.align(Alignment.CenterVertically),
          onClick = { onClearClicked.invoke() },
          contentPadding = PaddingValues(0.dp)) {
        Text("Clear")
      }
    }
  }
}

@Composable
fun AmPmSelector(
    modifier: Modifier = Modifier,
    isAm: Boolean,
    onIsAmEvent: (Boolean) -> Unit
) {
  Row(modifier = modifier) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .width(40.dp)
            .height(40.dp)
            .clickable { onIsAmEvent.invoke(isAm.not()) }
    ) {
      Text(
          text = if (isAm) "AM" else "PM",
          color = Color.Gray,
          textAlign = TextAlign.Center,
          modifier = Modifier.fillMaxWidth().align(Alignment.Center),
      )
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
      true,
      is24HourFormat = true
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
  TimeInput("12", {}, "10")
}

@Preview(showBackground = true)
@Composable
fun AlarmSetClearButtonsPreview() {
  AlarmSetClearButtons(true, {}, {})
}

@Preview(showBackground = true)
@Composable
fun PreviewAmPmSelector() {
  AmPmSelector(
      isAm = true,
      onIsAmEvent = {}
  )
}