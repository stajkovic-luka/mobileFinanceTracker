package com.luka.mobilefinance.security

import com.luka.mobilefinance.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

// Presretne svaki request - javni endpoint prolazi, a za sve ostale zahteva validan JWT.
@Component
class JwtAuthFilter(private val jwtService: JwtService) : OncePerRequestFilter() {

    private val publicPaths = setOf("/register", "/login", "/health", "/error")

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val path = request.requestURI
        if (path in publicPaths) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.writer.write("""{"error":"Missing or invalid Authorization header"}""")
            return
        }

        val token = authHeader.substring(7)
        val username = jwtService.extractUsername(token)
        if (username == null) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            response.contentType = MediaType.APPLICATION_JSON_VALUE
            response.writer.write("""{"error":"Invalid or expired token"}""")
            return
        }

        // Expose autentifikovani username controllerima kroz request attribute
        request.setAttribute("username", username)
        filterChain.doFilter(request, response)
    }
}
