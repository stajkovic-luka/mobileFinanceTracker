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
    var userName by mutableStateOf(tokenManager.getUserName().orEmpty())
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var email by mutableStateOf("")
    var registerUsername by mutableStateOf("")
    var registerPassword by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun clearError() {
        errorMessage = null
    }

    fun logout() {
        tokenManager.clearToken()
        userName = ""
        username = ""
        password = ""
        errorMessage = null
    }

    fun login(onSuccess: () -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            errorMessage = "Unesite username i šifru."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = repository.login(username, password)
                tokenManager.saveToken(response.token)
                tokenManager.saveUserData(
                    response.name,
                    response.username,
                    response.email,
                    response.createdAt
                )
                userName = response.name
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 401) {
                    "Pogrešan username ili šifra."
                } else {
                    "Prijava trenutno nije uspela."
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

    fun register(onSuccess: () -> Unit) {
        val trimmedFirstName = firstName.trim()
        val trimmedLastName = lastName.trim()

        if (
            trimmedFirstName.isBlank() || trimmedLastName.isBlank() || email.isBlank() ||
            registerUsername.isBlank() || registerPassword.isBlank() || confirmPassword.isBlank()
        ) {
            errorMessage = "Popunite sva polja."
            return
        }

        if (!EMAIL_REGEX.matches(email)) {
            errorMessage = "Unesite ispravnu email adresu."
            return
        }

        if (registerPassword != confirmPassword) {
            errorMessage = "Šifre se ne poklapaju."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = repository.register(
                    nameSurname = "$trimmedFirstName $trimmedLastName",
                    email = email,
                    username = registerUsername,
                    password = registerPassword
                )
                tokenManager.saveToken(response.token)
                tokenManager.saveUserData(
                    response.name,
                    response.username,
                    response.email,
                    response.createdAt
                )
                userName = response.name
                onSuccess()
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 400) {
                    "Proverite unete podatke."
                } else {
                    "Registracija trenutno nije uspela."
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

    private companion object {
        val EMAIL_REGEX = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
    }
}
