package com.ziad.utils

import android.net.Uri
import java.io.File

interface ImageFileProvider {
    suspend fun uriToFile(uri: Uri): File
}