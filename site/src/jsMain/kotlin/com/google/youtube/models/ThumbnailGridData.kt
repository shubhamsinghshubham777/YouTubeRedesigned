package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
data class ThumbnailGridData(val date: String, val thumbnailDetails: List<VideoThumbnailDetails>)
