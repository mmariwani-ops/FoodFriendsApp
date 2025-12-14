package com.example.foodfriends.ui.profile

data class ProfileUiState(
    val isLoading: Boolean = false,
    val username: String? = null,
    val email: String? = null,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)
