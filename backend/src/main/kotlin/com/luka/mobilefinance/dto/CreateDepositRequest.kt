package com.luka.mobilefinance.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.Size
import java.math.BigDecimal

// Podaci koje klijent salje kada dodaje uplatu na stedni cilj
data class CreateDepositRequest(
    @field:DecimalMin(value = "0.01")
    @field:Digits(integer = 10, fraction = 2)
    val amount: BigDecimal,

    @field:Size(max = 255)
    val note: String? = null
)
