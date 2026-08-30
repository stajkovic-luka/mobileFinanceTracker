package com.stajkovicluka.financeapp.ui.goals

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.data.model.Deposit
import com.stajkovicluka.financeapp.data.model.Goal
import com.stajkovicluka.financeapp.util.formatAmount
import com.stajkovicluka.financeapp.viewmodel.GoalDetailsViewModel

// Prikazuje detalje jednog cilja i listu njegovih uplata
@Composable
fun GoalDetailsScreen(
    goalId: Long,
    goalDetailsViewModel: GoalDetailsViewModel,
    onBack: () -> Unit,
    onEditGoal: () -> Unit,
    onGoalDeleted: () -> Unit,
    onGoalArchived: () -> Unit,
    onGoalUnarchived: () -> Unit,
    onCreateDeposit: () -> Unit,
    onEditDeposit: (Long) -> Unit,
    onDepositChanged: () -> Unit
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
                onEditGoal = onEditGoal,
                onDeleteGoal = {
                    goalDetailsViewModel.deleteGoal(goalId, onGoalDeleted)
                },
                onArchiveGoal = {
                    goalDetailsViewModel.archiveGoal(goalId, onGoalArchived)
                },
                onUnarchiveGoal = {
                    goalDetailsViewModel.unarchiveGoal(goalId, onGoalUnarchived)
                },
                onCreateDeposit = onCreateDeposit,
                onEditDeposit = onEditDeposit,
                onDeleteDeposit = { depositId ->
                    goalDetailsViewModel.deleteDeposit(goalId, depositId, onDepositChanged)
                }
            )
        }
    }
}

@Composable
private fun DetailsContent(
    goal: Goal,
    deposits: List<Deposit>,
    onBack: () -> Unit,
    onEditGoal: () -> Unit,
    onDeleteGoal: () -> Unit,
    onArchiveGoal: () -> Unit,
    onUnarchiveGoal: () -> Unit,
    onCreateDeposit: () -> Unit,
    onEditDeposit: (Long) -> Unit,
    onDeleteDeposit: (Long) -> Unit
) {
    var showDeleteGoalDialog by remember { mutableStateOf(false) }
    var depositToDelete by remember { mutableStateOf<Deposit?>(null) }
    var newestFirst by rememberSaveable { mutableStateOf(true) }
    val isArchived = goal.status == "ARCHIVED"
    val sortedDeposits = if (newestFirst) {
        deposits.sortedByDescending { it.createdAt }
    } else {
        deposits.sortedBy { it.createdAt }
    }

    if (showDeleteGoalDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteGoalDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            title = { Text(stringResource(R.string.delete_goal_title)) },
            text = { Text(stringResource(R.string.delete_goal_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteGoalDialog = false
                    onDeleteGoal()
                }) {
                    Text(stringResource(R.string.delete_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteGoalDialog = false }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }

    depositToDelete?.let { deposit ->
        AlertDialog(
            onDismissRequest = { depositToDelete = null },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            title = { Text(stringResource(R.string.delete_deposit_title)) },
            text = { Text(stringResource(R.string.delete_deposit_message)) },
            confirmButton = {
                TextButton(onClick = {
                    depositToDelete = null
                    onDeleteDeposit(deposit.id)
                }) {
                    Text(stringResource(R.string.delete_button))
                }
            },
            dismissButton = {
                TextButton(onClick = { depositToDelete = null }) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isArchived) {
                    OutlinedButton(
                        onClick = onUnarchiveGoal,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    ) {
                        Text(stringResource(R.string.unarchive_goal_button))
                    }
                } else {
                    Button(onClick = onEditGoal) {
                        Text(stringResource(R.string.edit_goal_button))
                    }
                    OutlinedButton(
                        onClick = onArchiveGoal,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    ) {
                        Text(stringResource(R.string.archive_goal_button))
                    }
                }
                OutlinedButton(
                    onClick = { showDeleteGoalDialog = true },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.delete_goal_button))
                }
            }
        }
        item {
            Column {
                Text(
                    text = stringResource(R.string.deposits_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp)
                )
                if (!isArchived) {
                    Button(
                        onClick = onCreateDeposit,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(stringResource(R.string.create_deposit_button))
                    }
                }
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
                        text = "${stringResource(R.string.sort_deposits_label)} " +
                            stringResource(sortOrder)
                    )
                }
            }
        }
        if (deposits.isEmpty()) {
            item {
                Text(stringResource(R.string.deposits_empty))
            }
        } else {
            items(sortedDeposits, key = { it.id }) { deposit ->
                DepositCard(
                    deposit = deposit,
                    isArchived = isArchived,
                    onEdit = { onEditDeposit(deposit.id) },
                    onDelete = { depositToDelete = deposit }
                )
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
            Text(text = deadlineText, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun DepositCard(
    deposit: Deposit,
    isArchived: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.amount_with_currency, formatAmount(deposit.amount)),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.16f)
            )
            Text(
                text = stringResource(R.string.deposit_date, formatDate(deposit.createdAt)),
                style = MaterialTheme.typography.bodyMedium
            )
            val note = deposit.note
            if (note != null && note.isNotBlank()) {
                Text(
                    text = stringResource(R.string.deposit_note, note),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            if (!isArchived) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onEdit,
                        colors = ButtonDefaults.outlinedButtonColors()
                    ) {
                        Text(stringResource(R.string.edit_deposit_button))
                    }
                    OutlinedButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(stringResource(R.string.delete_deposit_button))
                    }
                }
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
