package com.google.youtube.models

import androidx.compose.runtime.Immutable

// TODO: Use appropriate return types
@Immutable
data class VideoThumbnailDetails(
    val thumbnailAsset: String,
    val channelAsset: String,
    val title: String,
    val views: String,
    val daysSinceUploaded: String,
    val duration: String,
)
