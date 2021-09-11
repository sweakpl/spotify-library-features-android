package com.sweak.spotifylibraryfeatures.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.sweak.spotifylibraryfeatures.BuildConfig

class Preferences(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_FILE_KEY, MODE_PRIVATE)

    fun putString(key: String, value: String) {
        preferences.edit().apply {
            putString(key, value)
            apply()
        }
    }

    fun putBoolean(key: String, value: Boolean) {
        preferences.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }

    fun putLong(key: String, value: Long) {
        preferences.edit().apply {
            putLong(key, value)
            apply()
        }
    }

    fun getString(key: String): String = preferences.getString(key, PREFERENCES_DEFAULT_STRING)!!

    fun getBoolean(key: String): Boolean = preferences.getBoolean(key, false)

    fun getLong(key: String): Long = preferences.getLong(key, 0)

    companion object {
        const val PREFERENCE_FILE_KEY = BuildConfig.APPLICATION_ID + ".SHARED_PREFERENCES_KEY"
        const val PREFERENCES_DEFAULT_STRING = "preferencesDefaultString"

        const val PREFERENCES_ACCESS_TOKEN_KEY = "accessToken"
        const val PREFERENCES_EXPIRY_DATE_KEY = "expiryDate"
        const val PREFERENCES_USER_REGISTERED_KEY = "userRegistered"
    }
}