package com.stajkovicluka.financeapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stajkovicluka.financeapp.data.api.ApiClient
import com.stajkovicluka.financeapp.data.repository.AuthRepository
import com.stajkovicluka.financeapp.util.TokenManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Drzi stanje prijave i registracije i povezuje auth ekrane sa Repository klasom.
class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AuthRepository(ApiClient.api)
    private val tokenManager = TokenManager(application.applicationContext)
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var nameSurname by mutableStateOf("")
    var email by mutableStateOf("")
    var registerUsername by mutableStateOf("")
    var registerPassword by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun clearError() {
        errorMessage = null
    }

    fun login(onSuccess: () -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            errorMessage = "Unesite username i sifru."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = repository.login(username, password)
                tokenManager.saveToken(response.token)
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 401) {
                    "Pogresan username ili sifra."
                } else {
                    "Prijava trenutno nije uspela."
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

    fun register(onSuccess: () -> Unit) {
        if (
            nameSurname.isBlank() || email.isBlank() ||
            registerUsername.isBlank() || registerPassword.isBlank()
        ) {
            errorMessage = "Popunite sva polja."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = repository.register(
                    nameSurname = nameSurname,
                    email = email,
                    username = registerUsername,
                    password = registerPassword
                )
                tokenManager.saveToken(response.token)
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 400) {
                    "Proverite unete podatke."
                } else {
                    "Registracija trenutno nije uspela."
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
