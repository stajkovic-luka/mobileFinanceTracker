package com.luka.mobilefinance.dto

import java.math.BigDecimal
import java.util.Date

// Jedna uplata prikazana unutar izvestaja za izabrani period.
data class DepositReportItem(
    val depositId: Long,
    val goalId: Long,
    val goalName: String,
    val amount: BigDecimal,
    val note: String?,
    val createdAt: Date
)
