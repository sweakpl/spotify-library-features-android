package com.sweak.spotifylibraryfeatures.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStoreManager(val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("data_store")

    suspend fun putString(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun putLong(key: Preferences.Key<Long>, value: Long) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun putBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    fun getString(key: Preferences.Key<String>) =
        context.dataStore.data.map { preferences ->
            preferences[key] ?: PREFERENCES_DEFAULT_STRING
        }

    fun getLong(key: Preferences.Key<Long>) =
        context.dataStore.data.map { preferences ->
            preferences[key] ?: 0
        }

    fun getBoolean(key: Preferences.Key<Boolean>) =
        context.dataStore.data.map { preferences ->
            preferences[key] ?: false
        }

    companion object {
        const val PREFERENCES_DEFAULT_STRING = "preferencesDefaultString"
        val ACCESS_TOKEN = stringPreferencesKey("accessToken")
        val EXPIRY_DATE = longPreferencesKey("expiryDate")
        val USER_REGISTERED = booleanPreferencesKey("userRegistered")
    }
}