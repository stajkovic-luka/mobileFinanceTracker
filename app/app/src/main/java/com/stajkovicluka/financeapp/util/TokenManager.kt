package com.stajkovicluka.financeapp.util

import android.content.Context

// Cuva i cita JWT token kako bi aplikacija znala da li je korisnik prijavljen.
class TokenManager(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        preferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun saveUserName(name: String) {
        preferences.edit().putString(USER_NAME_KEY, name).apply()
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN_KEY, null)
    }

    fun getUserName(): String? {
        return preferences.getString(USER_NAME_KEY, null)
    }

    fun clearToken() {
        preferences.edit()
            .remove(TOKEN_KEY)
            .remove(USER_NAME_KEY)
            .apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "auth_preferences"
        const val TOKEN_KEY = "jwt_token"
        const val USER_NAME_KEY = "user_name"
    }
}
