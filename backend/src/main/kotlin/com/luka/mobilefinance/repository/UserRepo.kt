package com.luka.mobilefinance.repository

import com.luka.mobilefinance.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepo : JpaRepository<User, Long> {

//  Pronalazi u bazi korisnika prema zadatom username
    fun findByUsername(username: String) : User?


}