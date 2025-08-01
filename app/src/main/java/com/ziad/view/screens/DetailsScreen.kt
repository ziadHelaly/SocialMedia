package com.ziad.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ziad.view.screens.components.PostDialog
import com.ziad.view.screens.components.rememberRelativeTime
import com.ziad.viewModel.DetailsViewModel


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    postId: Int,
    onBack: () -> Unit,
    viewModel: DetailsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val post by viewModel.post.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showDeleteConfirmed by remember { mutableStateOf(false) }

    var showEditDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.isDeleted.collect {
            showDeleteConfirmed = true
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getPostById(postId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = post?.title ?: "Post Details",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (post != null) {
                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit post")
                        }
                        IconButton(onClick = { showDeleteConfirm = true }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete post"
                            )
                        }
                    }
                }
            )
        },
        content = { innerPadding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                when {
                    isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    post != null -> {

                        val relativeUpdated = rememberRelativeTime(post!!.updated)
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Updated $relativeUpdated",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            GlideImage(
                                model = post!!.photo,
                                contentDescription = "Post Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.background),
                                contentScale = ContentScale.Fit,
//                                placeholder = painterResource(id = R.drawable.placeholder), // replace with your placeholder
//                                error = painterResource(id = R.drawable.ic_broken_image) // replace with your error drawable
                            )

                            Spacer(Modifier.height(16.dp))

                            // Content body
                            Text(
                                text = post!!.content,
                                style = MaterialTheme.typography.bodyLarge,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    else -> {

                        Text(
                            text = "No post available.",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            if (showDeleteConfirm && post != null) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirm = false },
                    title = { Text("Delete Post") },
                    text = { Text("Are you sure you want to delete this post? This action cannot be undone.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDeleteConfirm = false
                            viewModel.deletePost()
                        }) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirm = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }
            if (showDeleteConfirmed) {
                AlertDialog(
                    onDismissRequest = { },
                    title = { Text("Post Deleted") },
                    text = { Text("Post Deleted Successfully") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDeleteConfirmed = false
                            onBack()
                        }) {
                            Text("Done")
                        }
                    }
                )
            }
            if (showEditDialog && post != null) {
                PostDialog(
                    isUpdate = true,
                    updatePost = post,
                    onDismiss = { showEditDialog = false },
                    onSubmit = { title, content, imageUri, isPhotoChanged ->
                        viewModel.updatePost(title, content, imageUri)
                        showEditDialog = false
                    }
                )
            }
        }
    )
}

