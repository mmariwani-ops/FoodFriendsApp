package com.example.foodfriends.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodfriends.data.friends.FirebaseFriendRepository
import com.example.foodfriends.data.friends.FirebaseUserProfileRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FriendsViewModel(
    private val friendRepository: FirebaseFriendRepository = FirebaseFriendRepository(),
    private val userProfileRepository: FirebaseUserProfileRepository = FirebaseUserProfileRepository()
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val _uiState = MutableStateFlow(FriendsUiState())
    val uiState: StateFlow<FriendsUiState> = _uiState

    init {
        loadFriends()
    }

    fun loadFriends() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _uiState.value = FriendsUiState(
                isLoading = false,
                error = "You need to be logged in to see your FoodFriends."
            )
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                // 1) Hämta UIDs för accepterade vänner
                val friendIds = friendRepository.getFriends(currentUser.uid)
                val friends = userProfileRepository.getUsersByIds(friendIds)

                // 2) Hämta UIDs för inkommande friend requests
                val requestIds = friendRepository.getIncomingRequests(currentUser.uid)
                val incomingRequests = userProfileRepository.getUsersByIds(requestIds)

                // 3) Uppdatera UI-state
                _uiState.value = FriendsUiState(
                    isLoading = false,
                    friends = friends,
                    incomingRequests = incomingRequests,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = FriendsUiState(
                    isLoading = false,
                    friends = emptyList(),
                    incomingRequests = emptyList(),
                    error = e.message ?: "Failed to load friends."
                )
            }
        }
    }

    fun loadIncomingRequests() {
        val currentUid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                val requesterIds = friendRepository.getIncomingRequests(currentUid)
                val requesterUsers = userProfileRepository.getUsersByIds(requesterIds)

                _uiState.value = _uiState.value.copy(
                    incomingRequests = requesterUsers
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message
                )
            }
        }
    }


    fun sendFriendRequest(email: String) {
        val currentUser = auth.currentUser ?: return

        viewModelScope.launch {
            try {
                friendRepository.sendFriendRequest(currentUser.uid, email)
                _uiState.value = _uiState.value.copy(error = null)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Could not send request"
                )
            }
        }
    }
    fun removeFriend(friendId: String) {
        val currentUser = auth.currentUser ?: return

        viewModelScope.launch {
            try {
                friendRepository.removeFriend(currentUser.uid, friendId)
                loadFriends()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "Could not remove friend"
                )
            }
        }
    }
    fun acceptFriendRequest(fromUserId: String) {
        val currentUid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                friendRepository.acceptFriendRequest(currentUid, fromUserId)
                loadFriends()
                loadIncomingRequests()   // uppdatera
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun rejectFriendRequest(fromUserId: String) {
        val currentUid = auth.currentUser?.uid ?: return

        viewModelScope.launch {
            try {
                friendRepository.rejectFriendRequest(currentUid, fromUserId)
                loadIncomingRequests()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
