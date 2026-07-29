package com.luka.mobilefinance.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class RegisterRequest(
    @NotBlank
    @Email
    val email: String,

    @NotBlank
    val username: String,

    @NotBlank
    val passwordPlain: String,

    @NotBlank
    val nameSurname: String,
)
