package com.stajkovicluka.financeapp.ui.deposits

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.viewmodel.GoalDetailsViewModel
import com.stajkovicluka.financeapp.data.model.Deposit

// Prikazuje formu za dodavanje ili izmenu uplate za jedan cilj
@Composable
fun DepositFormScreen(
    goalId: Long,
    goalDetailsViewModel: GoalDetailsViewModel,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    depositToEdit: Deposit? = null
) {
    var amount by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(depositToEdit?.id) {
        if (depositToEdit != null) {
            amount = depositToEdit.amount.toPlainString()
            note = depositToEdit.note.orEmpty()
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(
                    if (depositToEdit == null) R.string.create_deposit_title else R.string.edit_deposit_title
                ),
                style = MaterialTheme.typography.headlineMedium
            )
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text(stringResource(R.string.deposit_amount_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text(stringResource(R.string.deposit_note_label)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
            goalDetailsViewModel.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            Button(
                onClick = {
                    if (depositToEdit == null) {
                        goalDetailsViewModel.createDeposit(goalId, amount, note, onSaveSuccess)
                    } else {
                        goalDetailsViewModel.updateDeposit(
                            goalId,
                            depositToEdit.id,
                            amount,
                            note,
                            onSaveSuccess
                        )
                    }
                },
                enabled = !goalDetailsViewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                if (goalDetailsViewModel.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        stringResource(
                            if (depositToEdit == null) R.string.save_deposit_button else R.string.save_deposit_changes_button
                        )
                    )
                }
            }
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.back_button))
            }
        }
    }
}
