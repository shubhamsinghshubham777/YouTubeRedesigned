package com.google.youtube.pages

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.components.widgets.ThumbnailGrid
import com.google.youtube.components.widgets.context.RoundedSearchTextField
import com.google.youtube.models.ThumbnailGridData
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.utils.Asset
import com.google.youtube.utils.Constants
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
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.times
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

        // TODO: Use `Wrap` composable to make this view more responsive
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
                                .height(Constants.VERTICAL_DIVIDER_SIZE.height.px)
                                .margin(leftRight = 8.px)
                                .noShrink()
                                .width(Constants.VERTICAL_DIVIDER_SIZE.width.px)
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
            data = ThumbnailGridData(
                date = "Today - 15 Nov 2024",
                thumbnailDetails = List(3) { index ->
                    VideoThumbnailDetails(
                        id = index.toString(),
                        thumbnailAsset = Asset.Thumbnails.THUMBNAIL_1,
                        channelAsset = Asset.Avatar.JACKSEPTICEYE,
                        title = "Honest Trailers - Shrek",
                        channelName = "Screen Junkies",
                        isVerified = true,
                        views = "6.3M",
                        daysSinceUploaded = "7 years",
                        duration = "12:07",
                    )
                }
            )
        )
        ThumbnailGrid(
            data = ThumbnailGridData(
                date = "Yesterday - 14 Nov 2024",
                thumbnailDetails = List(1) { index ->
                    VideoThumbnailDetails(
                        id = index.toString(),
                        thumbnailAsset = Asset.Thumbnails.THUMBNAIL_1,
                        channelAsset = Asset.Avatar.JACKSEPTICEYE,
                        title = "Google - Year in Search 2024",
                        channelName = "Google",
                        isVerified = true,
                        views = "5.2M",
                        daysSinceUploaded = "1 day",
                        duration = "12:07",
                    )
                }
            )
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
    DateRange(iconPath = Asset.Path.DATE_RANGE),
    GridView(iconPath = Asset.Path.GRID, iconOnly = true),
    ListView(iconPath = Asset.Path.LIST, iconOnly = true)
}
