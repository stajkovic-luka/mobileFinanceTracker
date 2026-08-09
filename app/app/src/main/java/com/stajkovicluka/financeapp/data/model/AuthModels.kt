package com.stajkovicluka.financeapp.data.model

// Kotlin modeli za login, registraciju i JWT odgovor backend-a.
data class LoginRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val username: String,
    val email: String
)
