package com.google.youtube.data

import com.google.youtube.models.ChannelBriefPopupData
import com.google.youtube.models.ChannelListItemData
import com.google.youtube.models.VideoThumbnailDetails

class ChannelDataProvider {
    fun getMissedData(): List<ChannelListItemData> {
        return listOf(
            IN_MEMORY_POSTS
                .first { it.id == "d2jb68HJKLZXCV" }
                .copy(daysSinceUploaded = "1 day", isPinned = false),
        ).plus(
            IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                details.id == "p3va17HYCEF" ||
                        details.id == "s6lb42MNOPQ" ||
                        details.id == "g8df39ASDFJ"
            }.map { it.copy(channelAsset = null).toChannelListItemThumbnailDetails() }
        )
    }

    fun getPopularVideos(): List<ChannelListItemData.Thumbnail> {
        return IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
            details.id == "o8zj46LKJHGF" ||
                    details.id == "v9ke85QWERTYUI" ||
                    details.id == "o3mg46ZXCVBNA" ||
                    details.id == "h1rd71ASDFGHJK"
        }.map { it.copy(channelAsset = null).toChannelListItemThumbnailDetails() }
    }

    fun getLatestPosts(): List<ChannelListItemData.Post> {
        return IN_MEMORY_POSTS.filter { post ->
            post.id == "d2jb68HJKLZXCV" || post.id == "j7qh39MNBVCXZA" || post.id == "c5lz72POIUYTRE"
        }
    }

    fun getAllVideos(): List<VideoThumbnailDetails> {
        return IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
            details.id == "u2qm13POIUYT" ||
                    details.id == "p3va17HYCEF" ||
                    details.id == "e4ya29HJKLZXCV" ||
                    details.id == "u8wi63MNBVCXZA" ||
                    details.id == "s6lb42MNOPQ" ||
                    details.id == "r0ju95BXZGW" ||
                    details.id == "o8zj46LKJHGF" ||
                    details.id == "l6qb97POIUYTRE"
        }.map { it.copy(channelAsset = null) }
    }

    fun getAllPosts(): List<ChannelListItemData.Post> = IN_MEMORY_POSTS

    fun getChannelBriefForId(channelId: String): ChannelBriefPopupData {
        return IN_MEMORY_CHANNEL_DATA.find { it.id == channelId }
            ?: IN_MEMORY_CHANNEL_DATA.first()
    }
}
