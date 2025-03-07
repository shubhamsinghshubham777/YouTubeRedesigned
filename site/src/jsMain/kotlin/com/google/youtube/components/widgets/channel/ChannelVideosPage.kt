package com.google.youtube.components.widgets.channel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.pages.MainVideosGrid
import com.google.youtube.utils.Assets
import com.google.youtube.utils.GridGap
import com.google.youtube.utils.SpacedColumn
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import org.jetbrains.compose.web.css.px

@Composable
fun ChannelVideosPage() {
    val filterState = remember { mutableStateOf(VideosAndPostsFilter.Latest) }
    val layoutTypeState = remember { mutableStateOf(VideosAndPostsLayoutType.Grid) }

    SpacedColumn(spacePx = 28, modifier = Modifier.fillMaxWidth()) {
        VideosAndPostsFilters(filterState, layoutTypeState)
        MainVideosGrid(
            modifier = Modifier.fillMaxWidth(),
            videos = remember {
                List(20) { index ->
                    VideoThumbnailDetails(
                        id = index.toString(),
                        thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                        channelAsset = Assets.Icons.USER_AVATAR,
                        title = "How Websites Learned to Fit Everywhere",
                        channelName = "Juxtopposed",
                        isVerified = true,
                        views = "150K",
                        daysSinceUploaded = "4 months",
                        duration = "12:07",
                    )
                }
            },
            gridGap = GridGap(x = 17.px, y = 25.px),
            minWidth = 353.px,
        )
    }
}
