package com.example.shoppinglistmanager.ui.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.shoppinglistmanager.ui.main.MainActivity
import com.example.shoppinglistmanager.ui.options.OptionsActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(title: String) {
    val context: Context = LocalContext.current
    val activity: Activity = context as Activity

    TopAppBar(
        title = { Text(title) },
        colors = topAppBarColors(),
        navigationIcon = {
            if (title != "Home") {
                IconButton(onClick = {
                    activity.finish()
                }) {
                    Icon(Icons.Default.ArrowBack, "Return")
                }
            }
        },
        actions = {
            IconButton(onClick = {
                context.startActivity(
                    Intent(context, MainActivity::class.java)
                )
            }) {
                Icon(Icons.Filled.Home, "Home")
            }
            IconButton(onClick = {
                context.startActivity(
                    Intent(context, OptionsActivity::class.java)
                )
            }) {
                Icon(Icons.Filled.Settings, "Settings")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarAuthentication(title: String) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
        },
        colors = topAppBarColors()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun topAppBarColors(): TopAppBarColors {
    val containerColor: Color = MaterialTheme.colorScheme.primaryContainer
    val contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer

    return TopAppBarDefaults.topAppBarColors(
        containerColor = containerColor,
        titleContentColor = contentColor,
        navigationIconContentColor = contentColor,
        actionIconContentColor = contentColor
    )
}