package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.ColoredBorderContainer
import com.google.youtube.components.widgets.FilterRow
import com.google.youtube.components.widgets.MissedVideosContainer
import com.google.youtube.components.widgets.ShortThumbnailCard
import com.google.youtube.components.widgets.ShortThumbnailCardDefaults
import com.google.youtube.components.widgets.VideoThumbnailCard
import com.google.youtube.data.FeedProvider
import com.google.youtube.models.ShortThumbnailDetails
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Asset
import com.google.youtube.utils.BasicGrid
import com.google.youtube.utils.Constants
import com.google.youtube.utils.GridGap
import com.google.youtube.utils.Styles
import com.google.youtube.utils.isGreaterThan
import com.google.youtube.utils.rememberBreakpointAsState
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px

@Composable
fun HomePage(showPersonalisedFeedDialogState: MutableState<Boolean>) {
    val feedProvider = remember { FeedProvider() }

    Column(modifier = Modifier.fillMaxWidth()) {
        FilterRow(showPersonalisedFeedDialogState = showPersonalisedFeedDialogState)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.px, bottom = 27.px),
        ) {
            MissedVideosContainer(
                modifier = Modifier.margin(bottom = HomePageDefaults.SPACE_BETWEEN_CONTENT),
                videos = remember(feedProvider) { feedProvider.getMissedFeed() },
            )
            MainVideosGrid(videos = remember(feedProvider) { feedProvider.getNormalFeed() })
            RecentWatchSuggestions(
                modifier = Modifier.margin(bottom = HomePageDefaults.SPACE_BETWEEN_CONTENT),
                videos = remember(feedProvider) { feedProvider.getRecentWatchSuggestedFeed() },
            )
            ShortSuggestions(shorts = remember(feedProvider) { feedProvider.getShortsFeed() })
        }
    }
}

object HomePageDefaults {
    val SPACE_BETWEEN_CONTENT = 40.px
}

@Composable
private fun ShortSuggestions(modifier: Modifier = Modifier, shorts: List<ShortThumbnailDetails>) {
    var display by remember { mutableStateOf(true) }
    AnimatedVisibility(modifier = Modifier.fillMaxWidth(), isVisible = display) {
        ColoredBorderContainer(
            modifier = modifier,
            title = "Shorts",
            onNegativeCTA = { display = false },
            onPositiveCTA = {},
            color = Styles.BLUE_BORDER,
            asset = Asset.Icon.SHORTS_SELECTED,
            scrollPixels = ShortThumbnailCardDefaults.SIZE.width.toDouble(),
        ) {
            shorts.forEach { short -> ShortThumbnailCard(short) }
        }
    }
}

@Composable
private fun RecentWatchSuggestions(
    modifier: Modifier = Modifier,
    videos: List<VideoThumbnailDetails>,
) {
    var display by remember { mutableStateOf(true) }
    AnimatedVisibility(modifier = Modifier.fillMaxWidth(), isVisible = display) {
        ColoredBorderContainer(
            modifier = modifier,
            title = "Suggestions based on recent watches",
            onNegativeCTA = { display = false },
            onPositiveCTA = {},
            color = Styles.PURPLE_BORDER,
            contentGapPx = 30,
            scrollPixels = Constants.SUGGESTION_THUMBNAIL_SIZE.width.toDouble(),
        ) {
            videos.forEach { details ->
                VideoThumbnailCard(
                    details = details,
                    size = Constants.SUGGESTION_THUMBNAIL_SIZE,
                )
            }
        }
    }
}

@Composable
fun MainVideosGrid(
    videos: List<VideoThumbnailDetails>,
    modifier: Modifier = Modifier,
    gridGap: GridGap = GridGap(20.px),
    minWidth: CSSLengthOrPercentageNumericValue = 380.px,
) {
    val breakpoint by rememberBreakpointAsState()
    BasicGrid(
        modifier = Modifier.fillMaxWidth().display(DisplayStyle.Grid).then(modifier),
        columnBuilder = {
            minmax(
                min = if (breakpoint.isGreaterThan(Breakpoint.MD)) minWidth
                else Constants.MOBILE_MAX_AVAILABLE_WIDTH.px,
                max = 1.fr
            )
        },
        gridGap = gridGap,
    ) {
        videos.forEach { details ->
            VideoThumbnailCard(
                details = details,
                // TODO: Remove this bottom margin & update all its usages
                modifier = Modifier.margin(bottom = 45.px),
            )
        }
    }
}
