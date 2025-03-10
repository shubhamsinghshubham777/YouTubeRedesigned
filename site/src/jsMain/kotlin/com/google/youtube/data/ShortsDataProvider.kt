package com.google.youtube.data

import com.google.youtube.models.ShortThumbnailDetails

class ShortsDataProvider {
    fun provideAllShorts(): List<ShortThumbnailDetails> {
        return List(10) { IN_MEMORY_SHORTS_DETAILS }.flatten()
    }

    fun findShortById(id: String): ShortThumbnailDetails {
        return IN_MEMORY_SHORTS_DETAILS.find { it.id == id } ?: IN_MEMORY_SHORTS_DETAILS.first()
    }

    fun getShortSuggestionsForId(id: String): List<ShortThumbnailDetails> {
        return IN_MEMORY_SHORTS_DETAILS.filterNot { it.id == id }.plus(IN_MEMORY_SHORTS_DETAILS)
    }
}
