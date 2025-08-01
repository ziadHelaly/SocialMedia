package com.ziad.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException


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
fun uriToFile(context: Context, uri: Uri, outputName: String): File {
    val inputStream = context.contentResolver.openInputStream(uri)
        ?: throw IOException("Unable to open URI")
    val tempFile = File(context.cacheDir, outputName)
    tempFile.outputStream().use { output ->
        inputStream.copyTo(output)
    }
    return tempFile
}