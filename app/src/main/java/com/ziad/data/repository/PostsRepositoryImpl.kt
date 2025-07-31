package com.ziad.data.repository

import com.ziad.data.model.Post
import com.ziad.data.services.ApiService
import com.ziad.utils.Result
import com.ziad.utils.createPartFromString
import com.ziad.utils.prepareFilePart
import java.io.File
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(private val service: ApiService) : PostsRepository {
    override suspend fun getAllPosts(): List<Post> {
        return service.getPosts()
    }

    override suspend fun getPost(id: Int): Post {
        return service.getPostById(id)
    }

    override suspend fun storePost(
        photo: File,
        title: String,
        content: String
    ): Result {
        return try {
            val titleBody = createPartFromString(title)
            val contentBody = createPartFromString(content)
            val photoPart = prepareFilePart("photo", photo)

            val response = service.uploadPost(photoPart, titleBody, contentBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error(response.message())
            } else {
                val errMsg = response.errorBody()?.string() ?: "Unknown server error"
                Result.Error(errMsg)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun updatePost(
        id: Int,
        photo: File,
        title: String,
        content: String
    ): Result {
        return try {
            val titleBody = createPartFromString(title)
            val contentBody = createPartFromString(content)
            val photoPart = prepareFilePart("photo", photo)

            val response = service.updatePost(id, photoPart, titleBody, contentBody)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)
                } ?: Result.Error(response.message())
            } else {
                val errMsg = response.errorBody()?.string() ?: "Unknown server error"
                Result.Error(errMsg)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun deletePost(id: Int): String? {
        val result = service.deletePost(id)
        if (!result.isSuccessful) {
            throw Exception(result.body()?.error)
        }
        return result.body()?.message
    }

}