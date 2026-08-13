package com.stajkovicluka.financeapp.ui.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.data.model.Deposit
import com.stajkovicluka.financeapp.data.model.Goal
import com.stajkovicluka.financeapp.viewmodel.GoalDetailsViewModel

// Prikazuje detalje jednog cilja i listu njegovih uplata.
@Composable
fun GoalDetailsScreen(
    goalId: Long,
    goalDetailsViewModel: GoalDetailsViewModel,
    onBack: () -> Unit,
    onCreateDeposit: () -> Unit
) {
    LaunchedEffect(goalId) {
        goalDetailsViewModel.loadDetails(goalId)
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        when {
            goalDetailsViewModel.isLoading -> LoadingContent()
            goalDetailsViewModel.errorMessage != null -> MessageContent(goalDetailsViewModel.errorMessage!!)
            goalDetailsViewModel.goal != null -> DetailsContent(
                goal = goalDetailsViewModel.goal!!,
                deposits = goalDetailsViewModel.deposits,
                onBack = onBack,
                onCreateDeposit = onCreateDeposit
            )
        }
    }
}

@Composable
private fun DetailsContent(
    goal: Goal,
    deposits: List<Deposit>,
    onBack: () -> Unit,
    onCreateDeposit: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.back_button))
            }
        }
        item {
            GoalSummary(goal)
        }
        item {
            Column {
                Text(
                    text = stringResource(R.string.deposits_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Button(
                    onClick = onCreateDeposit,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(stringResource(R.string.create_deposit_button))
                }
            }
        }
        if (deposits.isEmpty()) {
            item {
                Text(stringResource(R.string.deposits_empty))
            }
        } else {
            items(deposits, key = { it.id }) { deposit ->
                DepositCard(deposit)
            }
        }
    }
}

@Composable
private fun GoalSummary(goal: Goal) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = goal.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${goal.currentAmount} / ${goal.targetAmount}",
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Napredak: ${goal.progressPct}%",
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(text = goal.status, modifier = Modifier.padding(top = 4.dp))
            goal.deadline?.let { deadline ->
                Text(
                    text = "Rok: ${formatDate(deadline)}",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun DepositCard(deposit: Deposit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = deposit.amount.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatDate(deposit.createdAt),
                modifier = Modifier.padding(top = 4.dp)
            )
            deposit.note?.takeIf { it.isNotBlank() }?.let { note ->
                Text(text = note, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

private fun formatDate(date: String): String {
    val isoDate = date.substringBefore("T")
    val parts = isoDate.split("-")
    return if (parts.size == 3) "${parts[2]}.${parts[1]}.${parts[0]}" else date
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
