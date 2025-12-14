package com.example.foodfriends.ui.friends

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodfriends.data.friends.FirebaseUserProfileRepository
import kotlinx.coroutines.launch

@Composable
fun FriendProfileScreen(friendId: String) {

    val repo = remember { FirebaseUserProfileRepository() }
    var isLoading by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf<String?>(null) }
    var email by remember { mutableStateOf<String?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(friendId) {
        scope.launch {
            try {
                val user = repo.getUsersByIds(listOf(friendId)).firstOrNull()
                if (user != null) {
                    username = user.username
                    email = user.email
                    error = null
                } else {
                    error = "User not found"
                }
            } catch (e: Exception) {
                error = e.message ?: "Failed to load friend"
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Friend Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text(
                    text = error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }
            else -> {
                Text("Username: ${username ?: "-"}")
                Text("Email: ${email ?: "-"}")
            }
        }
    }
}
