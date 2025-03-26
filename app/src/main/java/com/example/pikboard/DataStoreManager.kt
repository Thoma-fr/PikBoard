package com.example.pikboard

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object PreferencesKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
}

suspend fun saveDarkMode(context: Context, enabled: Boolean) {
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.DARK_MODE] = enabled
    }
}

fun readDarkMode(context: Context): Flow<Any> {
    return context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DARK_MODE] ?: false
        }
}