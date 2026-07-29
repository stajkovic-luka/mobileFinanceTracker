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
class UserController(private val userService: UserService) {


    @PostMapping("/register")
    fun register(@Valid @RequestBody req: RegisterRequest): AuthResponse {
        return userService.register(req)

    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody req: LoginRequest): AuthResponse {
        return userService.login(req)
    }
}
