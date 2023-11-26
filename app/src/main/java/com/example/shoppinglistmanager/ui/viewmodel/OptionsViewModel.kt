package com.example.shoppinglistmanager.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val application: Application
) : AndroidViewModel(application) {
    companion object {
        // Singleton
        private val Context.dataStore by preferencesDataStore(
            name = "preferences"
        )
    }

    private val darkThemeKey = booleanPreferencesKey("darkTheme")
    private val fontSizeKey = stringPreferencesKey("fontSize")

    val darkThemeFlow: Flow<Boolean> = application.dataStore.data
        .map { preferences ->
            preferences[darkThemeKey] ?: false
        }

    val fontSizeFlow: Flow<String> = application.dataStore.data
        .map { preferences ->
            preferences[fontSizeKey] ?: "Medium"
        }

    fun saveDarkThemeToPreferences(darkThemeEnabled: Boolean) {
        viewModelScope.launch {
            application.dataStore.edit { preferences ->
                preferences[darkThemeKey] = darkThemeEnabled
            }
        }
    }

    fun saveFontSizeToPreferences(fontSize: String) {
        viewModelScope.launch {
            application.dataStore.edit { preferences ->
                preferences[fontSizeKey] = fontSize
            }
        }
    }
}