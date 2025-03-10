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
        return List(5) {
            IN_MEMORY_SHORTS_DETAILS.filter { details ->
                details.id == "l7wk69TREWQ" ||
                        details.id == "y1sp52UIOPL"
            }
        }.flatten()
    }

    fun getTrendingNowFeed(): List<VideoThumbnailDetails> {
        return List(2) {
            IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                details.id == "z2mk17WERTDF" ||
                        details.id == "p9cj53QYUIOP" ||
                        details.id == "b4hv86ASDFGH" ||
                        details.id == "m1rg20ZXCVBN" ||
                        details.id == "x7fq94LKJHGF" ||
                        details.id == "s6wd38MNBVCX" ||
                        details.id == "g3yn71POIUYT" ||
                        details.id == "a5le65HGFDSA"
            }
        }.flatten()
    }

    fun getNewMusicFeed(): List<VideoThumbnailDetails> {
        return List(2) {
            IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                details.id == "v8uz49NBVCXS" ||
                        details.id == "q1ij72TREWQA" ||
                        details.id == "f5ok36YUIOPL" ||
                        details.id == "c3pn81LKJHGF"
            }
        }.flatten()
    }

    fun getGamingFeed(): List<VideoThumbnailDetails> {
        return List(2) {
            IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
                details.id == "l6ey25ASDFGHJ" ||
                        details.id == "n7da60QWERTYU" ||
                        details.id == "j2wt98HJKLZXC" ||
                        details.id == "d4sg57MNBVCXZ"
            }
        }.flatten()
    }
}
