package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
data class PlaylistItemData(
    val id: String,
    val name: String,
    val channelName: String?,
    val channelAsset: String,
    val thumbnailImageRef: String,
    val isChannelVerified: Boolean,
    val subscriberCount: String,
    val viewsCount: String,
    val videosCount: Int,
    val totalDuration: String,
)
