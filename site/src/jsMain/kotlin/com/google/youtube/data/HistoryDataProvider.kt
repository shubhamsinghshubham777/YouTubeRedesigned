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
            ThumbnailGridData(
                date = "7 March 2025",
                thumbnailDetails = IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                    details.id == "r0ju95BXZGW" ||
                            details.id == "p3va17HYCEF" ||
                            details.id == "s6lb42MNOPQ"
                },
            ),
            ThumbnailGridData(
                date = "6 March 2025",
                thumbnailDetails = IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                    details.id == "g8df39ASDFJ" ||
                            details.id == "m9kd28VOTLK" ||
                            details.id == "b1wq73JUIOP" ||
                            details.id == "z5cn61WZXCV"
                },
            ),
        )
    }
}
