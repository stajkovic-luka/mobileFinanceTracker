package com.stajkovicluka.financeapp.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.ui.auth.LoginScreen
import com.stajkovicluka.financeapp.viewmodel.AuthViewModel

// Definise rute i prelazak korisnika izmedju Compose ekrana.
private const val WELCOME_ROUTE = "welcome"
private const val LOGIN_ROUTE = "login"
private const val LOGGED_IN_ROUTE = "loggedIn"

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = WELCOME_ROUTE) {
        composable(WELCOME_ROUTE) {
            WelcomeScreen(onContinue = { navController.navigate(LOGIN_ROUTE) })
        }
        composable(LOGIN_ROUTE) {
            LoginScreen(
                authViewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onLoginSuccess = {
                    navController.navigate(LOGGED_IN_ROUTE) {
                        popUpTo(WELCOME_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(LOGGED_IN_ROUTE) {
            LoggedInScreen()
        }
    }
}

// Prikazuje pocetni ekran pre prelaska na prijavu.
@Composable
private fun WelcomeScreen(onContinue: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.welcome_title),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.welcome_subtitle),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Card(modifier = Modifier.padding(top = 32.dp)) {
                Text(
                    text = stringResource(R.string.welcome_description),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(24.dp)
                )
            }
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp)
            ) {
                Text(stringResource(R.string.welcome_continue))
            }
        }
    }
}

@Composable
private fun LoggedInScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.login_success),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
