package com.google.youtube.models

data class VideoComment(
    val commentId: String,
    val dislikeCount: String,
    val isHearted: Boolean,
    val likeCount: String,
    val message: String,
    val replies: List<VideoComment>,
    val timestamp: String,
    val userId: String,
)
