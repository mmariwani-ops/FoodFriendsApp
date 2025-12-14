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

private const val SCREEN_PADDING_DP = 16
private const val SECTION_SPACING_DP = 16
private const val LIST_ITEM_SPACING_DP = 12
private const val CARD_CONTENT_PADDING_DP = 12
private const val SMALL_SPACING_DP = 8
private const val FULL_WIDTH_WEIGHT = 1f

@Composable
fun PostsScreen(
    navController: NavController,
    viewModel: PostsViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val myPosts by viewModel.myPosts.collectAsState()

    // Ladda posts när sidan öppnas
    LaunchedEffect(Unit) {
        viewModel.loadMyPosts()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(SCREEN_PADDING_DP.dp)
    ) {

        Text(
            text = "Your Posts",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(SECTION_SPACING_DP.dp))

        Button(
            onClick = { navController.navigate("addPost") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Post")
        }

        Spacer(modifier = Modifier.height(SECTION_SPACING_DP.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(LIST_ITEM_SPACING_DP.dp)
        ) {
            items(myPosts) { post ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(CARD_CONTENT_PADDING_DP.dp)
                    ) {

                        Text(
                            text = post.restaurantName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text("Rating: ${post.rating}")
                        Text(post.comment)

                        Spacer(modifier = Modifier.height(SMALL_SPACING_DP.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(LIST_ITEM_SPACING_DP.dp)
                        ) {

                            // EDIT BUTTON
                            Button(
                                onClick = {
                                    navController.navigate("editPost/${post.id}")
                                },
                                modifier = Modifier.weight(FULL_WIDTH_WEIGHT)
                            ) {
                                Text("Edit")
                            }

                            // DELETE BUTTON
                            Button(
                                onClick = {
                                    viewModel.deletePost(post.id)
                                },
                                modifier = Modifier.weight(FULL_WIDTH_WEIGHT),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
