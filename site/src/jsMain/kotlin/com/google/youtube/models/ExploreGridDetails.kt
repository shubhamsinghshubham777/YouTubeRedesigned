package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
data class ExploreGridDetails(
    val asset: String,
    val title: String,
    val categoriesWithVideos: List<ExploreGridCategoryWithVideos> = emptyList(),
)

@Immutable
data class ExploreGridCategoryWithVideos(
    val label: String? = null,
    val videos: List<VideoThumbnailDetails>,
)
