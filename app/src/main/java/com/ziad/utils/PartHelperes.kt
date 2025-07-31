package com.ziad.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


fun createPartFromString(value: String): RequestBody =
    value.toRequestBody("text/plain".toMediaTypeOrNull())

fun prepareFilePart(partName: String, file: File): MultipartBody.Part {
    val mediaType = when (file.extension.lowercase()) {
        "png" -> "image/png"
        "jpg", "jpeg" -> "image/jpeg"
        else -> "application/octet-stream"
    }.toMediaTypeOrNull()

    val requestFile = file.asRequestBody(mediaType)
    return MultipartBody.Part.createFormData(partName, file.name, requestFile)
}