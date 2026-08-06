package com.luka.mobilefinance.repository

import com.luka.mobilefinance.entity.Deposit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
// Komunikacija sa deposits tabelom u bazi.
interface DepositRepo : JpaRepository<Deposit, Long> {

    // Vraca uplate jednog cilja od najstarije ka najnovijoj.
    fun findAllByGoalIdOrderByCreatedAtAsc(goalId: Long): List<Deposit>
}
