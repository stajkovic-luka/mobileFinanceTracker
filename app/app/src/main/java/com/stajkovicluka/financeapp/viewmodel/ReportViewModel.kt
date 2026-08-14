package com.stajkovicluka.financeapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stajkovicluka.financeapp.data.api.ApiClient
import com.stajkovicluka.financeapp.data.model.DepositReportResponse
import com.stajkovicluka.financeapp.data.repository.ReportRepository
import com.stajkovicluka.financeapp.util.TokenManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Drzi izabrani izvestaj i stanje njegovog ucitavanja.
class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ReportRepository(ApiClient.api)
    private val tokenManager = TokenManager(application.applicationContext)

    var report by mutableStateOf<DepositReportResponse?>(null)
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadReport(from: String, to: String) {
        if (from.isBlank() || to.isBlank()) {
            errorMessage = "Izaberite oba datuma."
            return
        }
        if (from > to) {
            errorMessage = "Pocetni datum ne moze biti posle krajnjeg datuma."
            return
        }

        val token = tokenManager.getToken()
        if (token == null) {
            errorMessage = "Nema aktivne prijave."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                report = repository.getDepositReport(token, from, to)
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 400) {
                    "Proverite izabrani period."
                } else {
                    "Izvestaj trenutno nije dostupan."
                }
            } catch (exception: IOException) {
                errorMessage = "Nije moguce povezati se sa backend-om."
            } catch (exception: Exception) {
                errorMessage = "Doslo je do neocekivane greske."
            } finally {
                isLoading = false
            }
        }
    }
}
