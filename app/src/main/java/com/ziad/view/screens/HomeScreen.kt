package com.ziad.view.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ziad.view.screens.components.PostRow
import com.ziad.viewModel.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), modifier: Modifier = Modifier,onNavToDetails:(Int)->Unit) {
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getAllPosts()
    }
    if (isLoading){
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }else{
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxSize()
        ){
            items(posts) {
                PostRow(it,onNavToDetails)
            }
        }
    }

}