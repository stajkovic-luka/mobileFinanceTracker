package com.stajkovicluka.financeapp.util

import android.content.Context

// Cuva i cita JWT token kako bi aplikacija znala da li je korisnik prijavljen
class TokenManager(context: Context) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        preferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun saveUserData(name: String, username: String, email: String, createdAt: String) {
        preferences.edit()
            .putString(USER_NAME_KEY, name)
            .putString(USERNAME_KEY, username)
            .putString(EMAIL_KEY, email)
            .putString(CREATED_AT_KEY, createdAt)
            .apply()
    }

    fun getToken(): String? {
        return preferences.getString(TOKEN_KEY, null)
    }

    fun getUserName(): String? {
        return preferences.getString(USER_NAME_KEY, null)
    }

    fun getUsername(): String? {
        return preferences.getString(USERNAME_KEY, null)
    }

    fun getEmail(): String? {
        return preferences.getString(EMAIL_KEY, null)
    }

    fun getCreatedAt(): String? {
        return preferences.getString(CREATED_AT_KEY, null)
    }

    fun clearToken() {
        preferences.edit()
            .remove(TOKEN_KEY)
            .remove(USER_NAME_KEY)
            .remove(USERNAME_KEY)
            .remove(EMAIL_KEY)
            .remove(CREATED_AT_KEY)
            .apply()
    }

    private companion object {
        const val PREFERENCES_NAME = "auth_preferences"
        const val TOKEN_KEY = "jwt_token"
        const val USER_NAME_KEY = "user_name"
        const val USERNAME_KEY = "username"
        const val EMAIL_KEY = "email"
        const val CREATED_AT_KEY = "created_at"
    }
}
