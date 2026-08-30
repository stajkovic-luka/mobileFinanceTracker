package com.luka.mobilefinance.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

// Kreira i validira JWT tokene koji se koriste za autentifikaciju API zahteva
@Service
class JwtService(
    @Value("\${app.jwt.secret}") private val secret: String,
    @Value("\${app.jwt.expire-length}") private val expireLengthMs: Long
) {

    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    // Potpisani JWT sa username-om i vremenom isticanja iz konfiguracije
    fun generateToken(username: String): String {
        val now = Date()
        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(Date(now.time + expireLengthMs))
            .signWith(key)
            .compact()
    }

    // Parsira token i vraca username iz njega
    fun extractUsername(token: String): String? {
        return try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload.subject
        } catch (e: Exception) {
            null
        }
    }
}
