package com.luka.mobilefinance.service

import com.luka.mobilefinance.dto.RegisterRequest
import com.luka.mobilefinance.entity.User
import com.luka.mobilefinance.repository.UserRepo
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Date

@Service
class UserService(private val repo: UserRepo, private val passwordEncoder: PasswordEncoder) {


    fun register(user: RegisterRequest): User {
        val hashedPW = passwordEncoder.encode(user.passwordPlain)

        val newUser = User().apply {
            email = user.email
            username = user.username
            passwordHash = hashedPW
            name = user.nameSurname
            createdAt = Date()
            updatedAt = Date()
        }

        return repo.save(newUser)
    }

}