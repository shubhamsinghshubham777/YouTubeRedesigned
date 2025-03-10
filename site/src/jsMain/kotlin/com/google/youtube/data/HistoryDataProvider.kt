package com.google.youtube.data

import com.google.youtube.models.ThumbnailGridData

class HistoryDataProvider {
    fun provideHistoryVideoDetails(): List<ThumbnailGridData> {
        return listOf(
            ThumbnailGridData(
                date = "Today - 9 March 2025",
                thumbnailDetails = IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                    details.id == "g4pc93HJKLZXC" ||
                            details.id == "a6tm27MNBVCXZ"
                },
            ),
            ThumbnailGridData(
                date = "Yesterday - 8 March 2025",
                thumbnailDetails = IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                    details.id == "f1vs50POIUYTR"
                },
            ),
        )
    }
}
