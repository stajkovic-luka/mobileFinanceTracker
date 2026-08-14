package com.luka.mobilefinance.dto

// Podaci koje backend vraca nakon uspesne registracije ili prijave.
data class AuthResponse (
    val token: String,
    val username: String,
    val email: String,
    val name: String
)
