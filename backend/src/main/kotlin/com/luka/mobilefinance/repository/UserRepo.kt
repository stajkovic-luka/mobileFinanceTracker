package com.luka.mobilefinance.repository

import com.luka.mobilefinance.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
// Komunikacija sa users tabelom u bazi
interface UserRepo : JpaRepository<User, Long> {

    // Pronalazi korisnika u bazi prema prosledjenom username-u
    fun findByUsername(username: String) : User?


}
