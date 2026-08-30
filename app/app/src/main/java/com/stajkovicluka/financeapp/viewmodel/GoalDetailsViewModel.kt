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
import com.stajkovicluka.financeapp.data.model.UpdateDepositRequest
import com.stajkovicluka.financeapp.data.model.Goal
import com.stajkovicluka.financeapp.data.model.UpdateGoalRequest
import com.stajkovicluka.financeapp.data.repository.DepositsRepository
import com.stajkovicluka.financeapp.data.repository.GoalsRepository
import com.stajkovicluka.financeapp.util.TokenManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.math.BigDecimal

// Drzi stanje detalja cilja i akcije nad njegovim uplatama
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
                    "Cilj nije pronađen."
                } else if (exception.code() == 401) {
                    "Prijava je istekla."
                } else {
                    "Detalji cilja trenutno nisu dostupni."
                }
            } catch (exception: IOException) {
                errorMessage = "Nije moguće povezati se sa backend-om."
            } catch (exception: Exception) {
                errorMessage = "Došlo je do neočekivane greške."
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
            errorMessage = "Unesite iznos veći od nule."
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
                    "Uplatu trenutno nije moguće sačuvati."
                }
            } catch (exception: IOException) {
                errorMessage = "Nije moguće povezati se sa backend-om."
            } catch (exception: Exception) {
                errorMessage = "Došlo je do neočekivane greške."
            } finally {
                isLoading = false
            }
        }
    }

    fun updateDeposit(
        goalId: Long,
        depositId: Long,
        amountText: String,
        note: String,
        onSuccess: () -> Unit
    ) {
        val amount = amountText.replace(',', '.').toBigDecimalOrNull()
        if (amount == null || amount <= BigDecimal.ZERO) {
            errorMessage = "Unesite iznos veći od nule."
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
                depositsRepository.updateDeposit(
                    token = token,
                    goalId = goalId,
                    depositId = depositId,
                    request = UpdateDepositRequest(amount, note.trim())
                )
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 400) {
                    "Proverite unete podatke."
                } else {
                    "Uplatu trenutno nije moguće izmeniti."
                }
            } catch (exception: IOException) {
                errorMessage = "Nije moguće povezati se sa backend-om."
            } catch (exception: Exception) {
                errorMessage = "Došlo je do neočekivane greške."
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteDeposit(goalId: Long, depositId: Long, onSuccess: () -> Unit) {
        val token = tokenManager.getToken()
        if (token == null) {
            errorMessage = "Nema aktivne prijave."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                depositsRepository.deleteDeposit(token, goalId, depositId)
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = "Uplatu trenutno nije moguće obrisati."
            } catch (exception: IOException) {
                errorMessage = "Nije moguće povezati se sa backend-om."
            } catch (exception: Exception) {
                errorMessage = "Došlo je do neočekivane greške."
            } finally {
                isLoading = false
            }
        }
    }

    fun updateGoal(
        goalId: Long,
        name: String,
        targetAmountText: String,
        deadline: String?,
        onSuccess: () -> Unit
    ) {
        val targetAmount = targetAmountText.replace(',', '.').toBigDecimalOrNull()
        if (name.trim().isBlank()) {
            errorMessage = "Unesite naziv cilja."
            return
        }
        if (targetAmount == null || targetAmount <= BigDecimal.ZERO) {
            errorMessage = "Unesite ciljani iznos veći od nule."
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
                goalsRepository.updateGoal(
                    token = token,
                    goalId = goalId,
                    request = UpdateGoalRequest(name.trim(), targetAmount, deadline)
                )
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 400) {
                    "Proverite unete podatke."
                } else {
                    "Cilj trenutno nije moguće izmeniti."
                }
            } catch (exception: IOException) {
                errorMessage = "Nije moguće povezati se sa backend-om."
            } catch (exception: Exception) {
                errorMessage = "Došlo je do neočekivane greške."
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteGoal(goalId: Long, onSuccess: () -> Unit) {
        val token = tokenManager.getToken()
        if (token == null) {
            errorMessage = "Nema aktivne prijave."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                goalsRepository.deleteGoal(token, goalId)
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = "Cilj trenutno nije moguće obrisati."
            } catch (exception: IOException) {
                errorMessage = "Nije moguće povezati se sa backend-om."
            } catch (exception: Exception) {
                errorMessage = "Došlo je do neočekivane greške."
            } finally {
                isLoading = false
            }
        }
    }

    fun archiveGoal(goalId: Long, onSuccess: () -> Unit) {
        val token = tokenManager.getToken()
        if (token == null) {
            errorMessage = "Nema aktivne prijave."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                goalsRepository.archiveGoal(token, goalId)
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 404) {
                    "Arhiviranje nije dostupno na pokrenutom backend-u."
                } else {
                    "Cilj trenutno nije moguće arhivirati."
                }
            } catch (exception: IOException) {
                errorMessage = "Nije moguće povezati se sa backend-om."
            } catch (exception: Exception) {
                errorMessage = "Došlo je do neočekivane greške."
            } finally {
                isLoading = false
            }
        }
    }

    fun unarchiveGoal(goalId: Long, onSuccess: () -> Unit) {
        val token = tokenManager.getToken()
        if (token == null) {
            errorMessage = "Nema aktivne prijave."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                goalsRepository.unarchiveGoal(token, goalId)
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = "Cilj trenutno nije moguće vratiti iz arhive."
            } catch (exception: IOException) {
                errorMessage = "Nije moguće povezati se sa backend-om."
            } catch (exception: Exception) {
                errorMessage = "Došlo je do neočekivane greške."
            } finally {
                isLoading = false
            }
        }
    }
}
