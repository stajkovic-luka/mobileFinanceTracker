package com.stajkovicluka.financeapp.data.model

import java.math.BigDecimal

// Sadrzi Kotlin modele za podatke i zahteve vezane za uplate.
data class Deposit(
    val id: Long,
    val amount: BigDecimal,
    val note: String?,
    val createdAt: String
)

data class CreateDepositRequest(
    val amount: BigDecimal,
    val note: String?
)
