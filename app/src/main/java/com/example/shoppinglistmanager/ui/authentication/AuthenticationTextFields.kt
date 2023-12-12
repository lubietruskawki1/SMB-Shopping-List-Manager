package com.example.shoppinglistmanager.ui.authentication

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun EmailTextField(emailState: MutableState<String>) {
    OutlinedTextField(
        value = emailState.value,
        onValueChange = {
            emailState.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
fun PasswordTextField(
    passwordState: MutableState<String>,
    labelText: String,
    imeAction: ImeAction
) {
    var isPasswordVisible: Boolean by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        label = { Text(labelText) },
        visualTransformation = when {
            isPasswordVisible -> VisualTransformation.None
            else -> PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        trailingIcon = {
            val visibleIconAndText = Pair(
                first = Icons.Outlined.Visibility,
                second = "Password Visible"
            )

            val hiddenIconAndText = Pair(
                first = Icons.Outlined.VisibilityOff,
                second = "Password Hidden"
            )

            val passwordVisibilityIconAndText = when {
                isPasswordVisible -> visibleIconAndText
                else -> hiddenIconAndText
            }

            IconButton(onClick = {
                isPasswordVisible = !isPasswordVisible
            }) {
                Icon(
                    imageVector = passwordVisibilityIconAndText.first,
                    contentDescription = passwordVisibilityIconAndText.second
                )
            }
        }
    )
}