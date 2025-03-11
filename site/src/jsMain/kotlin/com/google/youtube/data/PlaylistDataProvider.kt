package com.google.youtube.data

import com.google.youtube.models.PlaylistItemData
import com.google.youtube.models.VideoThumbnailDetails

class PlaylistDataProvider {
    fun getAllPlaylists(): List<PlaylistItemData> = IN_MEMORY_PLAYLISTS

    fun getPlaylistForId(id: String): PlaylistItemData {
        return IN_MEMORY_PLAYLISTS.find { it.id == id } ?: IN_MEMORY_PLAYLISTS.first()
    }

    fun getVideosForPlaylistWithId(@Suppress("UNUSED_PARAMETER") id: String): List<VideoThumbnailDetails> {
        return IN_MEMORY_THUMBNAIL_DETAILS.filter { details ->
            details.id == "m9kd28VOTLK" ||
                    details.id == "p3va17HYCEF" ||
                    details.id == "u2qm13POIUYT" ||
                    details.id == "o8zj46LKJHGF"
        }
    }
}
