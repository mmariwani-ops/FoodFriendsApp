package com.example.foodfriends.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.foodfriends.ui.auth.AuthViewModel
import com.example.foodfriends.ui.auth.LoginScreen
import com.example.foodfriends.ui.auth.RegisterScreen
import com.example.foodfriends.ui.post.AddPostScreen
import com.example.foodfriends.ui.post.PostsScreen
import com.example.foodfriends.ui.friends.FriendsScreen
import com.example.foodfriends.ui.home.HomeScreen
import com.example.foodfriends.ui.profile.ProfileScreen
import com.example.foodfriends.ui.friends.FriendProfileScreen
import com.example.foodfriends.ui.post.EditPostScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel          // få in samma VM som i FoodFriendsApp
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // LOGIN
        composable(NavRoutes.Login.route) {
            LoginScreen(
                viewModel = authViewModel,              //  samma VM
                onNavigateToRegister = {
                    navController.navigate(NavRoutes.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(NavRoutes.Home.route) {
                        launchSingleTop = true
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // REGISTER
        composable(NavRoutes.Register.route) {
            RegisterScreen(
                viewModel = authViewModel,              // samma VM
                onRegisterSuccess = {
                    navController.navigate(NavRoutes.Home.route) {
                        launchSingleTop = true
                        popUpTo(NavRoutes.Register.route) { inclusive = true }
                    }
                }
            )
        }

        // HOME
        composable(NavRoutes.Home.route) {
            HomeScreen()
        }

        // Posts
        composable(NavRoutes.Posts.route) {
            PostsScreen(navController = navController)
        }

        // ADD POST
        composable(NavRoutes.AddPost.route) {
            AddPostScreen(navController = navController)
        }

        // FRIENDS
        composable(NavRoutes.Friends.route) {
            FriendsScreen(navController = navController)
        }
        composable(
            route = "friendProfile/{friendId}",
            arguments = listOf(
                navArgument("friendId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val friendId = backStackEntry.arguments?.getString("friendId") ?: ""
            FriendProfileScreen(friendId = friendId)
        }

        // PROFILE
        composable(NavRoutes.Profile.route) {
            ProfileScreen(
                navController = navController,
                authViewModel = authViewModel          //  samma VM -> rätt login-status
            )
        }

        // ADD / EDIT REVIEW
        composable(
            route = NavRoutes.EditPost.route,
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->

            val postId = backStackEntry.arguments?.getString("postId") ?: ""

            EditPostScreen(
                navController = navController,
                postId = postId
            )
        }

    }
}
