package com.luka.mobilefinance.controller

import com.luka.mobilefinance.dto.RegisterRequest
import com.luka.mobilefinance.entity.User
import com.luka.mobilefinance.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userService: UserService) {

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest): User {
//        TODO: Nemoj da vracas ceo objekat
        return userService.register(req)

    }
}
