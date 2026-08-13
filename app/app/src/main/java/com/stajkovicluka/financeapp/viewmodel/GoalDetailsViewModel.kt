package com.stajkovicluka.financeapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stajkovicluka.financeapp.data.api.ApiClient
import com.stajkovicluka.financeapp.data.model.Deposit
import com.stajkovicluka.financeapp.data.model.CreateDepositRequest
import com.stajkovicluka.financeapp.data.model.Goal
import com.stajkovicluka.financeapp.data.repository.DepositsRepository
import com.stajkovicluka.financeapp.data.repository.GoalsRepository
import com.stajkovicluka.financeapp.util.TokenManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.math.BigDecimal

// Drzi stanje detalja cilja i akcije nad njegovim uplatama.
class GoalDetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val goalsRepository = GoalsRepository(ApiClient.api)
    private val depositsRepository = DepositsRepository(ApiClient.api)
    private val tokenManager = TokenManager(application.applicationContext)

    var goal by mutableStateOf<Goal?>(null)
    var deposits by mutableStateOf<List<Deposit>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadDetails(goalId: Long) {
        val token = tokenManager.getToken()
        if (token == null) {
            errorMessage = "Nema aktivne prijave."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                goal = goalsRepository.getGoal(token, goalId)
                deposits = depositsRepository.getDeposits(token, goalId)
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 404) {
                    "Cilj nije pronadjen."
                } else if (exception.code() == 401) {
                    "Prijava je istekla."
                } else {
                    "Detalji cilja trenutno nisu dostupni."
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

    fun createDeposit(
        goalId: Long,
        amountText: String,
        note: String,
        onSuccess: () -> Unit
    ) {
        val amount = amountText.replace(',', '.').toBigDecimalOrNull()
        if (amount == null || amount <= BigDecimal.ZERO) {
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
                depositsRepository.createDeposit(
                    token = token,
                    goalId = goalId,
                    request = CreateDepositRequest(amount, note.trim().ifBlank { null })
                )
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 400) {
                    "Proverite unete podatke."
                } else {
                    "Uplatu trenutno nije moguce sacuvati."
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
