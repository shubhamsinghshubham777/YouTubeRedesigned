package com.google.youtube.data

import com.google.youtube.models.ChannelListItemData
import com.google.youtube.models.ThumbnailGridData
import com.google.youtube.utils.Asset

class SubscriptionsDataProvider {
    fun getDataByTimeline(): List<ThumbnailGridData> {
        return listOf(
            ThumbnailGridData(
                date = "Today - 9 Mar 2025",
                thumbnailDetails = IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                    details.id == "m9kd28VOTLK" ||
                            details.id == "y9br63ASDFGHJ" ||
                            details.id == "h2lk95QWERTYU" ||
                            details.id == "o4vx28HJKLZXCV"
                },
            ),
            ThumbnailGridData(
                date = "Yesterday - 8 Mar 2025",
                thumbnailDetails = IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                    details.id == "w6cm71MNBVCXZA" ||
                            details.id == "e8qz34POIUYTRE" ||
                            details.id == "k1fj50LKJHGFDS" ||
                            details.id == "r7dt89ZXCVBNMA"
                },
            ),
            ThumbnailGridData(
                date = "7 Mar 2025",
                thumbnailDetails = IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                    details.id == "m9kd28VOTLK" ||
                            details.id == "y9br63ASDFGHJ" ||
                            details.id == "h2lk95QWERTYU" ||
                            details.id == "o4vx28HJKLZXCV"
                },
            ),
        )
    }

    fun getDataByChannel(): List<List<ChannelListItemData>> {
        return listOf(
            listOf(
                IN_MEMORY_THUMBNAIL_DETAILS
                    .first { it.id == "m9kd28VOTLK" }
                    .toChannelListItemThumbnailDetails(),
                FAKE_CHANNEL_POST,
                IN_MEMORY_THUMBNAIL_DETAILS
                    .first { it.id == "u2qm13POIUYT" }
                    .toChannelListItemThumbnailDetails(),
                IN_MEMORY_THUMBNAIL_DETAILS
                    .first { it.id == "p3va17HYCEF" }
                    .toChannelListItemThumbnailDetails(),
            ),
            listOf(
                IN_MEMORY_THUMBNAIL_DETAILS
                    .first { it.id == "w5ak28QAZXSW" }
                    .toChannelListItemThumbnailDetails(),
                FAKE_CHANNEL_POST,
                IN_MEMORY_THUMBNAIL_DETAILS
                    .first { it.id == "y9br63ASDFGHJ" }
                    .toChannelListItemThumbnailDetails(),
            ),
            listOf(
                IN_MEMORY_THUMBNAIL_DETAILS
                    .first { it.id == "t8dg34CVBJNM" }
                    .toChannelListItemThumbnailDetails(),
                FAKE_CHANNEL_POST,
                IN_MEMORY_THUMBNAIL_DETAILS
                    .first { it.id == "h2lk95QWERTYU" }
                    .toChannelListItemThumbnailDetails(),
            ),
        )
    }
}

// TODO: Get this data from the data layer instead
private val FAKE_CHANNEL_POST = ChannelListItemData.Post(
    id = "fake_post_id",
    channelAsset = Asset.Channel.JUXTOPPOSED,
    channelName = "juxtopposed",
    daysSinceUploaded = "1 day",
    isChannelVerified = true,
    subscribersCount = "",
    commentCount = "1.1K",
    dislikeCount = "12",
    likeCount = "2.9K",
    message = "it’s finally time for youtube. what are your biggest issues with it?",
    isPinned = false,
)
