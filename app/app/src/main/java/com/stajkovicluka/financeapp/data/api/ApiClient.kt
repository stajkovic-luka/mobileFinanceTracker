package com.stajkovicluka.financeapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Retrofit klijent
object ApiClient {
    private const val BASE_URL = "http://192.168.0.3:8080/"

    val api: FinanceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FinanceApi::class.java)
    }
}
