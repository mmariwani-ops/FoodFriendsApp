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

private const val SCREEN_PADDING_DP = 16
private const val LARGE_SPACING_DP = 24
private const val MEDIUM_SPACING_DP = 12
private const val SMALL_SPACING_DP = 8

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
            .padding(SCREEN_PADDING_DP.dp)
    ) {

        Text(
            text = "My Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(LARGE_SPACING_DP.dp))

        //  UTLOGGAD -> visa login / register
        if (!authState.isLoggedIn) {
            Text(
                text = "You are not logged in",
                color = MaterialTheme.colorScheme.error
            )

            Spacer(Modifier.height(MEDIUM_SPACING_DP.dp))

            Button(
                onClick = { navController.navigate(NavRoutes.Login.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Log In")
            }

            Spacer(Modifier.height(SMALL_SPACING_DP.dp))

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

                Spacer(Modifier.height(LARGE_SPACING_DP.dp))

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
