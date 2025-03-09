package com.google.youtube.utils

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.IntSize
import org.jetbrains.compose.web.css.px

object Constants {
    const val MOBILE_MAX_AVAILABLE_WIDTH = 320
    val CONTENT_PADDING = 12.px
    val SUGGESTION_THUMBNAIL_SIZE = IntSize(width = 332, height = 186)
    val VERTICAL_DIVIDER_SIZE = Size(width = 1.29f, height = 28f)
    const val POPUP_SHOW_DELAY_MS = 500
}
