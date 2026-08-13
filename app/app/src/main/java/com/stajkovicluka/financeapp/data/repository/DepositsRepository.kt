package com.stajkovicluka.financeapp.data.repository

import com.stajkovicluka.financeapp.data.api.FinanceApi
import com.stajkovicluka.financeapp.data.model.Deposit
import com.stajkovicluka.financeapp.data.model.CreateDepositRequest

// Poziva endpoint-e za kreiranje, izmenu i brisanje uplata.
class DepositsRepository(private val api: FinanceApi) {
    suspend fun getDeposits(token: String, goalId: Long): List<Deposit> {
        return api.getDeposits("Bearer $token", goalId)
    }

    suspend fun createDeposit(token: String, goalId: Long, request: CreateDepositRequest): Deposit {
        return api.createDeposit("Bearer $token", goalId, request)
    }
}
