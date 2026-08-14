package com.luka.mobilefinance.dto

import com.luka.mobilefinance.entity.Status
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Date

// Podaci o cilju koje backend vraca mobilnoj aplikaciji.
data class GoalResponse(
    val id: Long,
    val name: String,
    val targetAmount: BigDecimal,
    val currentAmount: BigDecimal,
    val progressPct: BigDecimal,
    val deadline: LocalDate?,
    val status: Status,
    val createdAt: Date
)
