package com.luka.mobilefinance.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
// Jednostavan endpoint za proveru da li je backend pokrenut
class HealthController {

    // Vraca status aplikacije - testni endpoint
    @GetMapping("/health")
    fun health(): Map<String, String> = mapOf("status" to "UP")
}
