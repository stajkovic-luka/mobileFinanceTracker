package com.stajkovicluka.financeapp.data.repository

import com.stajkovicluka.financeapp.data.api.FinanceApi
import com.stajkovicluka.financeapp.data.model.Deposit

// Poziva endpoint-e za kreiranje, izmenu i brisanje uplata.
class DepositsRepository(private val api: FinanceApi) {
    suspend fun getDeposits(token: String, goalId: Long): List<Deposit> {
        return api.getDeposits("Bearer $token", goalId)
    }
}
