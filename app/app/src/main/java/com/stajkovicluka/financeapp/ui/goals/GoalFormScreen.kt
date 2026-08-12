package com.stajkovicluka.financeapp.ui.goals

import android.app.DatePickerDialog
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.viewmodel.GoalsViewModel
import java.util.Calendar

// Prikazuje formu za dodavanje ili izmenu cilja stednje.
@Composable
fun GoalFormScreen(
    goalsViewModel: GoalsViewModel,
    onBack: () -> Unit,
    onCreateSuccess: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var targetAmount by rememberSaveable { mutableStateOf("") }
    var deadlineForRequest by rememberSaveable { mutableStateOf<String?>(null) }
    var deadlineForDisplay by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.create_goal_title),
                style = MaterialTheme.typography.headlineMedium
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.goal_name_label)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )
            OutlinedTextField(
                value = targetAmount,
                onValueChange = { targetAmount = it },
                label = { Text(stringResource(R.string.target_amount_label)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
            OutlinedTextField(
                value = deadlineForDisplay,
                onValueChange = {},
                label = { Text(stringResource(R.string.deadline_optional_label)) },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                trailingIcon = {
                    TextButton(
                        onClick = {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    deadlineForDisplay = "%02d.%02d.%04d".format(dayOfMonth, month + 1, year)
                                    deadlineForRequest = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                    ) {
                        Text(stringResource(R.string.select_date_button))
                    }
                }
            )
            goalsViewModel.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            Button(
                onClick = {
                    goalsViewModel.createGoal(name, targetAmount, deadlineForRequest, onCreateSuccess)
                },
                enabled = !goalsViewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                if (goalsViewModel.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(stringResource(R.string.save_goal_button))
                }
            }
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.back_button))
            }
        }
    }
}
