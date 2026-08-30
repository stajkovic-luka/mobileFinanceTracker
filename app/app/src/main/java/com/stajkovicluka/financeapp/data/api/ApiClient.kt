package com.stajkovicluka.financeapp.data.api

import com.stajkovicluka.financeapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Retrofit klijent sa baznom adresom backend-a
object ApiClient {
    val api: FinanceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FinanceApi::class.java)
    }
}
