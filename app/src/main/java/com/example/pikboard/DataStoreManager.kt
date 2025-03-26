package com.example.pikboard

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")

object PreferencesKeys {
    val USER_SESSION_TOKEN = stringPreferencesKey("session_key")
}

suspend fun saveSessionToken(context: Context, token: String) {
    context.dataStore.edit { preferences ->
        preferences[PreferencesKeys.USER_SESSION_TOKEN] = token
    }
}

fun readSessionToken(context: Context): Flow<Any> {
    return context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.USER_SESSION_TOKEN] ?: ""
        }
}