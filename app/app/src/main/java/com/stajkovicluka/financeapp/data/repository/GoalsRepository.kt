package com.stajkovicluka.financeapp.data.repository

import com.stajkovicluka.financeapp.data.api.FinanceApi
import com.stajkovicluka.financeapp.data.model.Goal

// Poziva endpoint-e za citanje i izmenu ciljeva stednje.
class GoalsRepository(private val api: FinanceApi) {
    suspend fun getGoals(token: String): List<Goal> {
        return api.getGoals("Bearer $token")
    }
}
