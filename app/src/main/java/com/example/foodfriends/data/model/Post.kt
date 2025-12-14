package com.example.foodfriends.data.model

data class Post(
    val id: String = "",
    val userId: String = "",
    val restaurantName: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val timestamp: Long = 0
)