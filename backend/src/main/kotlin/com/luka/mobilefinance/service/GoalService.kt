package com.luka.mobilefinance.service

import com.luka.mobilefinance.dto.CreateGoalRequest
import com.luka.mobilefinance.dto.GoalResponse
import com.luka.mobilefinance.entity.Goal
import com.luka.mobilefinance.entity.Status
import com.luka.mobilefinance.repository.GoalRepo
import com.luka.mobilefinance.repository.UserRepo
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Date

@Service
// Sadrzi poslovnu logiku za kreiranje i pregled stednih ciljeva.
class GoalService(
    private val goalRepo: GoalRepo,
    private val userRepo: UserRepo
) {

    // Kreira cilj i automatski ga povezuje sa prijavljenim korisnikom.
    fun createGoal(username: String, request: CreateGoalRequest): GoalResponse {
        val user = userRepo.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists")

        val now = Date()
        val goal = Goal().apply {
            this.user = user
            name = request.name
            targetAmount = request.targetAmount
            currentAmount = BigDecimal.ZERO
            deadline = request.deadline
            status = Status.ACTIVE
            createdAt = now
            updatedAt = now
        }

        return toResponse(goalRepo.save(goal))
    }

    // Nalazi sve ciljeve prijavljenog korisnika, bez ciljeva drugih korisnika.
    fun getGoals(username: String): List<GoalResponse> {
        val user = userRepo.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists")

        return goalRepo.findAllByUserIdOrderByCreatedAtDesc(user.id!!).map(::toResponse)
    }

    // Pretvara entitet iz baze u odgovor koji saljemo mobilnoj aplikaciji.
    private fun toResponse(goal: Goal): GoalResponse {
        // Procenat je ogranicen na 100 i racuna se iz trenutnog i ciljanog iznosa.
        val progress = goal.currentAmount
            .multiply(BigDecimal(100))
            .divide(goal.targetAmount, 2, RoundingMode.HALF_UP)
            .min(BigDecimal(100))

        return GoalResponse(
            id = goal.id!!,
            name = goal.name,
            targetAmount = goal.targetAmount,
            currentAmount = goal.currentAmount,
            progressPct = progress,
            deadline = goal.deadline,
            status = goal.status
        )
    }
}
