package com.example.foodfriends.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foodfriends.navigation.NavRoutes
import com.example.foodfriends.ui.auth.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(),
    authViewModel: AuthViewModel          // får in SAMMA AuthViewModel
) {
    val profileState by viewModel.uiState.collectAsState()
    val authState by authViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("My Profile", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        //  UTLOGGAD -> visa login / register
        if (!authState.isLoggedIn) {
            Text(
                text = "You are not logged in",
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate(NavRoutes.Login.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In")
            }

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = { navController.navigate(NavRoutes.Register.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Account")
            }
            return
        }

        //  INLOGGAD visa profilinfo + logout
        when {
            profileState.isLoading -> {
                CircularProgressIndicator()
            }

            profileState.errorMessage != null -> {
                Text(
                    text = profileState.errorMessage!!,
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {
                Text("Username: ${profileState.username ?: "-"}")
                Text("Email: ${profileState.email ?: "-"}")

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = { authViewModel.logout() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Logout")
                }
            }
        }
    }
}
