package com.luka.mobilefinance.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class LoginRequest (
    @NotBlank
    @Size(max = 20)
    val username: String,

    @NotBlank
    val password: String
)