package com.stajkovicluka.financeapp.ui.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.data.model.Goal
import com.stajkovicluka.financeapp.viewmodel.GoalsViewModel

// Prikazuje listu ciljeva prijavljenog korisnika.
@Composable
fun GoalsScreen(goalsViewModel: GoalsViewModel) {
    LaunchedEffect(Unit) {
        goalsViewModel.loadGoals()
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        when {
            goalsViewModel.isLoading -> LoadingContent()
            goalsViewModel.errorMessage != null -> MessageContent(goalsViewModel.errorMessage!!)
            goalsViewModel.goals.isEmpty() -> MessageContent(stringResource(R.string.goals_empty))
            else -> GoalsList(goalsViewModel.goals)
        }
    }
}

@Composable
private fun GoalsList(goals: List<Goal>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.goals_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        items(goals, key = { it.id }) { goal ->
            GoalCard(goal)
        }
    }
}

@Composable
private fun GoalCard(goal: Goal) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = goal.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${goal.currentAmount} / ${goal.targetAmount}",
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "${goal.progressPct}%",
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = goal.status,
                modifier = Modifier.padding(top = 4.dp)
            )
            goal.deadline?.let { deadline ->
                Text(
                    text = "Rok: $deadline",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
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
