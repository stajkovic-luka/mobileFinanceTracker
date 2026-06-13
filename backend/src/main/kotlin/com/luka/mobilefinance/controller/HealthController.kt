package com.luka.mobilefinance.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

//    TEST
    @GetMapping("/health")
    fun health(): Map<String, String> = mapOf("status" to "UP")
}