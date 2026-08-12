package com.stajkovicluka.financeapp.ui.auth

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.stajkovicluka.financeapp.R
import com.stajkovicluka.financeapp.viewmodel.AuthViewModel

// Prikazuje formu za registraciju novog korisnika.
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
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
                text = stringResource(R.string.register_title),
                style = MaterialTheme.typography.headlineMedium
            )
            OutlinedTextField(
                value = authViewModel.firstName,
                onValueChange = { authViewModel.firstName = it },
                label = { Text(stringResource(R.string.first_name_label)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )
            OutlinedTextField(
                value = authViewModel.lastName,
                onValueChange = { authViewModel.lastName = it },
                label = { Text(stringResource(R.string.last_name_label)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
            OutlinedTextField(
                value = authViewModel.email,
                onValueChange = { authViewModel.email = it },
                label = { Text(stringResource(R.string.email_label)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
            OutlinedTextField(
                value = authViewModel.registerUsername,
                onValueChange = { authViewModel.registerUsername = it },
                label = { Text(stringResource(R.string.username_label)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
            OutlinedTextField(
                value = authViewModel.registerPassword,
                onValueChange = { authViewModel.registerPassword = it },
                label = { Text(stringResource(R.string.password_label)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
            OutlinedTextField(
                value = authViewModel.confirmPassword,
                onValueChange = { authViewModel.confirmPassword = it },
                label = { Text(stringResource(R.string.confirm_password_label)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            )
            authViewModel.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            Button(
                onClick = { authViewModel.register(onRegisterSuccess) },
                enabled = !authViewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                if (authViewModel.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(stringResource(R.string.register_button))
                }
            }
            TextButton(onClick = onBack) {
                Text(stringResource(R.string.back_button))
            }
        }
    }
}
