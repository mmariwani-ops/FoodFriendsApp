package com.example.foodfriends.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()
    fun login(email: String, password: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _uiState.value =
                    if (task.isSuccessful) {
                        _uiState.value.copy(isLoading = false, isLoggedIn = true)
                    } else {
                        _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            errorMessage = task.exception?.message ?: "Login failed."
                        )
                    }
            }
    }

    fun register(email: String, password: String, username: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (!task.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isLoggedIn = false,
                        errorMessage = task.exception?.message ?: "Registration failed."
                    )
                    return@addOnCompleteListener
                }

                val uid = auth.currentUser!!.uid

                // Step 1: Link user profile in Firestore
                val userData = mapOf(
                    "email" to email,
                    "username" to username
                )

                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .set(userData)
                    .addOnSuccessListener {
                        // Step 2: Only mark as logged in when Firestore doc created/exists
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            errorMessage = null
                        )
                    }
                    .addOnFailureListener { e ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isLoggedIn = false,
                            errorMessage = "Account created, but profile failed to save: ${e.message}"
                        )
                    }
            }
    }
    fun logout() {
        auth.signOut()
        _uiState.value = AuthUiState()
    }
}