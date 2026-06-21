package com.luka.mobilefinance.dto

data class RegisterRequest(
    val email: String,
    val username: String,
    val passwordPlain: String,
    val nameSurname: String,
)
