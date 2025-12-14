package com.example.foodfriends.data.posts

import com.example.foodfriends.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class PostRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    private val postsRef = firestore.collection("posts")

    // CREATE POST
    suspend fun createPost(restaurantName: String, rating: Int, comment: String) {

        val uid = auth.currentUser?.uid ?: throw Exception("Not logged in")

        val postData = mapOf(
            "userId" to uid,
            "restaurantName" to restaurantName,
            "rating" to rating,
            "comment" to comment,
            "timestamp" to System.currentTimeMillis()
        )

        postsRef.add(postData).await()
    }

    // GET POSTS FOR LOGGED IN USER
    suspend fun getMyPosts(): List<Post> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        val snapshot = postsRef
            .whereEqualTo("userId", uid)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Post(
                id = doc.id,
                userId = doc.getString("userId") ?: "",
                restaurantName = doc.getString("restaurantName") ?: "",
                rating = (doc.getLong("rating") ?: 0).toInt(),
                comment = doc.getString("comment") ?: "",
                timestamp = doc.getLong("timestamp") ?: 0
            )
        }

    }
    suspend fun updatePost(
        postId: String,
        restaurantName: String,
        rating: Int,
        comment: String
    ) {
        postsRef.document(postId)
            .update(
                mapOf(
                    "restaurantName" to restaurantName,
                    "rating" to rating,
                    "comment" to comment,
                    "timestamp" to System.currentTimeMillis()
                )
            )
            .await()
    }

    suspend fun deletePost(postId: String) {
        postsRef.document(postId).delete().await()
    }

    // GET SINGLE POST BY ID
    suspend fun getPostById(postId: String): Post {
        val doc = postsRef.document(postId).get().await()

        if (!doc.exists()) {
            throw Exception("Post not found")
        }

        return Post(
            id = doc.id,
            userId = doc.getString("userId") ?: "",
            restaurantName = doc.getString("restaurantName") ?: "",
            rating = (doc.getLong("rating") ?: 0).toInt(),
            comment = doc.getString("comment") ?: "",
            timestamp = doc.getLong("timestamp") ?: 0
        )
    }
    suspend fun getFriendsPosts(friendIds: List<String>): List<Post> {
        if (friendIds.isEmpty()) return emptyList()

        val snapshot = postsRef
            .whereIn("userId", friendIds.take(10)) // Firestore limit
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Post(
                id = doc.id,
                userId = doc.getString("userId") ?: "",
                restaurantName = doc.getString("restaurantName") ?: "",
                rating = (doc.getLong("rating") ?: 0).toInt(),
                comment = doc.getString("comment") ?: "",
                timestamp = doc.getLong("timestamp") ?: 0
            )
        }
    }
    suspend fun getPostsForUsers(userIds: List<String>): List<Post> {
        if (userIds.isEmpty()) return emptyList()

        val chunks = userIds.chunked(10) // Firestore whereIn max 10
        val allDocs = mutableListOf<com.google.firebase.firestore.DocumentSnapshot>()

        for (chunk in chunks) {
            val snap = postsRef
                .whereIn("userId", chunk)
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .await()

            allDocs.addAll(snap.documents)
        }

        // ta bort dubletter om en post råkar komma med flera gånger
        val unique = allDocs.distinctBy { it.id }

        return unique.map { doc ->
            Post(
                id = doc.id,
                userId = doc.getString("userId") ?: "",
                restaurantName = doc.getString("restaurantName") ?: "",
                rating = (doc.getLong("rating") ?: 0).toInt(),
                comment = doc.getString("comment") ?: "",
                timestamp = doc.getLong("timestamp") ?: 0L
            )
        }.sortedByDescending { it.timestamp }
    }



}
