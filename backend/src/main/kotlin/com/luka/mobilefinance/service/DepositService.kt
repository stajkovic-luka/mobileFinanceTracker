package com.luka.mobilefinance.service

import com.luka.mobilefinance.dto.CreateDepositRequest
import com.luka.mobilefinance.dto.DepositResponse
import com.luka.mobilefinance.entity.Deposit
import com.luka.mobilefinance.entity.Goal
import com.luka.mobilefinance.entity.Status
import com.luka.mobilefinance.repository.DepositRepo
import com.luka.mobilefinance.repository.GoalRepo
import com.luka.mobilefinance.repository.UserRepo
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.util.Date

@Service
// Sadrzi poslovnu logiku za dodavanje i pregled uplata.
class DepositService(
    private val depositRepo: DepositRepo,
    private val goalRepo: GoalRepo,
    private val userRepo: UserRepo
) {

    // Dodaje uplatu na cilj prijavljenog korisnika i azurira stanje cilja.
    @Transactional
    fun createDeposit(username: String, goalId: Long, request: CreateDepositRequest): DepositResponse {
        val goal = findGoalForUser(username, goalId)
        val now = Date()

        val deposit = Deposit().apply {
            this.goal = goal
            amount = request.amount
            note = request.note
            createdAt = now
            updatedAt = now
        }

        val savedDeposit = depositRepo.save(deposit)
        recalculateGoalAmount(goal)

        return toResponse(savedDeposit)
    }

    // Vraca sve uplate cilja samo ako cilj pripada prijavljenom korisniku.
    fun getDeposits(username: String, goalId: Long): List<DepositResponse> {
        val goal = findGoalForUser(username, goalId)

        return depositRepo.findAllByGoalIdOrderByCreatedAtAsc(goal.id!!).map(::toResponse)
    }

    // Nalazi cilj samo ako pripada korisniku iz JWT tokena.
    private fun findGoalForUser(username: String, goalId: Long): Goal {
        val user = userRepo.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists")

        return goalRepo.findByIdAndUserId(goalId, user.id!!)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Goal not found")
    }

    // Ponovo racuna zbir svih uplata da currentAmount uvek odgovara deposits tabeli.
    private fun recalculateGoalAmount(goal: Goal) {
        val deposits = depositRepo.findAllByGoalIdOrderByCreatedAtAsc(goal.id!!)

        goal.currentAmount = deposits.sumOf { it.amount }
        goal.status = if (goal.currentAmount >= goal.targetAmount) Status.COMPLETED else Status.ACTIVE
        goal.updatedAt = Date()

        goalRepo.save(goal)
    }

    // Pretvara entitet u odgovor koji saljemo mobilnoj aplikaciji.
    private fun toResponse(deposit: Deposit): DepositResponse {
        return DepositResponse(
            id = deposit.id!!,
            amount = deposit.amount,
            note = deposit.note,
            createdAt = deposit.createdAt!!
        )
    }
}
