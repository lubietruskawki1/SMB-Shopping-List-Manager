package com.example.shoppinglistmanager.ui.authentication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shoppinglistmanager.ui.common.TopAppBarAuthentication
import com.example.shoppinglistmanager.ui.main.MainActivity
import com.example.shoppinglistmanager.ui.theme.ShoppingListManagerTheme
import com.example.shoppinglistmanager.ui.viewmodel.AuthenticationViewModel
import com.example.shoppinglistmanager.ui.viewmodel.OptionsViewModel

class RegistrationActivity : ComponentActivity() {
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
                    RegistrationScreen(authenticationViewModel)
                }
            }
        }
    }
}

@Composable
private fun RegistrationScreen(authenticationViewModel: AuthenticationViewModel) {
    val context: Context = LocalContext.current
    val activity: Activity = context as Activity

    Scaffold(
        topBar = { TopAppBarAuthentication("Registration") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp)
                    .clickable {
                        activity.finish()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Return",
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Back to Login",
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            ElevatedCard(
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(start = 24.dp, end = 24.dp,
                                 top = 24.dp, bottom = 32.dp)
                ) {
                    RegistrationInputs(authenticationViewModel)
                }
            }
        }
    }
}

@Composable
fun RegistrationInputs(authenticationViewModel: AuthenticationViewModel) {
    val context: Context = LocalContext.current

    val emailState: MutableState<String> = remember {
        mutableStateOf("")
    }
    val passwordState: MutableState<String> = remember {
        mutableStateOf("")
    }
    val confirmPasswordState: MutableState<String> = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Registration",
            style = TextStyle(fontSize = 32.sp)
        )

        EmailTextField(emailState)
        PasswordTextField(
            passwordState = passwordState,
            labelText = "Password",
            imeAction = ImeAction.Next
        )
        PasswordTextField(
            passwordState = confirmPasswordState,
            labelText = "Confirm password",
            imeAction = ImeAction.Done
        )

        Button(
            modifier = Modifier.padding(top = 24.dp),
            onClick = {
                authenticationViewModel.register(
                    email = emailState.value,
                    password = passwordState.value,
                    confirmPassword = confirmPasswordState.value,
                    onSuccess = {
                        context.startActivity(
                            Intent(context, MainActivity::class.java)
                        )
                    },
                    context = context
                )
            }
        ) {
            Text("Register")
        }
    }
}