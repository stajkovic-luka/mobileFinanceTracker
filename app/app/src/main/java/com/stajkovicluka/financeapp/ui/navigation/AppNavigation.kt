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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.ui.auth.LoginScreen
import com.stajkovicluka.financeapp.ui.auth.RegisterScreen
import com.stajkovicluka.financeapp.ui.goals.GoalsScreen
import com.stajkovicluka.financeapp.ui.goals.GoalFormScreen
import com.stajkovicluka.financeapp.ui.goals.GoalDetailsScreen
import com.stajkovicluka.financeapp.viewmodel.AuthViewModel
import com.stajkovicluka.financeapp.viewmodel.GoalsViewModel
import com.stajkovicluka.financeapp.viewmodel.GoalDetailsViewModel

// Definise rute i prelazak korisnika izmedju Compose ekrana.
private const val WELCOME_ROUTE = "welcome"
private const val LOGIN_ROUTE = "login"
private const val REGISTER_ROUTE = "register"
private const val GOALS_ROUTE = "goals"
private const val CREATE_GOAL_ROUTE = "createGoal"
private const val GOAL_DETAILS_ROUTE = "goalDetails/{goalId}"

@Composable
fun AppNavigation(authViewModel: AuthViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WELCOME_ROUTE,
        modifier = modifier
    ) {
        composable(WELCOME_ROUTE) {
            WelcomeScreen(onContinue = { navController.navigate(LOGIN_ROUTE) })
        }
        composable(LOGIN_ROUTE) {
            LoginScreen(
                authViewModel = authViewModel,
                onBack = {
                    authViewModel.clearError()
                    navController.popBackStack()
                },
                onRegister = {
                    authViewModel.clearError()
                    navController.navigate(REGISTER_ROUTE)
                },
                onLoginSuccess = {
                    navController.navigate(GOALS_ROUTE) {
                        popUpTo(WELCOME_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(REGISTER_ROUTE) {
            RegisterScreen(
                authViewModel = authViewModel,
                onBack = {
                    authViewModel.clearError()
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(GOALS_ROUTE) {
                        popUpTo(WELCOME_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(GOALS_ROUTE) {
            val goalsViewModel: GoalsViewModel = viewModel()
            GoalsScreen(
                goalsViewModel = goalsViewModel,
                onCreateGoal = { navController.navigate(CREATE_GOAL_ROUTE) },
                onGoalClick = { goalId -> navController.navigate("goalDetails/$goalId") }
            )
        }
        composable(CREATE_GOAL_ROUTE) {
            val goalsViewModel: GoalsViewModel = viewModel()
            GoalFormScreen(
                goalsViewModel = goalsViewModel,
                onBack = { navController.popBackStack() },
                onCreateSuccess = {
                    navController.navigate(GOALS_ROUTE) {
                        popUpTo(GOALS_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = GOAL_DETAILS_ROUTE,
            arguments = listOf(navArgument("goalId") { type = NavType.LongType })
        ) { backStackEntry ->
            val goalId = backStackEntry.arguments?.getLong("goalId") ?: return@composable
            val goalDetailsViewModel: GoalDetailsViewModel = viewModel()
            GoalDetailsScreen(
                goalId = goalId,
                goalDetailsViewModel = goalDetailsViewModel,
                onBack = { navController.popBackStack() }
            )
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
