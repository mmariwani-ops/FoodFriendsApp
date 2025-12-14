package com.example.foodfriends.ui.post


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun EditPostScreen(
    navController: NavController,
    postId: String,
    viewModel: PostsViewModel = viewModel()
) {
    val post by viewModel.selectedPost.collectAsState()

    var restaurantName by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var comment by remember { mutableStateOf("") }
    var ratingError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(postId) {
        viewModel.loadPostById(postId)
    }
    LaunchedEffect(post) {
        post?.let {
            restaurantName = it.restaurantName
            rating = it.rating.toString()
            comment = it.comment

            ratingError = null // 👈 STEG 5 – detta är hela grejen
        }
    }

    if (post == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Edit Post", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        //Restaurant name
        OutlinedTextField(
            value = restaurantName,
            onValueChange = { restaurantName = it },
            label = { Text("Restaurant Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        //Rating
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

        //Comment
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comment") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // SAVE BUTTON
        Button(
            onClick = {
                viewModel.updatePost(
                    postId = postId,
                    restaurantName = restaurantName.trim(),
                    rating = rating.toIntOrNull() ?: return@Button,
                    comment = comment.trim()
                ) {
                    navController.popBackStack()
                }
            },
            enabled = ratingError == null
                    && restaurantName.isNotBlank()
                    && comment.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Changes")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // DELETE BUTTON
        Button(
            onClick = {
                viewModel.deletePost(postId) {
                    navController.popBackStack()   // tillbaka när borttagen
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Post")
        }
    }
}
