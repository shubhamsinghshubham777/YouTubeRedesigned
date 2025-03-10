package com.google.youtube.components.widgets.channel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.data.ChannelDataProvider
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.pages.MainVideosGrid
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.GridGap
import com.google.youtube.utils.SpacedColumn
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
                VideosAndPostsFilter.Latest -> videos.sortedWith(DurationComparator)
                VideosAndPostsFilter.Popular -> videos.sortedWith(ViewCountComparator)
                VideosAndPostsFilter.Oldest -> videos.sortedWith(DescendingDurationComparator)
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

private object DurationComparator : Comparator<VideoThumbnailDetails> {
    private const val DAYS_IN_YEAR = 365 // Approximate, for simplicity
    private const val DAYS_IN_MONTH = 30  // Approximate, for simplicity
    private const val DAYS_IN_WEEK = 7

    override fun compare(a: VideoThumbnailDetails, b: VideoThumbnailDetails): Int {
        val duration1Days = getDurationInDays(a.daysSinceUploaded)
        val duration2Days = getDurationInDays(b.daysSinceUploaded)

        return duration1Days.compareTo(duration2Days)
    }

    private fun getDurationInDays(durationStr: String): Long {
        val parts = durationStr.split("\\s+".toRegex())
        if (parts.size != 2) {
            throw IllegalArgumentException("Invalid duration format: $durationStr")
        }

        val value = parts[0].toInt()

        // Case-insensitive unit matching
        return when (val unit = parts[1].lowercase()) {
            "year", "years" -> value * DAYS_IN_YEAR.toLong()
            "month", "months" -> value * DAYS_IN_MONTH.toLong()
            "week", "weeks" -> value * DAYS_IN_WEEK.toLong()
            "day", "days" -> value.toLong()
            else -> throw IllegalArgumentException("Unsupported duration unit: $unit")
        }
    }
}

private object DescendingDurationComparator : Comparator<VideoThumbnailDetails> {
    override fun compare(a: VideoThumbnailDetails, b: VideoThumbnailDetails): Int {
        // Reverse the result of the ascending comparator
        return DurationComparator.compare(b, a)
    }
}

private object ViewCountComparator : Comparator<VideoThumbnailDetails> {
    override fun compare(a: VideoThumbnailDetails, b: VideoThumbnailDetails): Int {
        val value1 = getViewCountValue(b.views)
        val value2 = getViewCountValue(a.views)
        return value1.compareTo(value2)
    }

    private fun getViewCountValue(viewStr: String): Double {
        val cleanViewStr = viewStr.trim()
        val suffix = cleanViewStr.takeLast(1).uppercase()
        val numberPart = cleanViewStr.dropLast(1)

        return when (suffix) {
            "K" -> parseNumber(numberPart) * 1000
            "M" -> parseNumber(numberPart) * 1000000
            else -> parseNumber(cleanViewStr) // No suffix, parse the whole string
        }
    }

    private fun parseNumber(numberStr: String): Double {
        return try {
            numberStr.toDouble()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid view count format: $numberStr", e)
        }
    }
}
