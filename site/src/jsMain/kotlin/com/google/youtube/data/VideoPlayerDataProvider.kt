package com.google.youtube.data

import com.google.youtube.models.SuggestionSectionData
import com.google.youtube.models.VideoCommentData
import com.google.youtube.models.VideoDetails
import com.google.youtube.utils.Asset
import com.google.youtube.utils.getCurrentDateFormatted

class VideoPlayerDataProvider {
    fun getSuggestionsForId(@Suppress("UNUSED_PARAMETER") id: String): List<SuggestionSectionData> {
        return listOf(
            SuggestionSectionData(
                title = "From Juxtopposed",
                videos = IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                    details.id == "o8zj46LKJHGF" ||
                            details.id == "u2qm13POIUYT" ||
                            details.id == "d9vn87ASDFGH"
                },
            ),
            SuggestionSectionData(
                title = "Related Videos",
                videos = IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                    details.id == "i3et90MNBVCX" ||
                            details.id == "w5ak28QAZXSW" ||
                            details.id == "q7rf51HJKLZX" ||
                            details.id == "e1up76POIRET" ||
                            details.id == "t8dg34CVBJNM" ||
                            details.id == "k4xc62LKJHGF" ||
                            details.id == "r9bh85ASDFGH"
                },
            ),
        )
    }

    fun getVideoDetailsForId(id: String): VideoDetails {
        val details = IN_MEMORY_THUMBNAIL_DETAILS.firstOrNull { it.id == id }
            ?: IN_MEMORY_THUMBNAIL_DETAILS.first()

        return VideoDetails(
            id = details.id,
            title = details.title,
            channelId = "juxtopposed", // TODO: Use real IDs (if possible)
            channelName = details.channelName,
            channelAsset = details.channelAsset ?: Asset.Channel.JUXTOPPOSED,
            subscribersCount = details.subscribersCount ?: "54K",
            viewCount = details.views,
            uploadDate = details.uploadDate ?: getCurrentDateFormatted(),
            likeCount = details.likeCount ?: "21K",
            dislikeCount = details.dislikeCount ?: "508",
            description = "I tried to redesign YouTube's UI and make it more user-friendly." +
                    "\n\nHope you enjoy!\n\nSubscribe to my YouTube channel for more " +
                    "updates.",
        )
    }

    fun getCommentsForId(@Suppress("UNUSED_PARAMETER") id: String): List<VideoCommentData> {
        return listOf(
            VideoCommentData(
                username = "@Juxtopposed",
                durationSinceUploaded = "1 day",
                message = "Soooo what did you think of this redesign?",
                likeCount = "5K",
                userAssetRef = Asset.Avatar.JUXTOPPOSED,
            ),
            VideoCommentData(
                username = "@YouTube Enjoyer",
                durationSinceUploaded = "1 day",
                message = "woah I like this redesign",
                likeCount = "10K",
                dislikeCount = "12",
                isHearted = true,
                userAssetRef = Asset.Avatar.BLUE,
                replies = listOf(
                    VideoCommentData(
                        username = "@CEOofDesign",
                        durationSinceUploaded = "1 day",
                        message = "I like it too",
                        likeCount = "1K",
                        userAssetRef = Asset.Avatar.CEO_OF_DESIGN,
                        replies = listOf(
                            VideoCommentData(
                                username = "@CEOofYouTube",
                                durationSinceUploaded = "1 day",
                                message = "same here",
                                likeCount = "1.5K",
                                userAssetRef = Asset.Avatar.CEO_OF_DESIGN,
                            ),
                        ),
                    ),
                    VideoCommentData(
                        username = "@TheRizzler",
                        durationSinceUploaded = "1 day",
                        message = "bro likes the redesign",
                        likeCount = "500",
                        dislikeCount = "3",
                        userAssetRef = Asset.Avatar.THE_RIZZLER,
                    ),
                ),
            ),
            VideoCommentData(
                username = "@PBJenjoyer",
                durationSinceUploaded = "1 day",
                message = "I like PB&J",
                likeCount = "15K",
                userAssetRef = Asset.Avatar.GREEN,
            ),
        )
    }
}
