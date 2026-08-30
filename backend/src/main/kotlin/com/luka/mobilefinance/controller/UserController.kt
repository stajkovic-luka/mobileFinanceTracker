package com.luka.mobilefinance.controller

import com.luka.mobilefinance.dto.AuthResponse
import com.luka.mobilefinance.dto.LoginRequest
import com.luka.mobilefinance.dto.RegisterRequest
import com.luka.mobilefinance.entity.User
import com.luka.mobilefinance.service.UserService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
// Prima zahteve za registraciju i prijavu korisnika
class UserController(private val userService: UserService) {

    // Registruje novog korisnika i vraca JWT token
    @PostMapping("/register")
    fun register(@Valid @RequestBody req: RegisterRequest): AuthResponse {
        return userService.register(req)

    }

    // Proverava username i lozinku, pa vraca JWT token ako su ispravni
    @PostMapping("/login")
    fun login(@Valid @RequestBody req: LoginRequest): AuthResponse {
        return userService.login(req)
    }
}
