package com.luka.mobilefinance.exception

import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

// Centralizuje obradu HTTP gresaka
@RestControllerAdvice
class GlobalExceptionHandler {

    // Vraca 409 kad DB unique constraint bude prekrsen - npr dupli email ili username
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(ex: DataIntegrityViolationException): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(mapOf("error" to "Username or email already exists"))
    }
}
