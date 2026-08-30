package com.stajkovicluka.financeapp.ui

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stajkovicluka.financeapp.ui.navigation.AppNavigation
import com.stajkovicluka.financeapp.ui.theme.ThemeMode
import com.stajkovicluka.financeapp.viewmodel.AuthViewModel

// Predstavlja glavni Compose sadrzaj aplikacije nakon pokretanja
@Composable
fun FinanceApp(
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val authViewModel: AuthViewModel = viewModel()
    AppNavigation(
        authViewModel = authViewModel,
        themeMode = themeMode,
        onThemeModeChanged = onThemeModeChanged,
        modifier = modifier.safeDrawingPadding()
    )
}
