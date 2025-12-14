package com.example.foodfriends.ui.friends

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.foodfriends.data.model.User

@Composable
fun FriendsScreen(
    navController: NavController,
    viewModel: FriendsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Your Friends",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // TEXT INPUT
        var emailInput by remember { mutableStateOf("") }

        TextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Add friend by email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (emailInput.isNotBlank()) {
                    viewModel.sendFriendRequest(emailInput.trim())
                    emailInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Friend")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            state.isLoading -> {
                CircularProgressIndicator()
            }

            state.error != null -> {
                Text(
                    text = state.error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }

            else -> {

                if (state.incomingRequests.isNotEmpty()) {

                    Text(
                        text = "Friend Requests",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    state.incomingRequests.forEach { user ->
                        IncomingRequestRow(
                            user = user,
                            onAccept = { viewModel.acceptFriendRequest(user.id) },
                            onReject = { viewModel.rejectFriendRequest(user.id) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                state.friends.forEach { user ->
                    FriendRow(
                        user = user,
                        onClick = {
                            navController.navigate("friendProfile/${user.id}")
                        },
                        onRemove = {
                            viewModel.removeFriend(user.id)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}


@Composable
fun FriendRow(
    user: User,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(
            text = user.displayName,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = user.email ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = onRemove,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Text("Remove Friend")
        }
    }
}
@Composable
fun IncomingRequestRow(
    user: User,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Column(modifier = Modifier.padding(8.dp)) {

        Text(
            text = user.displayName,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row {
            Button(
                onClick = onAccept,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Accept")
            }

            OutlinedButton(onClick = onReject) {
                Text("Reject")
            }
        }
    }
}


