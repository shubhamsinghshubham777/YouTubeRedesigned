package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
sealed class ChannelListItemData(
    open val channelAsset: String,
    open val channelName: String,
    open val daysAgo: String,
    open val isChannelVerified: Boolean,
    open val subscribersCount: String,
) {
    @Immutable
    data class Thumbnail(
        override val channelAsset: String,
        override val channelName: String,
        override val daysAgo: String,
        override val isChannelVerified: Boolean,
        override val subscribersCount: String,
        val thumbnailAsset: String,
        val videoDuration: String,
        val videoTitle: String,
        val viewCount: String,
    ) : ChannelListItemData(
        channelAsset = channelAsset,
        channelName = channelName,
        daysAgo = daysAgo,
        isChannelVerified = isChannelVerified,
        subscribersCount = subscribersCount,
    )

    @Immutable
    data class Post(
        override val channelAsset: String,
        override val channelName: String,
        override val daysAgo: String,
        override val isChannelVerified: Boolean,
        override val subscribersCount: String,
        val commentCount: String,
        val dislikeCount: String,
        val likeCount: String,
        val message: String,
    ) : ChannelListItemData(
        channelAsset = channelAsset,
        channelName = channelName,
        daysAgo = daysAgo,
        isChannelVerified = isChannelVerified,
        subscribersCount = subscribersCount,
    )
}
