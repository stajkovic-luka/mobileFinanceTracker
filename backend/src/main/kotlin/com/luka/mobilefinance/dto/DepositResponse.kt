package com.luka.mobilefinance.dto

import java.math.BigDecimal
import java.util.Date

// Podaci o uplati koje backend vraca mobilnoj aplikaciji
data class DepositResponse(
    val id: Long,
    val amount: BigDecimal,
    val note: String?,
    val createdAt: Date
)
