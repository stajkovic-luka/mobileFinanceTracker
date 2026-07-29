package com.luka.mobilefinance.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class LoginRequest (
    @field:NotBlank
    @field:Size(min = 3, max = 50)
    val username: String,

    @field:NotBlank
    val password: String
)
