package com.google.youtube.data

import com.google.youtube.components.widgets.notification.NotificationListItemData
import com.google.youtube.components.widgets.notification.NotificationListItemType
import com.google.youtube.components.widgets.notification.NotificationListItemVideoType
import com.google.youtube.utils.Asset

class NotificationDataProvider {
    fun getNotificationsData(): List<NotificationListItemData> {
        val video = IN_MEMORY_THUMBNAIL_DETAILS.first { it.id == "m9kd28VOTLK" }
        val short = IN_MEMORY_SHORTS_DETAILS.first { it.id == "l7wk69TREWQ" }
        return listOf(
            NotificationListItemData(
                id = video.id,
                sharerChannelAsset = video.channelAsset ?: Asset.Channel.JUXTOPPOSED,
                sharerChannelUsername = video.channelName ?: "Juxtopposed",
                title = video.title,
                notificationType = NotificationListItemType.Upload(timeSinceUpload = video.daysSinceUploaded),
                thumbnailAsset = video.thumbnailAsset,
                videoType = NotificationListItemVideoType.Video(video.id),
            ),
            NotificationListItemData(
                id = short.id,
                sharerChannelAsset = Asset.Avatar.BLUE,
                sharerChannelUsername = "YouTubeEnjoyer",
                title = short.title,
                notificationType = NotificationListItemType.Share(sharedVideoChannelUsername = short.channelName),
                thumbnailAsset = short.thumbnailAsset,
                videoType = NotificationListItemVideoType.Short(short.id),
            ),
        )
    }
}
