package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
data class ShortThumbnailDetails(
    val id: String,
    val thumbnailAsset: String,
    val title: String,
    val channelName: String,
    val channelAsset: String,
    val views: String,
    val daysSinceUploaded: String,
    val subscribersCount: String,
)
