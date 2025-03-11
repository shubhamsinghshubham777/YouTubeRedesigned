package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
data class ChannelBriefPopupData(
    val assetRef: String,
    val commentsCount: Int,
    val id: String,
    val isVerified: Boolean,
    val joinedSince: String,
    val name: String,
    val subscribersCount: String,
)
