package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
data class VideoDetails(
    val id: String,
    val title: String,
    val channelId: String,
    val channelName: String,
    val channelAsset: String,
    val subscribersCount: String,
    val viewCount: String,
    val uploadDate: String,
    val likeCount: String,
    val dislikeCount: String,
    val description: String,
)
