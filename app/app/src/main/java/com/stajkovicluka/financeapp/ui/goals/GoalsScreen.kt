package com.stajkovicluka.financeapp.ui.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.data.model.Goal
import com.stajkovicluka.financeapp.ui.theme.Green
import com.stajkovicluka.financeapp.util.formatAmount
import com.stajkovicluka.financeapp.viewmodel.GoalsViewModel

// Prikazuje listu ciljeva prijavljenog korisnika
@Composable
fun GoalsScreen(
    goalsViewModel: GoalsViewModel,
    onGoalClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        goalsViewModel.loadGoals()
    }

    Surface(modifier = modifier.fillMaxSize()) {
        when {
            goalsViewModel.isLoading -> LoadingContent()
            goalsViewModel.errorMessage != null -> MessageContent(goalsViewModel.errorMessage!!)
            goalsViewModel.goals.isEmpty() -> EmptyGoalsContent()
            else -> GoalsList(goalsViewModel.goals, onGoalClick)
        }
    }
}

@Composable
private fun GoalsList(
    goals: List<Goal>,
    onGoalClick: (Long) -> Unit
) {
    var newestFirst by rememberSaveable { mutableStateOf(true) }
    val archivedGoals = goals.filter { it.status == "ARCHIVED" }
    val completedGoals = goals.filter { it.status == "COMPLETED" }
    val goalsToSort = goals.filter { it.status != "COMPLETED" && it.status != "ARCHIVED" }
    val goalsSortedByCreatedAt = if (newestFirst) {
        goalsToSort.sortedByDescending { it.createdAt }
    } else {
        goalsToSort.sortedBy { it.createdAt }
    }
    val sortedGoals = goalsSortedByCreatedAt.filter { it.deadline != null } +
        goalsSortedByCreatedAt.filter { it.deadline == null } +
        completedGoals +
        archivedGoals

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = stringResource(R.string.goals_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(
                    onClick = { newestFirst = !newestFirst },
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    val sortOrder = if (newestFirst) {
                        R.string.sort_newest_first
                    } else {
                        R.string.sort_oldest_first
                    }
                    Text(
                        text = "${stringResource(R.string.sort_goals_label)} " +
                            stringResource(sortOrder)
                    )
                }
            }
        }
        items(sortedGoals, key = { it.id }) { goal ->
            GoalCard(goal, onGoalClick)
        }
    }
}

@Composable
private fun EmptyGoalsContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ) {
            Text(text = stringResource(R.string.goals_empty), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun GoalCard(goal: Goal, onGoalClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onGoalClick(goal.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = goal.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(
                    R.string.goal_amount_progress,
                    formatAmount(goal.currentAmount),
                    formatAmount(goal.targetAmount)
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
            LinearProgressIndicator(
                progress = {
                    (goal.progressPct.toFloat() / 100f).coerceIn(0f, 1f)
                },
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.14f),
                gapSize = 0.dp,
                drawStopIndicator = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            GoalStatus(status = goal.status, modifier = Modifier.padding(top = 4.dp))
            val deadlineText = goal.deadline?.let { deadline ->
                stringResource(R.string.deadline_value, formatDate(deadline))
            } ?: stringResource(R.string.deadline_not_set)
            Text(
                text = deadlineText,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

private fun formatDate(date: String): String {
    val parts = date.substringBefore("T").split("-")
    return if (parts.size == 3) "${parts[2]}.${parts[1]}.${parts[0]}" else date
}

// Prikazuje status cilja sa bojom koja odgovara njegovom stanju
@Composable
fun GoalStatus(status: String, modifier: Modifier = Modifier) {
    val statusColor = when (status) {
        "ACTIVE" -> Green
        "COMPLETED" -> MaterialTheme.colorScheme.primary
        "ARCHIVED" -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        else -> MaterialTheme.colorScheme.onSurface
    }

    Text(
        text = status,
        color = statusColor,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MessageContent(message: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
    }
}
