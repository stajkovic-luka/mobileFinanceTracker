package com.luka.mobilefinance.repository

import com.luka.mobilefinance.entity.Goal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
// Komunikacija sa GOALS u bazi
interface GoalRepo : JpaRepository<Goal, Long> {

    // Vraca ciljeve samo jednog korisnika, od najnovijeg ka najstarijem
    fun findAllByUserIdOrderByCreatedAtDesc(userId: Long): List<Goal>

    // Nalazi cilj samo ako pripada prosledjenom korisniku.
    fun findByIdAndUserId(id: Long, userId: Long): Goal?
}
