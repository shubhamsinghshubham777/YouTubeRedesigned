package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.google.youtube.components.widgets.FilterRow
import com.google.youtube.components.widgets.MissedVideosContainer
import com.google.youtube.components.widgets.VideoThumbnailCard
import com.google.youtube.utils.Assets
import com.google.youtube.utils.isGreaterThan
import com.varabyte.kobweb.compose.css.GridEntry
import com.varabyte.kobweb.compose.css.gridTemplateColumns
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
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
    val breakpoint = rememberBreakpoint()
    Column {
        FilterRow(showPersonalisedFeedDialogState = showPersonalisedFeedDialogState)
        Column(
            modifier = Modifier
                .padding(top = 27.px, right = horizontalPaddingState.value.px)
                .fillMaxWidth()
        ) {
            MissedVideosContainer()
            Div({
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
                    property("grid-gap", 20.px)
                }
            }) {
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
    }
}
