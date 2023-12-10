package com.example.shoppinglistmanager.ui.authentication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglistmanager.R
import com.example.shoppinglistmanager.ui.common.TopAppBarAuthentication
import com.example.shoppinglistmanager.ui.main.MainActivity
import com.example.shoppinglistmanager.ui.theme.ShoppingListManagerTheme
import com.example.shoppinglistmanager.ui.viewmodel.AuthenticationViewModel
import com.example.shoppinglistmanager.ui.viewmodel.OptionsViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val optionsViewModel = OptionsViewModel(application)
            ShoppingListManagerTheme(optionsViewModel) {
                val authenticationViewModel = AuthenticationViewModel(application)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen(authenticationViewModel)
                }
            }
        }
    }
}

@Composable
private fun LoginScreen(authenticationViewModel: AuthenticationViewModel) {
    Scaffold(
        topBar = { TopAppBarAuthentication("Login") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 32.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 32.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Shopping List Manager",
                            style = TextStyle(fontSize = 24.sp)
                        )

                        Image(
                            painter = painterResource(R.drawable.shopping_list),
                            contentDescription = "Shopping List",
                            modifier = Modifier.size(128.dp)
                        )
                    }

                    Text(
                        text = "Login",
                        style = TextStyle(fontSize = 24.sp)
                    )

                    LoginInputs(authenticationViewModel)

                }
            }

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Don't have an account?",
                    style = TextStyle(fontSize = 18.sp)
                )
                Text(
                    text = "Register",
                    modifier = Modifier
                        .clickable {
                            // TODO
                        },
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun LoginInputs(authenticationViewModel: AuthenticationViewModel) {
    val context: Context = LocalContext.current

    val loginState: MutableState<String> = remember {
        mutableStateOf("")
    }
    val passwordState: MutableState<String> = remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        LoginTextField(loginState)
        PasswordTextField(passwordState)

        Button(
            modifier = Modifier.padding(top = 24.dp),
            onClick = {
                authenticationViewModel.login(
                    login = loginState.value,
                    password = passwordState.value,
                    onSuccess = {
                        context.startActivity(
                            Intent(context, MainActivity::class.java)
                        )
                    },
                    context = context
                )
            }
        ) {
            Text("Login")
        }
    }
}

@Composable
private fun LoginTextField(loginState: MutableState<String>) {
    OutlinedTextField(
        value = loginState.value,
        onValueChange = {
            loginState.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        label = { Text("Login") },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )
    )
}

@Composable
private fun PasswordTextField(passwordState: MutableState<String>) {
    var isPasswordVisible: Boolean by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        label = { Text("Password") },
        visualTransformation = when {
            isPasswordVisible -> VisualTransformation.None
            else -> PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
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