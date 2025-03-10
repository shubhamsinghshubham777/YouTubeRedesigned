package com.google.youtube.components.widgets.channel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.data.ChannelDataProvider
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.pages.MainVideosGrid
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.DescendingStringDurationComparator
import com.google.youtube.utils.GridGap
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.StringDurationComparator
import com.google.youtube.utils.StringViewCountComparator
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import org.jetbrains.compose.web.css.px

@Composable
fun ChannelVideosPage() {
    val filterState = remember { mutableStateOf(VideosAndPostsFilter.Latest) }
    val layoutTypeState = remember { mutableStateOf(VideosAndPostsLayoutType.Grid) }
    val channelDataProvider = remember { ChannelDataProvider() }
    val videos = remember(channelDataProvider) { channelDataProvider.getAllVideos() }

    SpacedColumn(spacePx = 28, modifier = Modifier.fillMaxWidth()) {
        VideosAndPostsFilters(filterState, layoutTypeState)
        Crossfade(
            targetState = filterState.value,
            modifier = Modifier.fillMaxWidth(),
        ) { animatedFilterState ->
            val sortedVideos = when (animatedFilterState) {
                VideosAndPostsFilter.Latest -> videos.sortedWith(VideoDurationComparator)
                VideosAndPostsFilter.Popular -> videos.sortedWith(VideoViewCountComparator)
                VideosAndPostsFilter.Oldest -> videos.sortedWith(DescendingVideoDurationComparator)
            }
            MainVideosGrid(
                modifier = Modifier.fillMaxWidth(),
                videos = sortedVideos,
                gridGap = GridGap(x = 17.px, y = 25.px),
                minWidth = 353.px,
            )
        }
    }
}

private object VideoDurationComparator : Comparator<VideoThumbnailDetails> {
    override fun compare(a: VideoThumbnailDetails, b: VideoThumbnailDetails): Int {
        return StringDurationComparator.compare(a.daysSinceUploaded, b.daysSinceUploaded)
    }
}

private object DescendingVideoDurationComparator : Comparator<VideoThumbnailDetails> {
    override fun compare(a: VideoThumbnailDetails, b: VideoThumbnailDetails): Int {
        return DescendingStringDurationComparator.compare(a.daysSinceUploaded, b.daysSinceUploaded)
    }
}

private object VideoViewCountComparator : Comparator<VideoThumbnailDetails> {
    override fun compare(a: VideoThumbnailDetails, b: VideoThumbnailDetails): Int {
        return StringViewCountComparator.compare(a.views, b.views)
    }
}
