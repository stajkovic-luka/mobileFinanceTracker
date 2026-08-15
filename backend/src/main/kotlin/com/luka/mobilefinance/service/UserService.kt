package com.luka.mobilefinance.service

import com.luka.mobilefinance.dto.AuthResponse
import com.luka.mobilefinance.dto.LoginRequest
import com.luka.mobilefinance.dto.RegisterRequest
import com.luka.mobilefinance.entity.User
import com.luka.mobilefinance.repository.UserRepo
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.Date

// Obradjuje registraciju i login korisnika - hesovanje lozinke, provera kredencijala i izdavanje JWT-a
@Service
class UserService(private val repo: UserRepo, private val passwordEncoder: PasswordEncoder, private val jwtService: JwtService) {


    // Kreira novog usera (lozinka hesovana) i vraca JWT da klijent odmah bude ulogovan
    fun register(user: RegisterRequest): AuthResponse {
        val hashedPW = passwordEncoder.encode(user.passwordPlain)

        val newUser = User().apply {
            email = user.email
            username = user.username
            passwordHash = hashedPW
            name = user.nameSurname
            createdAt = Date()
            updatedAt = Date()
        }

        val savedUser : User = repo.save(newUser)

        val token = jwtService.generateToken(savedUser.username!!)

        return AuthResponse(
            token,
            savedUser.username!!,
            savedUser.email!!,
            savedUser.name!!,
            savedUser.createdAt!!
        )
    }

    // Proverava kredencijale i vraca JWT - pada sa 401 pri neslaganju
    fun login(req: LoginRequest): AuthResponse {
        val user = repo.findByUsername(req.username)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password")

        if(!passwordEncoder.matches(req.password, user.passwordHash))
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password")

        val token = jwtService.generateToken(user.username!!)

        return AuthResponse(token, user.username!!, user.email!!, user.name!!, user.createdAt!!)

    }

}
