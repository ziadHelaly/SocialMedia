package com.ziad.data.repository

import com.ziad.data.model.Post
import com.ziad.utils.Result
import java.io.File

interface PostsRepository {
    suspend fun getAllPosts(): List<Post>
    suspend fun getPost(id:Int): Post
    suspend fun storePost(
        photo: File,
        title: String,
        content: String
    ): Result
    suspend fun updatePost(
        id: Int,
        photo: File?,
        title: String,
        content: String
    ): Result
    suspend fun deletePost(
        id: Int
    ): String?
}