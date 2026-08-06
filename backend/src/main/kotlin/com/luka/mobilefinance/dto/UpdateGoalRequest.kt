package com.luka.mobilefinance.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate

// Podaci koje klijent salje kada menja postojeci stedni cilj
data class UpdateGoalRequest(
    @field:Size(min = 1, max = 150)
    @field:Pattern(regexp = ".*\\S.*", message = "Name must not be blank")
    val name: String? = null,

    @field:DecimalMin(value = "0.01")
    @field:Digits(integer = 10, fraction = 2)
    val targetAmount: BigDecimal? = null,

    val deadline: LocalDate? = null
)
