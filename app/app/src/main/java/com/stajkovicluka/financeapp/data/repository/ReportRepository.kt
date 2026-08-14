package com.stajkovicluka.financeapp.data.repository

import com.stajkovicluka.financeapp.data.api.FinanceApi
import com.stajkovicluka.financeapp.data.model.DepositReportResponse

// Poziva endpoint koji vraca izvestaj uplata za izabrani period.
class ReportRepository(private val api: FinanceApi) {
    suspend fun getDepositReport(
        token: String,
        from: String,
        to: String
    ): DepositReportResponse {
        return api.getDepositReport("Bearer $token", from, to)
    }
}
