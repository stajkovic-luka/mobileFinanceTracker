package com.luka.mobilefinance.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequest(
    @field:NotBlank
    @field:Email
    @field:Size(max = 150)
    val email: String,

    @field:NotBlank
    @field:Size(min = 3, max = 50)
    val username: String,

    @field:NotBlank
    @field:Size(min = 6)
    val passwordPlain: String,

    @field:NotBlank
    @field:Size(max = 120)
    val nameSurname: String,
)
