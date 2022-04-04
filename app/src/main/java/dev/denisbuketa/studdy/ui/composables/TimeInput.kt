package dev.denisbuketa.studdy.ui.composables

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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