package com.stajkovicluka.financeapp.data.api

import com.stajkovicluka.financeapp.data.model.AuthResponse
import com.stajkovicluka.financeapp.data.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

// Definise Retrofit pozive ka endpoint-ima Spring Boot backend-a.
interface FinanceApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse
}
