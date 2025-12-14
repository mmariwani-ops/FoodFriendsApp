package com.example.foodfriends.data.friends

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseFriendRepository {
    private val firestore = FirebaseFirestore.getInstance()

    // Hämta alla vänners UID
    suspend fun getFriends(uid: String): List<String> {
        val snapshot = firestore
            .collection("users")
            .document(uid)
            .collection("friends")
            .get()
            .await()

        return snapshot.documents.map { it.id }
    }

    // Ta bort en vän
    suspend fun removeFriend(currentUid: String, friendId: String) {

        // Remove friend from current user's list
        firestore.collection("users")
            .document(currentUid)
            .collection("friends")
            .document(friendId)
            .delete()
            .await()

        // ALSO remove current user from friend's list
        firestore.collection("users")
            .document(friendId)
            .collection("friends")
            .document(currentUid)
            .delete()
            .await()
    }
    // Lägg till vän via email
    suspend fun sendFriendRequest(currentUid: String, email: String) {

        // hitta userId baserat på email
        val userQuery = firestore.collection("users")
            .whereEqualTo("email", email)
            .get()
            .await()

        if (userQuery.isEmpty) {
            throw Exception("No user found with that email")
        }

        val targetUid = userQuery.documents.first().id

        // skapa friend request hos mottagaren
        firestore.collection("users")
            .document(targetUid)
            .collection("friend_requests")
            .document(currentUid)
            .set(mapOf("timestamp" to System.currentTimeMillis()))
            .await()
    }
    suspend fun getIncomingRequests(uid: String): List<String> {
        val snapshot = firestore.collection("users")
            .document(uid)
            .collection("friend_requests")
            .get()
            .await()

        return snapshot.documents.map { it.id }
    }
    suspend fun acceptFriendRequest(currentUid: String, requesterUid: String) {

        // 1. Lägg till relationen (båda sidor)
        firestore.collection("users")
            .document(currentUid)
            .collection("friends")
            .document(requesterUid)
            .set(mapOf("added" to true))
            .await()

        firestore.collection("users")
            .document(requesterUid)
            .collection("friends")
            .document(currentUid)
            .set(mapOf("added" to true))
            .await()

        // 2. Ta bort friend request
        firestore.collection("users")
            .document(currentUid)
            .collection("friend_requests")
            .document(requesterUid)
            .delete()
            .await()
    }

    suspend fun rejectFriendRequest(currentUid: String, requesterUid: String) {

        // Ta bort förfrågan
        firestore.collection("users")
            .document(currentUid)
            .collection("friend_requests")
            .document(requesterUid)
            .delete()
            .await()
    }
}
