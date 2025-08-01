package com.ziad.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad.data.model.Post
import com.ziad.data.repository.PostsRepository
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
class DetailsViewModel @Inject constructor(private val repo: PostsRepository) : ViewModel() {
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
        photo:File?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            val res = repo.updatePost(
                id = _post.value?.id ?: 1,
                photo = photo,
                title = title,
                content = content
            )
            _isLoading.value = false
            if (res is Result.Error) {
                _message.emit(res.message)
            } else {
                _message.emit("Post updated successfully")
            }
        }
    }

}