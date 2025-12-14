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

private const val SCREEN_PADDING_DP = 16
private const val SECTION_SPACING_DP = 16
private const val SMALL_SPACING_DP = 8
private const val TINY_SPACING_DP = 6
private const val ROW_PADDING_DP = 8
private const val BUTTON_TOP_PADDING_DP = 4

@Composable
fun FriendsScreen(
    navController: NavController,
    viewModel: FriendsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SCREEN_PADDING_DP.dp)
    ) {

        Text(
            text = "Your Friends",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(SECTION_SPACING_DP.dp))

        // TEXT INPUT
        var emailInput by remember { mutableStateOf("") }

        TextField(
            value = emailInput,
            onValueChange = { emailInput = it },
            label = { Text("Add friend by email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(SMALL_SPACING_DP.dp))

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

        Spacer(modifier = Modifier.height(SECTION_SPACING_DP.dp))

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

                    Spacer(modifier = Modifier.height(SMALL_SPACING_DP.dp))

                    state.incomingRequests.forEach { user ->
                        IncomingRequestRow(
                            user = user,
                            onAccept = { viewModel.acceptFriendRequest(user.id) },
                            onReject = { viewModel.rejectFriendRequest(user.id) }
                        )
                        Spacer(modifier = Modifier.height(SMALL_SPACING_DP.dp))
                    }

                    Spacer(modifier = Modifier.height(SECTION_SPACING_DP.dp))
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
                    Spacer(modifier = Modifier.height(SMALL_SPACING_DP.dp))
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
            .padding(ROW_PADDING_DP.dp)
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

        Spacer(modifier = Modifier.height(TINY_SPACING_DP.dp))

        Button(
            onClick = onRemove,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = BUTTON_TOP_PADDING_DP.dp)
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
    Column(
        modifier = Modifier.padding(ROW_PADDING_DP.dp)
    ) {

        Text(
            text = user.displayName,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(TINY_SPACING_DP.dp))

        Row {
            Button(
                onClick = onAccept,
                modifier = Modifier.padding(end = SMALL_SPACING_DP.dp)
            ) {
                Text("Accept")
            }

            OutlinedButton(onClick = onReject) {
                Text("Reject")
            }
        }
    }
}
