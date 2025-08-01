package com.ziad.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziad.data.model.Post
import com.ziad.data.repository.PostsRepository
import com.ziad.utils.ImageFileProvider
import com.ziad.utils.Result
import com.ziad.utils.uriToFile
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PostsRepository,
    private val imageFileProvider: ImageFileProvider
) : ViewModel() {
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts = _posts.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()
    private val _errorMessage = MutableSharedFlow<String?>()
    val errorMessage = _errorMessage.asSharedFlow()

    fun getAllPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            try {
                _posts.value = repository.getAllPosts()
            }catch (e: Exception){
                Log.d("``TAG``", "getAllPosts:${e.message} ")
            }
            _isLoading.value = false
        }
    }

    /*fun createPost(
        title: String,
        content: String,
        photo: Uri
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            val res = repository.storePost(
                photo = photo,
                title = title,
                content = content
            )
            _isLoading.value = false
            if (res is Result.Error) {
//                _message.emit(res.message)
            } else {
//                _message.emit("Post updated successfully")
            }
        }
    }*/
    fun createPost(
        title: String,
        content: String,
        photoUri: Uri?
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val photoFile = photoUri?.let { imageFileProvider.uriToFile(it) }
                if (photoFile != null){
                    val res = repository.storePost(
                        photo = photoFile,
                        title = title,
                        content = content
                    )
                    if (res is Result.Error) {
//                        _message.emit(res.message)
                    } else {
                        Log.d("``TAG``", "createPost: post created")
//                        _message.emit("Post created successfully")
                        // optionally refresh list
                        getAllPosts()
                    }

                }

            } catch (e: Exception) {
//                _message.emit("Failed to create post: ${e.message ?: "unknown error"}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}