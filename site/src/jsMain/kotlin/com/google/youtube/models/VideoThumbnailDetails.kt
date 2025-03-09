package com.google.youtube.models

import androidx.compose.runtime.Immutable
import com.google.youtube.utils.Asset

// TODO: Use appropriate return types
@Immutable
data class VideoThumbnailDetails(
    val id: String,
    val thumbnailAsset: String,
    val channelAsset: String? = null,
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
) {
    fun toChannelListItemThumbnailDetails(): ChannelListItemData.Thumbnail {
        return ChannelListItemData.Thumbnail(
            id = id,
            channelAsset = channelAsset ?: Asset.Channel.JUXTOPPOSED,
            channelName = channelName,
            daysSinceUploaded = daysSinceUploaded,
            isChannelVerified = isVerified,
            subscribersCount = subscribersCount ?: "10K",
            thumbnailAsset = thumbnailAsset,
            videoDuration = duration,
            videoTitle = title,
            viewCount = views,
        )
    }
}
