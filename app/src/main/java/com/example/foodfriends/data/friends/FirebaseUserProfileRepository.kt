package com.example.foodfriends.data.friends

import com.example.foodfriends.data.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseUserProfileRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Hämtar användarprofiler (username + email) baserat på lista med UID:s
    suspend fun getUsersByIds(ids: List<String>): List<User> {
        val resultList = mutableListOf<User>()

        for (uid in ids) {
            val doc = firestore.collection("users").document(uid).get().await()
            if (doc.exists()) {
                val username = doc.getString("username") ?: "(no username)"
                val email = doc.getString("email") ?: ""

                resultList.add(
                    User(
                        id = uid,
                        username = username,
                        email = email
                    )
                )
            }
        }

        return resultList
    }
}
