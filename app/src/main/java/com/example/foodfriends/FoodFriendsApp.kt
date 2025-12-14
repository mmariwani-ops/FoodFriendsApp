package com.example.foodfriends

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.foodfriends.navigation.AppNavGraph
import com.example.foodfriends.navigation.NavRoutes
import com.example.foodfriends.ui.MainScaffold
import com.example.foodfriends.ui.auth.AuthViewModel
import com.example.foodfriends.ui.splash.SplashScreen

@Composable
fun FoodFriendsApp() {
    val navController = rememberNavController()

    // Shared AuthViewModel for the entire app
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.uiState.collectAsState()

    when {
        // Show splash while checking auth
        authState.isLoading -> {
            SplashScreen()
        }

        //  USER IS LOGGED IN -> Wrap the app in MainScaffold
        authState.isLoggedIn -> {
            MainScaffold(navController = navController) { innerModifier ->
                AppNavGraph(
                    navController = navController,
                    startDestination = NavRoutes.Home.route,
                    modifier = innerModifier,
                    authViewModel = authViewModel
                )
            }
        }

        //  USER IS LOGGED OUT -> Show Login/Register WITHOUT MainScaffold
        else -> {
            AppNavGraph(
                navController = navController,
                startDestination = NavRoutes.Login.route,
                authViewModel = authViewModel
            )
        }
    }
}
