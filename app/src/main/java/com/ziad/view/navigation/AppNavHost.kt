package com.ziad.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ziad.view.screens.DetailsScreen
import com.ziad.view.screens.HomeScreen

@Composable
fun AppNav(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = ScreensRouts.HomeScreenRout,
        modifier = modifier
    ) {
        composable<ScreensRouts.HomeScreenRout> {
            HomeScreen(){
                navHostController.navigate(ScreensRouts.DetailsScreenRout(it))
            }
        }
        composable<ScreensRouts.DetailsScreenRout> {
            val args: ScreensRouts.DetailsScreenRout = it.toRoute()
            val postId=args.postId
            DetailsScreen(postId, onBack = {navHostController.popBackStack()})
        }
    }
}