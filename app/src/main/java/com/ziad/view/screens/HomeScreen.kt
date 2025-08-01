package com.ziad.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ziad.R
import com.ziad.utils.uriToFile
import com.ziad.view.screens.components.CreatePostDialog
import com.ziad.view.screens.components.EmptyState
import com.ziad.view.screens.components.PostRow
import com.ziad.viewModel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onNavToDetails: (Int) -> Unit
) {
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
//    val errorMessage by viewModel.errorMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showNewPostDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getAllPosts()
    }
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Home",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showNewPostDialog = true }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Post")
                }
            }
        ) { innerPadding ->
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (posts.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(posts) {
                            PostRow(it, onNavToDetails)
                        }
                    }
                } else {
                    EmptyState(
                        imgRes = R.drawable.ic_empty_posts,
                        mainText = "No Post Available",
                        description = "Add Some Posts from add button",
                        modifier = Modifier.fillMaxSize()
                    )
                }


                if (showNewPostDialog) {
                    CreatePostDialog(
                        onDismiss = { showNewPostDialog = false },
                        onSubmit = { title, content, imageUri ->
                            if (imageUri != null) {
                                viewModel.createPost(title, content, imageUri)

                            } else {
                                Log.d("``TAG``", "HomeScreen: should add a img")
                            }
                            showNewPostDialog = false
                        }
                    )
                }
            }
        }
    }
}