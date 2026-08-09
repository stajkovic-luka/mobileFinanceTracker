package com.stajkovicluka.financeapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Retrofit klijent koji aplikacija koristi za komunikaciju sa backendom
object ApiClient {
    // Pristup laptopu preko telefona koristec WIFI
    private const val BASE_URL = "http://192.168.0.2:8080/"

    val api: FinanceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FinanceApi::class.java)
    }
}
