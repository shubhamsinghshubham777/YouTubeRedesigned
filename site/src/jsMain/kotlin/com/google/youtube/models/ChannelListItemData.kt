package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
sealed class ChannelListItemData(
    open val id: String,
    open val channelAsset: String?,
    open val channelName: String?,
    open val daysSinceUploaded: String,
    open val isChannelVerified: Boolean,
    open val subscribersCount: String,
) {
    @Immutable
    data class Thumbnail(
        override val id: String,
        override val channelAsset: String?,
        override val channelName: String?,
        override val daysSinceUploaded: String,
        override val isChannelVerified: Boolean,
        override val subscribersCount: String,
        val thumbnailAsset: String,
        val videoDuration: String,
        val videoTitle: String,
        val viewCount: String,
    ) : ChannelListItemData(
        id = id,
        channelAsset = channelAsset,
        channelName = channelName,
        daysSinceUploaded = daysSinceUploaded,
        isChannelVerified = isChannelVerified,
        subscribersCount = subscribersCount,
    ) {
        fun toThumbnailDetails(): VideoThumbnailDetails {
            return VideoThumbnailDetails(
                id = id,
                thumbnailAsset = thumbnailAsset,
                channelAsset = channelAsset,
                title = videoTitle,
                channelName = channelName,
                isVerified = isChannelVerified,
                views = viewCount,
                daysSinceUploaded = daysSinceUploaded,
                duration = videoDuration,
                subscribersCount = subscribersCount,
            )
        }
    }

    @Immutable
    data class Post(
        override val id: String,
        override val channelAsset: String?,
        override val channelName: String?,
        override val daysSinceUploaded: String,
        override val isChannelVerified: Boolean,
        override val subscribersCount: String,
        val commentCount: String,
        val dislikeCount: String?,
        val likeCount: String,
        val message: String,
        val postAsset: String? = null,
        val isPinned: Boolean = false,
    ) : ChannelListItemData(
        id = id,
        channelAsset = channelAsset,
        channelName = channelName,
        daysSinceUploaded = daysSinceUploaded,
        isChannelVerified = isChannelVerified,
        subscribersCount = subscribersCount,
    )
}
