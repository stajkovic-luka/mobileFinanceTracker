package com.stajkovicluka.financeapp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stajkovicluka.financeapp.ui.navigation.AppNavigation
import com.stajkovicluka.financeapp.viewmodel.AuthViewModel

// Predstavlja glavni Compose sadrzaj aplikacije nakon pokretanja.
@Composable
fun FinanceApp() {
    val authViewModel: AuthViewModel = viewModel()
    AppNavigation(authViewModel)
}
