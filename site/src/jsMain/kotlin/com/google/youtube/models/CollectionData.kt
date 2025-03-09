package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
data class CollectionData(
    val id: String,
    val name: String,
    val channelItems: List<CollectionChannelItem>,
)

@Immutable
data class CollectionChannelItem(
    val channelId: String,
    val avatarAsset: String,
    val channelName: String,
    val isVerified: Boolean,
    val subscribersCount: String,
    val isSubscribed: Boolean,
)
