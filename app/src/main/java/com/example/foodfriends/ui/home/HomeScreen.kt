package com.example.foodfriends.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

private const val SCREEN_PADDING_DP = 16
private const val SECTION_SPACING_DP = 16
private const val LIST_ITEM_SPACING_DP = 8
private const val CARD_CONTENT_PADDING_DP = 12
private const val TEXT_SPACING_DP = 4

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val feedPosts by viewModel.feedPosts.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(SCREEN_PADDING_DP.dp)
    ) {
        Text(
            text = "Friends' Activity",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(SECTION_SPACING_DP.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(LIST_ITEM_SPACING_DP.dp)
        ) {
            items(feedPosts) { feedPost ->   // INTE count, INTE list:
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(CARD_CONTENT_PADDING_DP.dp)
                    ) {

                        //  VEM POSTADE
                        Text(
                            text = feedPost.username,
                            style = MaterialTheme.typography.labelMedium
                        )

                        Spacer(modifier = Modifier.height(TEXT_SPACING_DP.dp))

                        //  RATING + RESTAURANG
                        Text(
                            text = "${feedPost.post.restaurantName} · ${feedPost.post.rating}/5",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(TEXT_SPACING_DP.dp))

                        //  KOMMENTAR
                        Text(
                            text = feedPost.post.comment,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
