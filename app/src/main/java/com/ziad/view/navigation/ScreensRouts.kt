package com.ziad.view.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreensRouts {
    @Serializable
    object HomeScreenRout : ScreensRouts()

    @Serializable
    data class DetailsScreenRout(val postId: Int) : ScreensRouts()

}