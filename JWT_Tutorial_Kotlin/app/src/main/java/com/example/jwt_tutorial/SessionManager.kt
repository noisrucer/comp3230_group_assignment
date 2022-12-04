package com.example.jwt_tutorial

import android.content.Context
import android.content.SharedPreferences

/**
 * Session manager to save and fetch data from SharedPreferences
 */
class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val ACCESS_TOKEN = "accessToken"
        const val REFRESH_TOKEN = "refreshToken"
    }

    /**
     * Function to save auth token
     */
    fun saveAccessToken(access_token: String) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, access_token)
        editor.apply()
    }

    fun saveRefreshToken(refresh_token: String) {
        val editor = prefs.edit()
        editor.putString(REFRESH_TOKEN, refresh_token)
        editor.apply()
    }
    /**
     * Function to fetch auth token
     */
    fun fetchAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun fetchRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN, null)
    }

    /**
     * Function to logout if the refresh token has expired
     */
    fun logout() {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, null)
        editor.apply()
    }
}