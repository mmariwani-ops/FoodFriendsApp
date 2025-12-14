package com.example.foodfriends.ui.post

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun AddPostScreen(navController: NavController) {
    val viewModel: PostsViewModel = viewModel()

    var restaurantName by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var ratingError by remember { mutableStateOf<String?>(null) }
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Add New Post", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // RESTAURANT NAME
        OutlinedTextField(
            value = restaurantName,
            onValueChange = { restaurantName = it },
            label = { Text("Restaurant Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // RATING
        OutlinedTextField(
            value = rating,
            onValueChange = { newValue ->
                rating = newValue

                val ratingInt = newValue.toIntOrNull()

                ratingError = when {
                    newValue.isEmpty() ->
                        "Rating måste anges"

                    ratingInt == null ->
                        "Rating måste vara en siffra"

                    ratingInt < 1 || ratingInt > 5 ->
                        "Rating måste vara mellan 1 och 5"

                    else -> null
                }
            },
            label = { Text("Rating (1–5)") },
            isError = ratingError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (ratingError != null) {
            Text(
                text = ratingError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // COMMENT
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comment") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // SUBMIT BUTTON
        Button(
            onClick = {
                val rate = rating.toIntOrNull() ?: return@Button

                viewModel.createPost(
                    restaurantName = restaurantName.trim(),
                    rating = rate,
                    comment = comment.trim()
                ) {
                    navController.popBackStack()
                }
            },
            enabled = ratingError == null
                    && rating.isNotBlank()
                    && restaurantName.isNotBlank()
                    && comment.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Post")
        }
    }
}
