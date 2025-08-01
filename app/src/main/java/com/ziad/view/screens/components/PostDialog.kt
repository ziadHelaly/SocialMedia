package com.ziad.view.screens.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ziad.data.model.Post

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PostDialog(
    isUpdate: Boolean=false,
    updatePost: Post?=null,
    onDismiss: () -> Unit,
    onSubmit: (title: String, content: String, imageUri: Uri?, isPhotoChanged:Boolean) -> Unit
) {
    var title by remember { mutableStateOf(updatePost?.title ?: "") }
    var content by remember { mutableStateOf(updatePost?.content ?: "") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var isPhotoChanged by remember { mutableStateOf(false) }
    var showValidationError by remember { mutableStateOf(false) }

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            isPhotoChanged = true
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text( if (isUpdate) "Update Post" else "New Post") },
        text = {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.outline),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUri != null || isUpdate) {
                        GlideImage(
                            model = if(isUpdate && !isPhotoChanged) updatePost!!.photo else imageUri ,
                            contentDescription = "Selected image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Fit
                        )
                        TextButton(
                            onClick = { pickImageLauncher.launch("image/*") },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(4.dp)
                        ) {
                            Text("Change")
                        }
                    } else {
                        TextButton(onClick = { pickImageLauncher.launch("image/*") }) {
                            Text("Select Image")
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    isError = showValidationError && title.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Message") },
                    maxLines = 5,
                    isError = showValidationError && content.isBlank(),
                    modifier = Modifier.fillMaxWidth()
                )

                if (showValidationError && (title.isBlank() || content.isBlank())) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Title and message cannot be empty.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (title.isBlank() || content.isBlank()) {
                    showValidationError = true
                } else {
                    onSubmit(title.trim(), content.trim(), imageUri, isPhotoChanged)
                }
            }) {
                Text(if (isUpdate) "Update" else "Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
