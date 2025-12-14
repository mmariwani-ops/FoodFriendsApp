package com.example.foodfriends.navigation

// Alldeles för mycket
sealed class NavRoutes(val route: String) {

    data object Login : NavRoutes("login")
    data object Register : NavRoutes("register")
    data object Home : NavRoutes("home")
    data object Posts : NavRoutes("posts")
    data object AddPost : NavRoutes("addPost")
    data object Friends : NavRoutes("friends")
    data object Profile : NavRoutes("profile")
    data object EditPost : NavRoutes("editPost/{postId}") {
    }

}
