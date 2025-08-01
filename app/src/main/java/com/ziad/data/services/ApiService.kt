package com.ziad.data.services

import com.ziad.data.model.DeleteResponse
import com.ziad.data.model.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("api/blogs/")
    suspend fun getPosts(): List<Post>

    @GET("api/blogs/show/{id}")
    suspend fun getPostById(@Path("id") id: Int): Post

    @Multipart
    @POST("api/blogs/store")
    suspend fun uploadPost(
        @Part photo: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody
    ): Response<Post>

    @POST("api/blogs/update/{id}")
    suspend fun updatePost(
        @Path("id") id: Int,
        @Part photo: MultipartBody.Part?,
        @Part("title") title: RequestBody,
        @Part("content") content: RequestBody
    ): Response<Post>

    @POST("api/blogs/delete/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<DeleteResponse>
}