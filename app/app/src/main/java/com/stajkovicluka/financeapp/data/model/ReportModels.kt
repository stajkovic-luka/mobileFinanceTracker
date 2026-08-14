package com.stajkovicluka.financeapp.data.model

import java.math.BigDecimal

// Sadrzi podatke koje backend vraca za izabrani period izvestaja.
data class DepositReportResponse(
    val from: String,
    val to: String,
    val totalDeposited: BigDecimal,
    val deposits: List<DepositReportItem>
)

data class DepositReportItem(
    val depositId: Long,
    val goalId: Long,
    val goalName: String,
    val amount: BigDecimal,
    val note: String?,
    val createdAt: String
)

// Predstavlja zbir uplata za jedan dan, potreban za prikaz trenda.
data class DailyDepositTotal(
    val date: String,
    val totalAmount: BigDecimal
)

// Predstavlja zbir uplata za jedan mesec, potreban za godisnji trend.
data class MonthlyDepositTotal(
    val month: String,
    val totalAmount: BigDecimal
)
