package com.example.shoppinglistmanager.ui.options

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shoppinglistmanager.R
import com.example.shoppinglistmanager.ui.common.TopAppBar
import com.example.shoppinglistmanager.ui.theme.FontSizePreferences
import com.example.shoppinglistmanager.ui.theme.ShoppingListManagerTheme
import com.example.shoppinglistmanager.ui.viewmodel.OptionsViewModel

class OptionsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val optionsViewModel = OptionsViewModel(application)
            ShoppingListManagerTheme(optionsViewModel) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OptionsScreen(optionsViewModel)
                }
            }
        }
    }
}

@Composable
fun OptionsScreen(optionsViewModel: OptionsViewModel) {
    Scaffold(
        topBar = { TopAppBar("Options") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            ThemesInterface(optionsViewModel)
            FontSizeInterface(optionsViewModel)
        }
    }
}

@Composable
private fun ThemesInterface(optionsViewModel: OptionsViewModel) {
    var isThemeCollected by remember { mutableStateOf(false) }

    val darkThemeEnabledState: MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(optionsViewModel.fontSizeFlow) {
        isThemeCollected = true
    }

    if (isThemeCollected) {
        Column(
            modifier = Modifier.padding(start = 32.dp, top = 24.dp, end = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pick a theme:",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.2,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ThemeItemCard(
                    optionsViewModel = optionsViewModel,
                    darkThemeEnabledState = darkThemeEnabledState,
                    name = "Light theme",
                    isDark = false,
                    icon = painterResource(R.drawable.sun)
                )
                ThemeItemCard(
                    optionsViewModel = optionsViewModel,
                    darkThemeEnabledState = darkThemeEnabledState,
                    name = "Dark theme",
                    isDark = true,
                    icon = painterResource(R.drawable.moon)
                )
            }
        }
    }
}

@Composable
private fun ThemeItemCard(
    optionsViewModel: OptionsViewModel,
    darkThemeEnabledState: MutableState<Boolean>,
    name: String,
    isDark: Boolean,
    icon: Painter
) {
    Card(
        modifier = Modifier
            .clickable {
                darkThemeEnabledState.value = isDark
                optionsViewModel
                    .saveDarkThemeToPreferences(darkThemeEnabledState.value)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(name)
            Image(
                painter = icon,
                contentDescription = name,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}

@Composable
private fun FontSizeInterface(optionsViewModel: OptionsViewModel) {
    var isFontSizeCollected by remember { mutableStateOf(false) }

    val fontSize by optionsViewModel.fontSizeFlow.collectAsState("Medium")

    LaunchedEffect(optionsViewModel.fontSizeFlow) {
        isFontSizeCollected = true
    }

    if (isFontSizeCollected) {
        val fontSizeOptions: List<String> =
            FontSizePreferences.getFontSizeOptions()
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Pick font size: ",
                fontSize = MaterialTheme.typography.bodyLarge.fontSize * 1.2,
                fontWeight = FontWeight.Bold
            )
            fontSizeOptions.forEach { fontSizeOption ->
                Row(
                    Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .selectable(
                            selected = (fontSizeOption == fontSize),
                            onClick = {
                                optionsViewModel
                                    .saveFontSizeToPreferences(fontSizeOption)
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (fontSizeOption == fontSize),
                        onClick = {
                            optionsViewModel
                                .saveFontSizeToPreferences(fontSizeOption)
                        }
                    )
                    Text(
                        text = fontSizeOption,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}