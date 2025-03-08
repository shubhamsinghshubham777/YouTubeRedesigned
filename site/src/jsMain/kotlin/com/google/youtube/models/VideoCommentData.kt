package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
data class VideoCommentData(
    val dislikeCount: String? = null,
    val isHearted: Boolean = false,
    val likeCount: String,
    val message: String,
    val replies: List<VideoCommentData> = emptyList(),
    val durationSinceUploaded: String,
    val username: String,
    val userAssetRef: String,
)
