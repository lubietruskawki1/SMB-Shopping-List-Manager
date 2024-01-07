package com.example.shoppinglistmanager.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit

@Composable
fun StringTextField(
    state: MutableState<String>,
    labelText: String
) {
    TextField(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        label = {
            Text(
                text = labelText,
                color = Color.Gray
            )
        }
    )
}

@Composable
fun EditTitleStringTextField(
    nameState: MutableState<String>
) {
    TextField(
        value = nameState.value,
        textStyle = TextStyle(
            fontSize = MaterialTheme.typography.bodyLarge.fontSize,
            fontWeight = FontWeight.Bold
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        onValueChange = {
            nameState.value = it
        }
    )
}

@Composable
fun EditStringTextField(
    state: MutableState<String>,
    fieldName: String,
    fontSize: TextUnit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$fieldName: ",
            fontSize = fontSize
        )
        TextField(
            value = state.value,
            textStyle = TextStyle(fontSize = fontSize),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onValueChange = {
                state.value = it
            }
        )
    }
}

@Composable
fun NumberTextField(
    state: MutableState<String>,
    labelText: String
) {
    TextField(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        label = {
            Text(
                text = labelText,
                color = Color.Gray
            )
        }
    )
}

@Composable
fun EditNumberTextField(
    state: MutableState<String>,
    fieldName: String,
    fontSize: TextUnit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$fieldName: ",
            fontSize = fontSize
        )
        TextField(
            value = state.value,
            textStyle = TextStyle(fontSize = fontSize),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                state.value = it
            }
        )
    }
}