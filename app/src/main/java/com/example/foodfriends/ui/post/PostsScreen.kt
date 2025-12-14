package com.example.foodfriends.ui.post

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

@Composable
fun PostsScreen(
    navController: NavController,
    viewModel: PostsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val myPosts by viewModel.myPosts.collectAsState()

    // 🔥 FIX: Ladda posts när sidan öppnas
    LaunchedEffect(Unit) {
        viewModel.loadMyPosts()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Your Posts",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("addPost") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Post")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(myPosts) { post ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(post.restaurantName, style = MaterialTheme.typography.titleMedium)
                        Text("Rating: ${post.rating}")
                        Text(post.comment)

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                            // EDIT BUTTON
                            Button(
                                onClick = {
                                    navController.navigate("editPost/${post.id}")
                                },
                                modifier = Modifier.weight(1f)
                            ) { Text("Edit") }

                            // DELETE BUTTON
                            Button(
                                onClick = {
                                    viewModel.deletePost(post.id)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) { Text("Delete") }
                        }
                    }
                }
            }
        }
    }
}

