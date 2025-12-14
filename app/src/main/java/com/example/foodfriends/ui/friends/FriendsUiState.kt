package com.example.foodfriends.ui.friends

import com.example.foodfriends.data.model.User

data class FriendsUiState(
    val isLoading: Boolean = false,
    val friends: List<User> = emptyList(),
    val incomingRequests: List<User> = emptyList(),
    val error: String? = null
)
