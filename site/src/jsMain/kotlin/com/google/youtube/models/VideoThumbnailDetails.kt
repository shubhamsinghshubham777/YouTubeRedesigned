package com.google.youtube.models

import androidx.compose.runtime.Immutable

// TODO: Use appropriate return types
@Immutable
data class VideoThumbnailDetails(
    val id: String? = null,
    val thumbnailAsset: String,
    val channelAsset: String,
    val title: String,
    val channelName: String,
    val isVerified: Boolean,
    // TODO: Rename this to viewCount
    val views: String,
    val daysSinceUploaded: String,
    val duration: String,
    val likeCount: String? = null,
    val dislikeCount: String? = null,
    val subscribersCount: String? = null,
    val uploadDate: String? = null,
)
