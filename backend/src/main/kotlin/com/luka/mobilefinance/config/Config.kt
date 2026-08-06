package com.luka.mobilefinance.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
// Konfiguracija zajednickih Spring bean-ova koje koriste servisi aplikacije.
class Config {
    // BCrypt enkoder koristi se za hashovanje i proveru korisnickih lozinki.
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}
