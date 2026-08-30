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
import com.stajkovicluka.financeapp.ui.deposits.DepositFormScreen
import com.stajkovicluka.financeapp.ui.home.HomeScreen
import com.stajkovicluka.financeapp.ui.reports.ReportsScreen
import com.stajkovicluka.financeapp.ui.theme.ThemeMode
import com.stajkovicluka.financeapp.viewmodel.AuthViewModel
import com.stajkovicluka.financeapp.viewmodel.GoalsViewModel
import com.stajkovicluka.financeapp.viewmodel.GoalDetailsViewModel
import com.stajkovicluka.financeapp.viewmodel.ReportViewModel

// Definise rute i prelazak korisnika izmedju Compose ekrana
private const val WELCOME_ROUTE = "welcome"
private const val LOGIN_ROUTE = "login"
private const val REGISTER_ROUTE = "register"
private const val HOME_ROUTE = "home"
private const val GOALS_ROUTE = "goals"
private const val REPORTS_ROUTE = "reports"
private const val CREATE_GOAL_ROUTE = "createGoal"
private const val GOAL_DETAILS_ROUTE = "goalDetails/{goalId}"
private const val EDIT_GOAL_ROUTE = "editGoal/{goalId}"
private const val CREATE_DEPOSIT_ROUTE = "createDeposit/{goalId}"
private const val EDIT_DEPOSIT_ROUTE = "editDeposit/{goalId}/{depositId}"

@Composable
fun AppNavigation(
    authViewModel: AuthViewModel,
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier
) {
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
                    navController.navigate(HOME_ROUTE) {
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
                    navController.navigate(HOME_ROUTE) {
                        popUpTo(WELCOME_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(HOME_ROUTE) {
            AppShell(
                selectedDestination = AppDestination.HOME,
                themeMode = themeMode,
                onThemeModeChanged = onThemeModeChanged,
                onHomeClick = { },
                onGoalsClick = { navController.navigate(GOALS_ROUTE) },
                onReportsClick = { navController.navigate(REPORTS_ROUTE) },
                onCreateGoal = { navController.navigate(CREATE_GOAL_ROUTE) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) { paddingValues ->
                HomeScreen(
                    userName = authViewModel.userName,
                    onShowGoals = { navController.navigate(GOALS_ROUTE) },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
        composable(GOALS_ROUTE) {
            val goalsViewModel: GoalsViewModel = viewModel()
            AppShell(
                selectedDestination = AppDestination.GOALS,
                themeMode = themeMode,
                onThemeModeChanged = onThemeModeChanged,
                onHomeClick = { navController.navigate(HOME_ROUTE) },
                onGoalsClick = { },
                onReportsClick = { navController.navigate(REPORTS_ROUTE) },
                onCreateGoal = { navController.navigate(CREATE_GOAL_ROUTE) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) { paddingValues ->
                GoalsScreen(
                    goalsViewModel = goalsViewModel,
                    onGoalClick = { goalId -> navController.navigate("goalDetails/$goalId") },
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
        composable(REPORTS_ROUTE) {
            val reportViewModel: ReportViewModel = viewModel()
            AppShell(
                selectedDestination = AppDestination.REPORTS,
                themeMode = themeMode,
                onThemeModeChanged = onThemeModeChanged,
                onHomeClick = { navController.navigate(HOME_ROUTE) },
                onGoalsClick = { navController.navigate(GOALS_ROUTE) },
                onReportsClick = { },
                onCreateGoal = { navController.navigate(CREATE_GOAL_ROUTE) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            ) { paddingValues ->
                ReportsScreen(
                    reportViewModel = reportViewModel,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
        composable(CREATE_GOAL_ROUTE) {
            val goalsViewModel: GoalsViewModel = viewModel()
            GoalFormScreen(
                onBack = { navController.popBackStack() },
                onSave = { name, targetAmount, deadline, onSuccess ->
                    goalsViewModel.createGoal(name, targetAmount, deadline, onSuccess)
                },
                isLoading = goalsViewModel.isLoading,
                errorMessage = goalsViewModel.errorMessage,
                onSaveSuccess = {
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
                onBack = { navController.popBackStack() },
                onEditGoal = { navController.navigate("editGoal/$goalId") },
                onGoalDeleted = {
                    navController.navigate(GOALS_ROUTE) {
                        popUpTo(GOALS_ROUTE) { inclusive = true }
                    }
                },
                onGoalArchived = {
                    navController.navigate(GOALS_ROUTE) {
                        popUpTo(GOALS_ROUTE) { inclusive = true }
                    }
                },
                onGoalUnarchived = {
                    navController.navigate(GOALS_ROUTE) {
                        popUpTo(GOALS_ROUTE) { inclusive = true }
                    }
                },
                onCreateDeposit = { navController.navigate("createDeposit/$goalId") },
                onEditDeposit = { depositId -> navController.navigate("editDeposit/$goalId/$depositId") },
                onDepositChanged = {
                    navController.navigate("goalDetails/$goalId") {
                        popUpTo(GOAL_DETAILS_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = EDIT_GOAL_ROUTE,
            arguments = listOf(navArgument("goalId") { type = NavType.LongType })
        ) { backStackEntry ->
            val goalId = backStackEntry.arguments?.getLong("goalId") ?: return@composable
            val goalDetailsViewModel: GoalDetailsViewModel = viewModel()

            androidx.compose.runtime.LaunchedEffect(goalId) {
                goalDetailsViewModel.loadDetails(goalId)
            }

            val goal = goalDetailsViewModel.goal
            if (goalDetailsViewModel.isLoading) {
                androidx.compose.material3.CircularProgressIndicator()
            } else if (goal != null) {
                GoalFormScreen(
                    onBack = { navController.popBackStack() },
                    onSave = { name, targetAmount, deadline, onSuccess ->
                        goalDetailsViewModel.updateGoal(
                            goalId,
                            name,
                            targetAmount,
                            deadline,
                            onSuccess
                        )
                    },
                    onSaveSuccess = {
                        navController.navigate("goalDetails/$goalId") {
                            popUpTo(GOAL_DETAILS_ROUTE) { inclusive = true }
                        }
                    },
                    isLoading = goalDetailsViewModel.isLoading,
                    errorMessage = goalDetailsViewModel.errorMessage,
                    goalToEdit = goal
                )
            }
        }
        composable(
            route = CREATE_DEPOSIT_ROUTE,
            arguments = listOf(navArgument("goalId") { type = NavType.LongType })
        ) { backStackEntry ->
            val goalId = backStackEntry.arguments?.getLong("goalId") ?: return@composable
            val goalDetailsViewModel: GoalDetailsViewModel = viewModel()
            DepositFormScreen(
                goalId = goalId,
                goalDetailsViewModel = goalDetailsViewModel,
                onBack = { navController.popBackStack() },
                onSaveSuccess = {
                    navController.navigate("goalDetails/$goalId") {
                        popUpTo(GOAL_DETAILS_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = EDIT_DEPOSIT_ROUTE,
            arguments = listOf(
                navArgument("goalId") { type = NavType.LongType },
                navArgument("depositId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val goalId = backStackEntry.arguments?.getLong("goalId") ?: return@composable
            val depositId = backStackEntry.arguments?.getLong("depositId") ?: return@composable
            val goalDetailsViewModel: GoalDetailsViewModel = viewModel()

            androidx.compose.runtime.LaunchedEffect(goalId) {
                goalDetailsViewModel.loadDetails(goalId)
            }

            val deposit = goalDetailsViewModel.deposits.find { it.id == depositId }
            if (goalDetailsViewModel.isLoading) {
                androidx.compose.material3.CircularProgressIndicator()
            } else if (deposit != null) {
                DepositFormScreen(
                    goalId = goalId,
                    goalDetailsViewModel = goalDetailsViewModel,
                    onBack = { navController.popBackStack() },
                    onSaveSuccess = {
                        navController.navigate("goalDetails/$goalId") {
                            popUpTo(GOAL_DETAILS_ROUTE) { inclusive = true }
                        }
                    },
                    depositToEdit = deposit
                )
            }
        }
    }
}

// Prikazuje pocetni ekran pre prelaska na prijavu
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
