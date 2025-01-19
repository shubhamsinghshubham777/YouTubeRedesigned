package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize
import com.google.youtube.components.widgets.ColoredBorderContainer
import com.google.youtube.components.widgets.FilterRow
import com.google.youtube.components.widgets.MissedVideosContainer
import com.google.youtube.components.widgets.ShortThumbnailCard
import com.google.youtube.components.widgets.ShortThumbnailCardDefaults
import com.google.youtube.components.widgets.VideoThumbnailCard
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Styles
import com.google.youtube.utils.isGreaterThan
import com.varabyte.kobweb.compose.css.GridEntry
import com.varabyte.kobweb.compose.css.gridTemplateColumns
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div

@Composable
fun HomePage(
    showPersonalisedFeedDialogState: MutableState<Boolean>,
    horizontalPaddingState: State<Float>,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        FilterRow(showPersonalisedFeedDialogState = showPersonalisedFeedDialogState)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.px, right = horizontalPaddingState.value.px, bottom = 27.px),
        ) {
            MissedVideosContainer(Modifier.margin(bottom = HomePageDefaults.SPACE_BETWEEN_CONTENT))
            MainVideosGrid()
            RecentWatchSuggestions(Modifier.margin(bottom = HomePageDefaults.SPACE_BETWEEN_CONTENT))
            ShortSuggestions()
        }
    }
}

object HomePageDefaults {
    val SPACE_BETWEEN_CONTENT = 40.px
}

@Composable
private fun ShortSuggestions(modifier: Modifier = Modifier) {
    var display by remember { mutableStateOf(true) }
    AnimatedVisibility(modifier = Modifier.fillMaxWidth(), isVisible = display) {
        ColoredBorderContainer(
            modifier = modifier,
            title = "Shorts",
            onNegativeCTA = { display = false },
            onPositiveCTA = {},
            color = Styles.BLUE_BORDER,
            asset = Assets.Icons.SHORTS_SELECTED,
            scrollPixels = ShortThumbnailCardDefaults.SIZE.width.toDouble(),
        ) {
            repeat(10) {
                ShortThumbnailCard(
                    thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                    channelName = "DailyDoseOfInternet",
                    title = "Put this cat in jail",
                    views = "10M",
                    daysSinceUploaded = "3weeks"
                )
            }
        }
    }
}

@Composable
private fun RecentWatchSuggestions(modifier: Modifier = Modifier) {
    var display by remember { mutableStateOf(true) }
    AnimatedVisibility(modifier = Modifier.fillMaxWidth(), isVisible = display) {
        ColoredBorderContainer(
            modifier = modifier,
            title = "Suggestions based on recent watches",
            onNegativeCTA = { display = false },
            onPositiveCTA = {},
            color = Styles.PURPLE_BORDER,
            contentGapPx = 30,
            scrollPixels = SUGGESTION_THUMBNAIL_SIZE.width.toDouble(),
        ) {
            repeat(4) {
                VideoThumbnailCard(
                    thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                    channelAsset = Assets.Icons.USER_AVATAR,
                    title = "How Websites Learned to Fit Everywhere",
                    views = "150K",
                    daysSinceUploaded = "4 months ago",
                    duration = "12:07",
                    size = SUGGESTION_THUMBNAIL_SIZE
                )
            }
        }
    }
}

@Composable
private fun MainVideosGrid(modifier: Modifier = Modifier) {
    val breakpoint = rememberBreakpoint()
    Div(
        modifier.toAttrs {
            style {
                display(DisplayStyle.Grid)
                width(100.percent)
                gridTemplateColumns {
                    repeat(GridEntry.Repeat.Auto.Type.AutoFit) {
                        minmax(
                            min = if (breakpoint.isGreaterThan(Breakpoint.MD)) 380.px else 320.px,
                            max = 1.fr
                        )
                    }
                }
                property("grid-gap", "20px")
            }
        }
    ) {
        repeat(20) {
            VideoThumbnailCard(
                modifier = Modifier.margin(bottom = 45.px),
                thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                channelAsset = Assets.Icons.USER_AVATAR,
                title = "How Websites Learned to Fit Everywhere",
                views = "150K",
                daysSinceUploaded = "4 months ago",
                duration = "12:07",
            )
        }
    }
}

private val SUGGESTION_THUMBNAIL_SIZE = IntSize(width = 332, height = 186)
