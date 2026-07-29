package com.luka.mobilefinance.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate

// Podaci koje klijent salje kada kreira novi stedni cilj.
data class CreateGoalRequest(
    @field:NotBlank
    @field:Size(max = 150)
    val name: String,

    @field:DecimalMin(value = "0.01")
    @field:Digits(integer = 10, fraction = 2)
    val targetAmount: BigDecimal,

    // Rok je optional
    val deadline: LocalDate?
)
