package com.stajkovicluka.financeapp.data.repository

import com.stajkovicluka.financeapp.data.api.FinanceApi
import com.stajkovicluka.financeapp.data.model.AuthResponse
import com.stajkovicluka.financeapp.data.model.LoginRequest
import com.stajkovicluka.financeapp.data.model.RegisterRequest

// Poziva login i registraciju i vraca njihove rezultate ViewModel-u.
class AuthRepository(private val api: FinanceApi) {
    suspend fun login(username: String, password: String): AuthResponse {
        return api.login(LoginRequest(username, password))
    }

    suspend fun register(
        nameSurname: String,
        email: String,
        username: String,
        password: String
    ): AuthResponse {
        return api.register(RegisterRequest(email, username, password, nameSurname))
    }
}
