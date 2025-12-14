package com.example.foodfriends

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.foodfriends.ui.theme.FoodFriendsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FoodFriendsTheme {
                FoodFriendsApp()
            }
        }
    }
}