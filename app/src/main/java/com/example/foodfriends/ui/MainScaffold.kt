package com.example.foodfriends.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.foodfriends.navigation.BottomNavItem
import com.example.foodfriends.navigation.NavRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavHostController,
    content: @Composable (Modifier) -> Unit
) {
    val bottomItems = listOf(
        BottomNavItem(
            route = NavRoutes.Home.route,
            label = "Home",
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            route = NavRoutes.Posts.route,
            label = "Posts",
            icon = Icons.Default.Add
        ),
        BottomNavItem(
            route = NavRoutes.Friends.route,
            label = "Friends",
            icon = Icons.Default.Group
        ),
        BottomNavItem(
            route = NavRoutes.Profile.route,
            label = "Profile",
            icon = Icons.Default.Person
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val canNavigateBack = navController.previousBackStackEntry != null

    // Hide bottom nav on these screens
    val showBottomBar = currentRoute !in listOf(
        NavRoutes.Login.route,
        NavRoutes.Register.route,
    )

    // Dynamic top bar title
    val title = when {
        currentRoute == NavRoutes.Home.route -> "Food Lovers"
        currentRoute == NavRoutes.Posts.route -> "Posts"
        currentRoute == NavRoutes.Friends.route -> "Friends"
        currentRoute == NavRoutes.Profile.route -> "Profile"
        else -> "FoodFriends"
    }

    Scaffold(
        topBar = {
            if (
                currentRoute !in listOf(
                    NavRoutes.Login.route,
                    NavRoutes.Register.route
                )
            ) {
                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        if (canNavigateBack) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors()
                )
            }
        },

        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomItems.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    launchSingleTop = true
                                    popUpTo(NavRoutes.Home.route)
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        content(Modifier.padding(paddingValues))
    }
}
