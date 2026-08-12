package com.stajkovicluka.financeapp.data.api

import com.stajkovicluka.financeapp.data.model.AuthResponse
import com.stajkovicluka.financeapp.data.model.Goal
import com.stajkovicluka.financeapp.data.model.CreateGoalRequest
import com.stajkovicluka.financeapp.data.model.LoginRequest
import com.stajkovicluka.financeapp.data.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// Definise Retrofit pozive ka endpoint-ima Spring Boot backend-a.
interface FinanceApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("goals")
    suspend fun getGoals(@Header("Authorization") authorization: String): List<Goal>

    @POST("goals")
    suspend fun createGoal(
        @Header("Authorization") authorization: String,
        @Body request: CreateGoalRequest
    ): Goal
}
