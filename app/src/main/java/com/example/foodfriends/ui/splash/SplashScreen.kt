package com.example.foodfriends.ui.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private const val SCREEN_PADDING_DP = 32

// Extra poäng :)
@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(SCREEN_PADDING_DP.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
