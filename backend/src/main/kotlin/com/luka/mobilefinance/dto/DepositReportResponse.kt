package com.luka.mobilefinance.dto

import java.math.BigDecimal
import java.time.LocalDate

// Ceo izvestaj uplata prijavljenog korisnika za izabrani period.
data class DepositReportResponse(
    val from: LocalDate,
    val to: LocalDate,
    val totalDeposited: BigDecimal,
    val deposits: List<DepositReportItem>
)
