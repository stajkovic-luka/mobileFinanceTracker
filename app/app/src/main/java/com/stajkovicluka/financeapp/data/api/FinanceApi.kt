package com.stajkovicluka.financeapp.data.api

import com.stajkovicluka.financeapp.data.model.AuthResponse
import com.stajkovicluka.financeapp.data.model.Goal
import com.stajkovicluka.financeapp.data.model.CreateGoalRequest
import com.stajkovicluka.financeapp.data.model.UpdateGoalRequest
import com.stajkovicluka.financeapp.data.model.Deposit
import com.stajkovicluka.financeapp.data.model.CreateDepositRequest
import com.stajkovicluka.financeapp.data.model.UpdateDepositRequest
import com.stajkovicluka.financeapp.data.model.LoginRequest
import com.stajkovicluka.financeapp.data.model.RegisterRequest
import com.stajkovicluka.financeapp.data.model.DepositReportResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PATCH
import retrofit2.http.DELETE
import retrofit2.http.Query

// Retrofit pozivi ka endpointima na backendu
interface FinanceApi {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @GET("goals")
    suspend fun getGoals(@Header("Authorization") authorization: String): List<Goal>

    @GET("goals/{goalId}")
    suspend fun getGoal(
        @Header("Authorization") authorization: String,
        @retrofit2.http.Path("goalId") goalId: Long
    ): Goal

    @GET("goals/{goalId}/deposits")
    suspend fun getDeposits(
        @Header("Authorization") authorization: String,
        @retrofit2.http.Path("goalId") goalId: Long
    ): List<Deposit>

    @POST("goals/{goalId}/deposits")
    suspend fun createDeposit(
        @Header("Authorization") authorization: String,
        @retrofit2.http.Path("goalId") goalId: Long,
        @Body request: CreateDepositRequest
    ): Deposit

    @PATCH("goals/{goalId}/deposits/{depositId}")
    suspend fun updateDeposit(
        @Header("Authorization") authorization: String,
        @retrofit2.http.Path("goalId") goalId: Long,
        @retrofit2.http.Path("depositId") depositId: Long,
        @Body request: UpdateDepositRequest
    ): Deposit

    @DELETE("goals/{goalId}/deposits/{depositId}")
    suspend fun deleteDeposit(
        @Header("Authorization") authorization: String,
        @retrofit2.http.Path("goalId") goalId: Long,
        @retrofit2.http.Path("depositId") depositId: Long
    )

    @POST("goals")
    suspend fun createGoal(
        @Header("Authorization") authorization: String,
        @Body request: CreateGoalRequest
    ): Goal

    @PATCH("goals/{goalId}")
    suspend fun updateGoal(
        @Header("Authorization") authorization: String,
        @retrofit2.http.Path("goalId") goalId: Long,
        @Body request: UpdateGoalRequest
    ): Goal

    @DELETE("goals/{goalId}")
    suspend fun deleteGoal(
        @Header("Authorization") authorization: String,
        @retrofit2.http.Path("goalId") goalId: Long
    )

    @GET("reports/deposits")
    suspend fun getDepositReport(
        @Header("Authorization") authorization: String,
        @Query("from") from: String,
        @Query("to") to: String
    ): DepositReportResponse
}
