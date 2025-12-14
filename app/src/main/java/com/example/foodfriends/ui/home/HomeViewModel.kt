package com.example.foodfriends.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodfriends.data.friends.FirebaseFriendRepository
import com.example.foodfriends.data.friends.FirebaseUserProfileRepository
import com.example.foodfriends.data.posts.PostRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val friendsRepo: FirebaseFriendRepository = FirebaseFriendRepository(),
    private val userRepo: FirebaseUserProfileRepository = FirebaseUserProfileRepository(),
    private val postRepo: PostRepository = PostRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _feedPosts = MutableStateFlow<List<FeedPost>>(emptyList())
    val feedPosts: StateFlow<List<FeedPost>> = _feedPosts

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadFriendsPosts() {
        viewModelScope.launch {
            try {
                val myUid = auth.currentUser?.uid ?: throw Exception("Not logged in")

                // 1) Hämta friend UIDs
                val friendIds = friendsRepo.getFriends(myUid)

                // 2) Hämta deras posts (du behöver ha denna i PostRepository)
                val posts = postRepo.getPostsForUsers(friendIds)

                // 3) Hämta usernames för alla unika userIds som finns i posts
                val userIds = posts.map { it.userId }.distinct()
                val users = userRepo.getUsersByIds(userIds)
                val usernameMap = users.associateBy({ it.id }, { it.username })

                // 4) Bygg feed-lista (post + username)
                _feedPosts.value = posts.map { post ->
                    FeedPost(
                        post = post,
                        username = usernameMap[post.userId] ?: "Unknown"
                    )
                }

                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    init {
        loadFriendsPosts()
    }
}
