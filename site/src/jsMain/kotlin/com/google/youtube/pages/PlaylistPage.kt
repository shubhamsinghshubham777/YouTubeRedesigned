package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.youtube.components.widgets.PlaylistListItem
import com.google.youtube.models.PlaylistItemData
import com.google.youtube.utils.Assets
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.Styles
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

@Composable
fun PlaylistPage(id: String) {
    SpacedColumn(spacePx = 32, modifier = Modifier.fillMaxWidth()) {
        // Bordered container
        Box(
            modifier = Modifier
                .border(1.px, LineStyle.Solid, Styles.WHITE.copyf(alpha = 0.14f))
                .borderRadius(24.px)
                .fillMaxWidth()
                .padding(24.px)
        ) {
            PlaylistListItem(
                data = remember { getSamplePlaylistPage(id) },
                showThumbnailColorPalette = false,
                isEditable = true,
            )
        }
    }
}

private fun getSamplePlaylistPage(id: String) = PlaylistItemData(
    id = id,
    name = "I Redesigned the ENTIRE YouTube UI from Scratch",
    channelName = "Juxtopposed",
    thumbnailImageRef = Assets.Thumbnails.THUMBNAIL_1,
    channelImageRef = Assets.Icons.USER_AVATAR,
    isChannelVerified = true,
    subscriberCount = "295K",
    viewsCount = 50,
    videosCount = 6,
    totalDuration = "2:51:23",
)
