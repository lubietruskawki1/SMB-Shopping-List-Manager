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
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
    val context: Context = LocalContext.current

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
                modifier = Modifier.padding(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
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
                            context.startActivity(
                                Intent(context, RegistrationActivity::class.java)
                            )
                        },
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
fun LoginInputs(authenticationViewModel: AuthenticationViewModel) {
    val context: Context = LocalContext.current

    val emailState: MutableState<String> = remember {
        mutableStateOf("")
    }
    val passwordState: MutableState<String> = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Login",
            style = TextStyle(fontSize = 32.sp)
        )

        EmailTextField(emailState)
        PasswordTextField(
            passwordState = passwordState,
            labelText = "Password",
            imeAction = ImeAction.Done
        )

        Button(
            modifier = Modifier.padding(top = 24.dp),
            onClick = {
                authenticationViewModel.login(
                    email = emailState.value,
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