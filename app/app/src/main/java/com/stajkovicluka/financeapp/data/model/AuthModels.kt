package com.stajkovicluka.financeapp.data.model

// Kotlin modeli za login, registraciju i JWT odgovor backend-a.
data class LoginRequest(
    val username: String,
    val password: String
)

data class RegisterRequest(
    val email: String,
    val username: String,
    val passwordPlain: String,
    val nameSurname: String
)

data class AuthResponse(
    val token: String,
    val username: String,
    val email: String,
    val name: String,
    val createdAt: String
)
