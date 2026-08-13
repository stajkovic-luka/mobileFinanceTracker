package com.stajkovicluka.financeapp.data.model

import java.math.BigDecimal

// Sadrzi Kotlin modele za podatke i zahteve vezane za ciljeve stednje.
data class Goal(
    val id: Long,
    val name: String,
    val targetAmount: BigDecimal,
    val currentAmount: BigDecimal,
    val progressPct: BigDecimal,
    val deadline: String?,
    val status: String
)

data class CreateGoalRequest(
    val name: String,
    val targetAmount: BigDecimal,
    val deadline: String?
)

data class UpdateGoalRequest(
    val name: String,
    val targetAmount: BigDecimal,
    val deadline: String?
)
