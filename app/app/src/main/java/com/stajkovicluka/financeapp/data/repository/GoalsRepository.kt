package com.stajkovicluka.financeapp.data.repository

import com.stajkovicluka.financeapp.data.api.FinanceApi
import com.stajkovicluka.financeapp.data.model.Goal
import com.stajkovicluka.financeapp.data.model.CreateGoalRequest
import com.stajkovicluka.financeapp.data.model.UpdateGoalRequest

// Poziva endpoint-e za citanje i izmenu ciljeva stednje.
class GoalsRepository(private val api: FinanceApi) {
    suspend fun getGoals(token: String): List<Goal> {
        return api.getGoals("Bearer $token")
    }

    suspend fun getGoal(token: String, goalId: Long): Goal {
        return api.getGoal("Bearer $token", goalId)
    }

    suspend fun createGoal(token: String, request: CreateGoalRequest): Goal {
        return api.createGoal("Bearer $token", request)
    }

    suspend fun updateGoal(token: String, goalId: Long, request: UpdateGoalRequest): Goal {
        return api.updateGoal("Bearer $token", goalId, request)
    }

    suspend fun deleteGoal(token: String, goalId: Long) {
        api.deleteGoal("Bearer $token", goalId)
    }
}
