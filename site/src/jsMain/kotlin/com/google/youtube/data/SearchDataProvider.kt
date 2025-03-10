package com.google.youtube.data

import com.google.youtube.models.VideoThumbnailDetails

class SearchDataProvider {
    fun getSearchedVideosForQuery(@Suppress("UNUSED_PARAMETER") query: String): List<VideoThumbnailDetails> {
        return IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
            details.id == "q6hv43QWERTYUI" ||
                    details.id == "n9zc76ZXCVBNA" ||
                    details.id == "w3pe20ASDFGHJK" ||
                    details.id == "b1mk59HJKLZXCV"
        }
    }
}
