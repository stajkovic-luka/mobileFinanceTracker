package com.stajkovicluka.financeapp.data.repository

import com.stajkovicluka.financeapp.data.api.FinanceApi
import com.stajkovicluka.financeapp.data.model.Deposit
import com.stajkovicluka.financeapp.data.model.CreateDepositRequest
import com.stajkovicluka.financeapp.data.model.UpdateDepositRequest

// Poziva endpoint-e za kreiranje, izmenu i brisanje uplata
class DepositsRepository(private val api: FinanceApi) {
    suspend fun getDeposits(token: String, goalId: Long): List<Deposit> {
        return api.getDeposits("Bearer $token", goalId)
    }

    suspend fun createDeposit(token: String, goalId: Long, request: CreateDepositRequest): Deposit {
        return api.createDeposit("Bearer $token", goalId, request)
    }

    suspend fun updateDeposit(
        token: String,
        goalId: Long,
        depositId: Long,
        request: UpdateDepositRequest
    ): Deposit {
        return api.updateDeposit("Bearer $token", goalId, depositId, request)
    }

    suspend fun deleteDeposit(token: String, goalId: Long, depositId: Long) {
        api.deleteDeposit("Bearer $token", goalId, depositId)
    }
}
