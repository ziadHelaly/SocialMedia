package com.ziad.data.model

import com.google.gson.annotations.SerializedName

data class Post(
    val id: Int,
    val content: String,
    val photo: String,
    val title: String,
    @SerializedName("created_at")
    val created: String,
    @SerializedName("updated_at")
    val updated: String
)
