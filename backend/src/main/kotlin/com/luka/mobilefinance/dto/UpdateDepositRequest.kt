package com.luka.mobilefinance.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.Size
import java.math.BigDecimal

// Podaci koje klijent salje kada menja jednu ili vise vrednosti postojece uplate
data class UpdateDepositRequest(
    @field:DecimalMin(value = "0.01")
    @field:Digits(integer = 10, fraction = 2)
    val amount: BigDecimal? = null,

    @field:Size(max = 255)
    val note: String? = null
)
