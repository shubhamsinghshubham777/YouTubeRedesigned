package com.google.youtube.pages

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.components.widgets.VideoThumbnailCard
import com.google.youtube.components.widgets.context_menu.RoundedSearchTextField
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.utils.Assets
import com.google.youtube.utils.BasicGrid
import com.google.youtube.utils.Constants
import com.google.youtube.utils.GridGap
import com.google.youtube.utils.Styles
import com.google.youtube.utils.hideScrollBar
import com.google.youtube.utils.isGreaterThan
import com.google.youtube.utils.noShrink
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.borderLeft
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.div
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.times
import org.jetbrains.compose.web.css.unaryMinus
import org.jetbrains.compose.web.dom.Text

@Composable
fun HistoryPage() {
    val breakpoint = rememberBreakpoint()
    val isLargeBreakpoint = remember(breakpoint) { breakpoint.isGreaterThan(Breakpoint.SM) }

    var selectedTypeFilter by remember { mutableStateOf(HistoryFilter.Videos) }
    var selectedSortFilter by remember { mutableStateOf(HistoryFilter.Newest) }
    var selectedLayoutFilter by remember { mutableStateOf(HistoryFilter.GridView) }
    val searchQueryState = remember { mutableStateOf("") }
    val animatedFilterPaddingFraction by animateFloatAsState(if (isLargeBreakpoint) 1f else 0.5f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fontSize(20.px).fontWeight(FontWeight.Medium)) { Text("History") }

        // Divider
        Box(
            modifier = Modifier
                .background(Styles.DIVIDER)
                .fillMaxWidth()
                .height(1.px)
                .margin(top = 18.px)
        )

        // Filters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .hideScrollBar()
                .margin(
                    top = 40.px * animatedFilterPaddingFraction,
                    bottom = 48.px * animatedFilterPaddingFraction,
                )
                .overflow { x(Overflow.Scroll) },
            horizontalArrangement = Arrangement.spacedBy(16.px),
        ) {
            Row(
                modifier = Modifier.noShrink(),
                horizontalArrangement = Arrangement.spacedBy(8.px),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                listOf(
                    HistoryFilter.Videos,
                    HistoryFilter.Posts,
                    HistoryFilter.Live,
                    HistoryFilter.Shorts,
                    HistoryFilter.Comments,
                    HistoryFilter.Divider,
                    HistoryFilter.Newest,
                    HistoryFilter.Oldest,
                    HistoryFilter.DateRange,
                    HistoryFilter.Divider,
                    HistoryFilter.GridView,
                    HistoryFilter.ListView,
                ).forEach { filter ->
                    if (filter == HistoryFilter.Divider) {
                        Box(
                            Modifier
                                .background(Styles.DIVIDER)
                                .height(28.px)
                                .margin(leftRight = 8.px)
                                .noShrink()
                                .width(1.29.px)
                        )
                    } else {
                        AssetSvgButton(
                            isDense = true,
                            id = "${filter.ordinal}_${filter.name}_filter",
                            onClick = {
                                when (filter) {
                                    HistoryFilter.Videos,
                                    HistoryFilter.Posts,
                                    HistoryFilter.Live,
                                    HistoryFilter.Shorts,
                                    HistoryFilter.Comments,
                                        -> selectedTypeFilter = filter

                                    HistoryFilter.Newest,
                                    HistoryFilter.Oldest,
                                    HistoryFilter.DateRange,
                                        -> selectedSortFilter = filter

                                    HistoryFilter.GridView,
                                    HistoryFilter.ListView,
                                        -> selectedLayoutFilter = filter

                                    else -> Unit
                                }
                            },
                            isSelected = selectedSortFilter == filter ||
                                    selectedTypeFilter == filter ||
                                    selectedLayoutFilter == filter,
                            text = when {
                                filter == HistoryFilter.DateRange -> "All"
                                !filter.iconOnly -> filter.name
                                else -> null
                            },
                            secondaryText = if (filter == HistoryFilter.DateRange) "Date Range:" else null,
                            type = AssetSvgButtonType.SelectableChip,
                            startIconPath = filter.iconPath,
                        )
                    }
                }
            }
            Spacer()
            RoundedSearchTextField(textState = searchQueryState, hintText = "Search history")
        }

        ThumbnailGrid(
            date = "Today - 15 Nov 2024",
            thumbnailDetails = List(3) {
                VideoThumbnailDetails(
                    thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                    channelAsset = Assets.Avatars.AVATAR_JACKSEPTICEYE,
                    title = "Honest Trailers - Shrek",
                    channelName = "Screen Junkies",
                    isVerified = true,
                    views = "6.3M",
                    daysSinceUploaded = "7 years",
                    duration = "12:07",
                )
            }
        )
        ThumbnailGrid(
            date = "Yesterday - 14 Nov 2024",
            thumbnailDetails = List(1) {
                VideoThumbnailDetails(
                    thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                    channelAsset = Assets.Avatars.AVATAR_JACKSEPTICEYE,
                    title = "Google - Year in Search 2024",
                    channelName = "Google",
                    isVerified = true,
                    views = "5.2M",
                    daysSinceUploaded = "1 day",
                    duration = "12:07",
                )
            }
        )
    }
}

private enum class HistoryFilter(
    val iconPath: String? = null,
    val iconOnly: Boolean = false,
) {
    Videos,
    Posts,
    Live,
    Shorts,
    Comments,
    Divider,
    Newest,
    Oldest,
    DateRange(iconPath = Assets.Paths.DATE_RANGE),
    GridView(iconPath = Assets.Paths.GRID, iconOnly = true),
    ListView(iconPath = Assets.Paths.LIST, iconOnly = true)
}

@Composable
private fun ThumbnailGrid(
    date: String,
    thumbnailDetails: List<VideoThumbnailDetails>,
) {
    val breakpoint = rememberBreakpoint()
    val thumbnailWidth = remember(breakpoint) {
        when {
            breakpoint.isGreaterThan(Breakpoint.SM) -> 341
            else -> Constants.MOBILE_MAX_AVAILABLE_WIDTH - CONTAINER_PADDING_LEFT
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(left = DOT_INDICATOR_SIZE / 3)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(21.px),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DotIndicator(
                modifier = Modifier.translateX(-DOT_INDICATOR_SIZE / 3),
                isActive = date.contains("Today")
            )
            Box { Text(date) }
        }

        BasicGrid(
            modifier = Modifier
                .borderLeft(3.px, LineStyle.Solid, Styles.DIVIDER)
                .fillMaxWidth()
                .padding(left = CONTAINER_PADDING_LEFT.px, top = 24.px, bottom = 72.px),
            gridGap = GridGap(x = 19.px),
            columnBuilder = { size(thumbnailWidth.px) },
        ) {
            thumbnailDetails.forEach { details ->
                VideoThumbnailCard(
                    thumbnailAsset = details.thumbnailAsset,
                    channelAsset = details.channelAsset,
                    title = details.title,
                    channelName = details.channelName,
                    isVerified = details.isVerified,
                    views = details.views,
                    daysSinceUploaded = details.daysSinceUploaded,
                    duration = details.duration,
                    size = IntSize(width = thumbnailWidth, height = 190),
                )
            }
        }
    }
}

@Composable
private fun DotIndicator(
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = Modifier
            .backgroundImage(Styles.Gradient.RED_TO_PINK)
            .clip(Circle())
            .size(DOT_INDICATOR_SIZE)
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.background(Styles.WHITE.copyf(alpha = 0.3f)).fillMaxSize())
        if (!isActive) Box(Modifier.background(Styles.SURFACE).clip(Circle()).size(6.px))
    }
}

private val DOT_INDICATOR_SIZE = 9.px
private const val CONTAINER_PADDING_LEFT = 21
