package com.qwaykee.booku.data.preferences

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

// Note: Generated through ChatGPT

// Extension for accessing the DataStore instance
val Context.dataStore by preferencesDataStore(name = "app_preferences")

class PreferencesManager(private val context: Context) {

    // Generic function to save a preference
    suspend fun <T> savePreference(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // Generic function to read a preference
    fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return context.dataStore.data
            .map { preferences ->
                preferences[key] ?: defaultValue
            }
    }
}

// Define Preference Keys using a sealed class
@Suppress("ConvertObjectToDataObject", "unused")
sealed class PreferencesKeys<T>(val key: Preferences.Key<T>) {
    object DarkMode : PreferencesKeys<Boolean>(booleanPreferencesKey("dark_mode"))
    object InternetEnabled : PreferencesKeys<Boolean>(booleanPreferencesKey("internet_enabled"))
    object LanguageCode : PreferencesKeys<String>(stringPreferencesKey("language_code"))
    object LanguageDisplay : PreferencesKeys<String>(stringPreferencesKey("language_display"))
    object ScanHiddenFiles : PreferencesKeys<Boolean>(booleanPreferencesKey("scan_hidden_files"))
    object ScanBehavior : PreferencesKeys<String>(stringPreferencesKey("scan_behavior"))
}

// Helper function to remember preferences in a Composable
@Composable
fun <T> rememberPreference(
    context: Context,
    key: Preferences.Key<T>,
    defaultValue: T
): T {
    val flow = remember { PreferencesManager(context).getPreference(key, defaultValue) }
    val value by flow.collectAsState(initial = defaultValue)
    return value
}

// Helper function to save preferences
fun <T> savePreference(context: Context, key: Preferences.Key<T>, value: T) {
    CoroutineScope(Dispatchers.IO).launch {
        PreferencesManager(context).savePreference(key, value)
    }
}

// Usage in a Composable example
//@Composable
//fun SettingsScreen(context: Context) {
//    val isDarkMode = rememberPreference(context, PreferencesKeys.DarkMode.key, false)
//    val fontSize = rememberPreference(context, PreferencesKeys.FontSize.key, 14)
//
//    // Dark mode toggle
//    Switch(
//        checked = isDarkMode,
//        onCheckedChange = { newValue ->
//            savePreference(context, PreferencesKeys.DarkMode.key, newValue)
//        }
//    )
//
//    // Font size slider
//    Slider(
//        value = fontSize.toFloat(),
//        onValueChange = { newValue ->
//            savePreference(context, PreferencesKeys.FontSize.key, newValue.toInt())
//        },
//        valueRange = 12f..24f
//    )
//}
