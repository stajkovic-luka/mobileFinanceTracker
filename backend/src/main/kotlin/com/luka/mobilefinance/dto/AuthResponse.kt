package com.luka.mobilefinance.dto

import java.util.Date

// Podaci koje backend vraca nakon uspesne registracije ili prijave.
data class AuthResponse (
    val token: String,
    val username: String,
    val email: String,
    val name: String,
    val createdAt: Date
)
