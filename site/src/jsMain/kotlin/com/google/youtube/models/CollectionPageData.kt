package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
data class CollectionPageData(val name: String, val videos: List<VideoThumbnailDetails>)
