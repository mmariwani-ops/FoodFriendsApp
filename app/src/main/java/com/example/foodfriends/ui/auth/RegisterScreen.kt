package com.example.foodfriends.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

private const val SCREEN_PADDING_DP = 16
private const val SMALL_SPACING_DP = 12
private const val LARGE_SPACING_DP = 24
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,                 //  samma VM
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    //  När vi blivit inloggade efter register, navigera hem
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) onRegisterSuccess()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(SCREEN_PADDING_DP.dp)
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(LARGE_SPACING_DP.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(SMALL_SPACING_DP.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(SMALL_SPACING_DP.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(LARGE_SPACING_DP.dp))

        Button(
            onClick = {
                viewModel.register(
                    email = email.trim(),
                    password = password.trim(),
                    username = username.trim()
                )
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        if (uiState.isLoading) {
            Spacer(Modifier.height(SMALL_SPACING_DP.dp))
            CircularProgressIndicator()
        }

        uiState.errorMessage?.let { msg ->
            Spacer(Modifier.height(SMALL_SPACING_DP.dp))
            Text(
                text = msg,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
