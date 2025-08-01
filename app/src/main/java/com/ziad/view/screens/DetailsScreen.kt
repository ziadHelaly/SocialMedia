package com.ziad.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.ziad.R
import com.ziad.viewModel.DetailsViewModel


// Extension to parse ISO and compute human-friendly "time ago"
@Composable
fun rememberRelativeTime(isoTime: String): String {
    // Parse once; we’ll refresh every minute to keep it reasonably up-to-date.
    val formatter = remember {
        java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
    }
    val updatedInstant = remember(isoTime) {
        try {
            java.time.OffsetDateTime.parse(isoTime, formatter).toInstant()
        } catch (e: Exception) {
            java.time.Instant.EPOCH
        }
    }

    // Tick every minute to recompute
    val ticker = remember {
        kotlinx.coroutines.flow.flow {
            while (true) {
                emit(Unit)
                kotlinx.coroutines.delay(60_000L)
            }
        }
    }

    // Recompose on tick
    LaunchedEffect(updatedInstant) {
        // nothing needed, just to keep effect scope alive so snapshotFlow below works if used elsewhere
    }

    // Derived value
    val relative = remember(updatedInstant) {
        mutableStateOf(computeRelativeTime(updatedInstant))
    }

    LaunchedEffect(ticker, updatedInstant) {
        ticker.collect {
            relative.value = computeRelativeTime(updatedInstant)
        }
    }

    return relative.value
}

private fun computeRelativeTime(past: java.time.Instant): String {
    val now = java.time.Instant.now()
    val dur = java.time.Duration.between(past, now)
    return when {
        dur.isNegative -> "just now"
        dur.toMinutes() < 1 -> "just now"
        dur.toMinutes() < 60 -> "${dur.toMinutes()} minute${if (dur.toMinutes() == 1L) "" else "s"} ago"
        dur.toHours() < 24 -> "${dur.toHours()} hour${if (dur.toHours() == 1L) "" else "s"} ago"
        dur.toDays() < 7 -> "${dur.toDays()} day${if (dur.toDays() == 1L) "" else "s"} ago"
        else -> {
            val dt = java.time.ZonedDateTime.ofInstant(past, java.time.ZoneId.systemDefault())
            val fmt = java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")
            "on ${dt.format(fmt)}"
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    postId: Int,
    onBack:()-> Unit,
    viewModel: DetailsViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val post by viewModel.post.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState() // assume you expose this
    /*
        // If you want to show toast/snackbar from message flow
        val scaffoldState = rememberScaffoldState()
        LaunchedEffect(Unit) {
            viewModel.message.collect { msg ->
                scaffoldState.snackbarHostState.showSnackbar(msg)
            }
        }*/
    var showDeleteConfirm by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        viewModel.isDeleted.collect {
            ////////
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getPostById(postId)
    }
    Scaffold(
        //      scaffoldState = scaffoldState,
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
                    // Edit / Update
                    if (post != null) {
                        IconButton(onClick = { }) {
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
                        // simple centered progress
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    /*
                                        errorMessage != null -> {
                                            // error state
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .padding(16.dp),
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text("Something went wrong: $errorMessage")
                                                Spacer(Modifier.height(8.dp))
                                                Button(onClick = { /*viewModel.retry()*/ }) {
                                                    Text("Retry")
                                                }
                                            }
                                        }
                    */
                    post != null -> {
                        val p = post!!
                        // Compute relative updated time
                        val relativeUpdated = rememberRelativeTime(p.updated)

                        // Content
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp)
                        ) {
                            // Updated time subtitle
                            Text(
                                text = "Updated $relativeUpdated",
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            // Photo: large rectangle with aspect ratio
                            GlideImage(
                                model = p.photo,
                                contentDescription = "Post Image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.outline),
                                contentScale = ContentScale.Crop,
//                                placeholder = painterResource(id = R.drawable.placeholder), // replace with your placeholder
//                                error = painterResource(id = R.drawable.ic_broken_image) // replace with your error drawable
                            )

                            Spacer(Modifier.height(16.dp))

                            // Content body
                            Text(
                                text = p.content,
                                style = MaterialTheme.typography.bodyLarge,
                                lineHeight = 20.sp
                            )
                        }
                    }

                    else -> {
                        // empty/fallback
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
                            viewModel.deletePost() // assume this triggers deletion
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
        }
    )
}

