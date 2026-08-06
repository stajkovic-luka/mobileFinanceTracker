package com.luka.mobilefinance.repository

import com.luka.mobilefinance.entity.Deposit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.Date

@Repository
// Komunikacija sa deposits tabelom u bazi.
interface DepositRepo : JpaRepository<Deposit, Long> {

    // Vraca uplate jednog cilja od najstarije ka najnovijoj.
    fun findAllByGoalIdOrderByCreatedAtAsc(goalId: Long): List<Deposit>

    // Nalazi uplatu samo ako pripada prosledjenom cilju.
    fun findByIdAndGoalId(id: Long, goalId: Long): Deposit?

    // Vraca uplate svih ciljeva jednog korisnika unutar vremenskog perioda.
    @Query("""
        SELECT d
        FROM Deposit d
        JOIN d.goal g
        WHERE g.user.id = :userId
          AND d.createdAt >= :from
          AND d.createdAt < :to
        ORDER BY d.createdAt DESC
    """)
    fun findForUserInPeriod(
        @Param("userId") userId: Long,
        @Param("from") from: Date,
        @Param("to") to: Date
    ): List<Deposit>
}
