package com.ziad.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ImageFileProviderImpl @Inject constructor(
    private val context: Context
) : ImageFileProvider {
    override suspend fun uriToFile(uri: Uri): File = withContext(Dispatchers.IO) {
        val name = queryFileName(context.contentResolver, uri)
            ?: "post_${System.currentTimeMillis()}.jpg"
        uriToFile(context, uri, name)
    }
    private fun queryFileName(resolver: ContentResolver, uri: Uri): String? {
        var name: String? = null
        val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
        resolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                name = cursor.getString(index)
            }
        }
        return name
    }

}