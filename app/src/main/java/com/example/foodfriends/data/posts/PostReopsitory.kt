package com.example.foodfriends.data.posts

import com.example.foodfriends.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

private const val COLLECTION_POSTS = "posts"
private const val FIELD_USER_ID = "userId"
private const val FIELD_RESTAURANT_NAME = "restaurantName"
private const val FIELD_RATING = "rating"
private const val FIELD_COMMENT = "comment"
private const val FIELD_TIMESTAMP = "timestamp"
private const val DEFAULT_LONG = 0L
private const val FIRESTORE_WHERE_IN_LIMIT = 10

class PostRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {

    private val postsRef = firestore.collection(COLLECTION_POSTS)

    // CREATE POST
    suspend fun createPost(
        restaurantName: String,
        rating: Int,
        comment: String
    ) {
        val uid = auth.currentUser?.uid ?: throw Exception("Not logged in")

        val postData = mapOf(
            FIELD_USER_ID to uid,
            FIELD_RESTAURANT_NAME to restaurantName,
            FIELD_RATING to rating,
            FIELD_COMMENT to comment,
            FIELD_TIMESTAMP to System.currentTimeMillis()
        )

        postsRef.add(postData).await()
    }

    // GET POSTS FOR LOGGED IN USER
    suspend fun getMyPosts(): List<Post> {
        val uid = auth.currentUser?.uid ?: return emptyList()

        val snapshot = postsRef
            .whereEqualTo(FIELD_USER_ID, uid)
            .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.map { doc ->
            Post(
                id = doc.id,
                userId = doc.getString(FIELD_USER_ID) ?: "",
                restaurantName = doc.getString(FIELD_RESTAURANT_NAME) ?: "",
                rating = (doc.getLong(FIELD_RATING) ?: DEFAULT_LONG).toInt(),
                comment = doc.getString(FIELD_COMMENT) ?: "",
                timestamp = doc.getLong(FIELD_TIMESTAMP) ?: DEFAULT_LONG
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
                    FIELD_RESTAURANT_NAME to restaurantName,
                    FIELD_RATING to rating,
                    FIELD_COMMENT to comment,
                    FIELD_TIMESTAMP to System.currentTimeMillis()
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
            userId = doc.getString(FIELD_USER_ID) ?: "",
            restaurantName = doc.getString(FIELD_RESTAURANT_NAME) ?: "",
            rating = (doc.getLong(FIELD_RATING) ?: DEFAULT_LONG).toInt(),
            comment = doc.getString(FIELD_COMMENT) ?: "",
            timestamp = doc.getLong(FIELD_TIMESTAMP) ?: DEFAULT_LONG
        )
    }

    suspend fun getPostsForUsers(userIds: List<String>): List<Post> {
        if (userIds.isEmpty()) return emptyList()

        val chunks = userIds.chunked(FIRESTORE_WHERE_IN_LIMIT)
        val allDocs = mutableListOf<com.google.firebase.firestore.DocumentSnapshot>()

        for (chunk in chunks) {
            val snap = postsRef
                .whereIn(FIELD_USER_ID, chunk)
                .orderBy(FIELD_TIMESTAMP, Query.Direction.DESCENDING)
                .get()
                .await()

            allDocs.addAll(snap.documents)
        }

        val unique = allDocs.distinctBy { it.id }
        //
        return unique
            .map { doc ->
                Post(
                    id = doc.id,
                    userId = doc.getString(FIELD_USER_ID) ?: "",
                    restaurantName = doc.getString(FIELD_RESTAURANT_NAME) ?: "",
                    rating = (doc.getLong(FIELD_RATING) ?: DEFAULT_LONG).toInt(),
                    comment = doc.getString(FIELD_COMMENT) ?: "",
                    timestamp = doc.getLong(FIELD_TIMESTAMP) ?: DEFAULT_LONG
                )
            }
            .sortedByDescending { it.timestamp }
    }
}
