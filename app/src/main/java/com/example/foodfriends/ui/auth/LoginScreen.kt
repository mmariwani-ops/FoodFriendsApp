package com.example.foodfriends.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
private const val SCREEN_PADDING_DP = 24
private const val SMALL_SPACING_DP = 12
private const val MEDIUM_SPACING_DP = 16
private const val LARGE_SPACING_DP = 24

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,                 //  skickas in utifrån (VM)
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    //  Navigera automatiskt när vi blir inloggade
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) onLoginSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SCREEN_PADDING_DP.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(MEDIUM_SPACING_DP.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(LARGE_SPACING_DP.dp))

        Button(
            onClick = { viewModel.login(email.trim(), password.trim()) },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        if (uiState.errorMessage != null) {
            Spacer(Modifier.height(SMALL_SPACING_DP.dp))
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(MEDIUM_SPACING_DP.dp))

        TextButton(onClick = onNavigateToRegister) {
            Text("Create account")
        }
    }
}