package com.example.foodfriends.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = ""
) {
    val displayName: String
        get() = username.ifNotBlank() ?: name.ifNotBlank() ?: "Unknown user"
}
private fun String?.ifNotBlank(): String {
    return this?.takeIf { it.isNotBlank() } ?: ""
}