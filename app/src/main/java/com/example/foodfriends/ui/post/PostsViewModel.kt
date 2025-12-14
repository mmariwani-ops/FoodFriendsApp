package com.example.foodfriends.ui.post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodfriends.data.model.Post
import com.example.foodfriends.data.posts.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostsViewModel(
    private val postRepository: PostRepository = PostRepository()
) : ViewModel() {

    private val _myPosts = MutableStateFlow<List<Post>>(emptyList())
    val myPosts: StateFlow<List<Post>> = _myPosts
    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> = _selectedPost
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error



    init {
        loadMyPosts()
    }

    fun loadMyPosts() {
        viewModelScope.launch {
            try {
                _myPosts.value = postRepository.getMyPosts()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun createPost(restaurantName: String, rating: Int, comment: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                postRepository.createPost(restaurantName, rating, comment)
                loadMyPosts()
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    fun getPostById(id: String): Post? {
        return myPosts.value.firstOrNull { it.id == id }
    }

    fun updatePost(postId: String, restaurantName: String, rating: Int, comment: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                postRepository.updatePost(postId, restaurantName, rating, comment)
                loadMyPosts()
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deletePost(postId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                postRepository.deletePost(postId)
                loadMyPosts()
                onSuccess()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
    fun loadPostById(postId: String) {
        viewModelScope.launch {
            try {
                _selectedPost.value = postRepository.getPostById(postId)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

}
