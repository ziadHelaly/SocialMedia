package com.ziad.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad.data.model.Post
import com.ziad.data.repository.PostsRepository
import com.ziad.utils.ImageFileProvider
import com.ziad.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repo: PostsRepository,
    private val imageFileProvider: ImageFileProvider

) : ViewModel() {
    private val _post = MutableStateFlow<Post?>(null)
    val post = _post.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    private val _isDeleted = MutableSharedFlow<Boolean>()
    val isDeleted = _isDeleted.asSharedFlow()

    fun getPostById(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _post.value = repo.getPost(id)
            _isLoading.value = false
        }
    }

    fun deletePost() {
        viewModelScope.launch {
            _message.emit(repo.deletePost(_post.value?.id ?: 1) ?: "")
            _isDeleted.emit(true)
        }
    }

    fun updatePost(
        title: String,
        content: String,
        photoUri: Uri?
    ) {
        viewModelScope.launch {
            try {
                val photoFile = photoUri?.let { imageFileProvider.uriToFile(it) }

                val res = repo.updatePost(
                    id = _post.value?.id ?: 1,
                    photo = photoFile,
                    title = title,
                    content = content
                )
                if (res is Result.Error) {
                    Log.d("```TAG```", "updatePost: ${res.message}")
                    _message.emit(res.message)
                } else {
                    Log.d("``TAG``", "createPost: post updated")
                    _message.emit("Post Updated successfully")
                    getPostById(id = _post.value?.id ?: 1)
                }

            } catch (e: Exception) {
                Log.d("``TAG``", "Failed to create post: ${e.message ?: "unknown error"}")
                _message.emit("Failed to create post: ${e.message ?: "unknown error"}")
            }
        }
    }

}