package com.google.youtube.data

import com.google.youtube.models.ShortThumbnailDetails
import com.google.youtube.models.VideoThumbnailDetails

class FeedProvider {
    fun getMissedFeed(): List<VideoThumbnailDetails> {
        return IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
            details.id == "r0ju95BXZGW" ||
                    details.id == "p3va17HYCEF" ||
                    details.id == "s6lb42MNOPQ" ||
                    details.id == "g8df39ASDFJ"
        }
    }

    fun getNormalFeed(): List<VideoThumbnailDetails> {
        return IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
            details.id == "m9kd28VOTLK" ||
                    details.id == "b1wq73JUIOP" ||
                    details.id == "z5cn61WZXCV" ||
                    details.id == "a2fv50QWERA" ||
                    details.id == "h7pe86NBVCX" ||
                    details.id == "j4yi92LKJHG"
        }
    }

    fun getRecentWatchSuggestedFeed(): List<VideoThumbnailDetails> {
        return IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
            details.id == "x0gc74PYUIO" ||
                    details.id == "n5rt21KLMNO" ||
                    details.id == "c4dh98ZXCVB" ||
                    details.id == "f6bq35CVBNM"
        }
    }

    fun getShortsFeed(): List<ShortThumbnailDetails> {
        return List(3) {
            IN_MEMORY_SHORTS_DETAILS.filter { details ->
                details.id == "l7wk69TREWQ" ||
                        details.id == "y1sp52UIOPL"
            }
        }.flatten()
    }
}
