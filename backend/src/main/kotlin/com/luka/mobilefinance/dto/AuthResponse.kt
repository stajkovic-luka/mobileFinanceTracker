package com.luka.mobilefinance.dto

data class AuthResponse (
    val token: String,
    val username: String,
    val email: String
)