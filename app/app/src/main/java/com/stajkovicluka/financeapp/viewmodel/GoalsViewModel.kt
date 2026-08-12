package com.stajkovicluka.financeapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stajkovicluka.financeapp.data.api.ApiClient
import com.stajkovicluka.financeapp.data.model.Goal
import com.stajkovicluka.financeapp.data.model.CreateGoalRequest
import com.stajkovicluka.financeapp.data.repository.GoalsRepository
import com.stajkovicluka.financeapp.util.TokenManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.math.BigDecimal

// Drzi listu ciljeva i akcije nad ciljevima na ekranu liste.
class GoalsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = GoalsRepository(ApiClient.api)
    private val tokenManager = TokenManager(application.applicationContext)

    var goals by mutableStateOf<List<Goal>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadGoals() {
        val token = tokenManager.getToken()
        if (token == null) {
            errorMessage = "Nema aktivne prijave."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                goals = repository.getGoals(token)
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 401) {
                    "Prijava je istekla."
                } else {
                    "Ciljevi trenutno nisu dostupni."
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

    fun createGoal(name: String, targetAmountText: String, deadline: String?, onSuccess: () -> Unit) {
        val trimmedName = name.trim()
        val targetAmount = targetAmountText.replace(',', '.').toBigDecimalOrNull()

        if (trimmedName.isBlank()) {
            errorMessage = "Unesite naziv cilja."
            return
        }

        if (targetAmount == null || targetAmount <= BigDecimal.ZERO) {
            errorMessage = "Unesite iznos veci od nule."
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
                repository.createGoal(
                    token,
                    CreateGoalRequest(trimmedName, targetAmount, deadline)
                )
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 400) {
                    "Proverite unete podatke."
                } else {
                    "Cilj trenutno nije moguce sacuvati."
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
