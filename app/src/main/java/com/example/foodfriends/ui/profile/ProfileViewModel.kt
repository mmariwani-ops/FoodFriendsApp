package com.example.foodfriends.ui.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val user = auth.currentUser

        if (user == null) {
            _uiState.value = ProfileUiState(
                isLoading = false,
                errorMessage = "Not logged in"
            )
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        firestore.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { doc ->
                val username = doc.getString("username")?: "(no username)"

                _uiState.value = ProfileUiState(
                    isLoading = false,
                    username = username,
                    email = user.email
                )
            }
            .addOnFailureListener {
                _uiState.value = ProfileUiState(
                    isLoading = false,
                    errorMessage = "Failed to load profile"
                )
            }
    }
}
