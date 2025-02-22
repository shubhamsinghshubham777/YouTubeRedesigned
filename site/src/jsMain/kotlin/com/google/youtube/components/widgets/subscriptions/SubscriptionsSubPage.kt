package com.google.youtube.components.widgets.subscriptions

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.components.widgets.HorizontalDivider
import com.google.youtube.components.widgets.ThumbnailGrid
import com.google.youtube.components.widgets.VerticalDivider
import com.google.youtube.components.widgets.VideoThumbnailCard
import com.google.youtube.components.widgets.VideoThumbnailCardDefaults
import com.google.youtube.components.widgets.Wrap
import com.google.youtube.components.widgets.context_menu.RoundedSearchTextField
import com.google.youtube.models.ChannelListItemData
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.pages.ScrollableSpacedRow
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Composable
fun SubscriptionsSubPage() {
    val searchQueryState = remember { mutableStateOf("") }
    val viewAsTimeline = remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("All") }

    Column(modifier = Modifier.fillMaxWidth()) {
        Wrap(
            modifier = Modifier.fillMaxWidth().margin(top = 40.px),
            verticalGapPx = 8,
            horizontalGapPx = 8,
        ) {
            AssetSvgButton(
                endIconPath = Assets.Paths.ARROW_DOWN,
                id = "view_as_button",
                isDense = true,
                isSelected = false,
                onClick = { viewAsTimeline.value = !viewAsTimeline.value },
                secondaryText = "View as:",
                // TODO: Make a dropdown for this
                text = if (viewAsTimeline.value) "Timeline" else "Channel",
                type = AssetSvgButtonType.SelectableChip,
            )
            VerticalDivider()
            listOf("All", "Videos", "Posts", "Live", "Shorts").forEach { label ->
                AssetSvgButton(
                    id = "content_filter_type_${label}_button",
                    isDense = true,
                    isSelected = selectedFilter == label,
                    onClick = { selectedFilter = label },
                    text = label,
                    type = AssetSvgButtonType.SelectableChip,
                )
            }
            VerticalDivider()
            if (viewAsTimeline.value) {
                listOf("Newest", "Oldest", "Date Range").forEach { label ->
                    AssetSvgButton(
                        id = "sort_type_${label}_button",
                        isDense = true,
                        isSelected = false,
                        onClick = {},
                        text = if (label == "Date Range") "All" else label,
                        type = AssetSvgButtonType.SelectableChip,
                        startIconPath = if (label == "Date Range") Assets.Paths.DATE_RANGE else null,
                        secondaryText = if (label == "Date Range") label else null,
                    )
                }
            } else {
                AssetSvgButton(
                    endIconPath = Assets.Paths.ARROW_DOWN,
                    id = "sort_by_button",
                    isDense = true,
                    isSelected = false,
                    onClick = {},
                    secondaryText = "Sort by: ",
                    text = "Latest Activity",
                    type = AssetSvgButtonType.SelectableChip,
                )
            }
            VerticalDivider()
            AssetSvgButton(
                id = "layout_type_grid_button",
                isDense = true,
                isSelected = viewAsTimeline.value,
                onClick = { viewAsTimeline.value = true },
                type = AssetSvgButtonType.SelectableChip,
                startIconPath = Assets.Paths.GRID,
            )
            AssetSvgButton(
                id = "layout_type_list_button",
                isDense = true,
                isSelected = !viewAsTimeline.value,
                onClick = { viewAsTimeline.value = false },
                type = AssetSvgButtonType.SelectableChip,
                startIconPath = Assets.Paths.LIST,
            )
            Spacer()
            RoundedSearchTextField(textState = searchQueryState, hintText = "Search subscriptions")
        }
        Box(modifier = Modifier.height(47.px))
        Crossfade(
            targetState = viewAsTimeline.value,
            modifier = Modifier.fillMaxWidth(),
        ) { isTimelineMode ->
            if (isTimelineMode) {
                Column(modifier = Modifier.fillMaxWidth()) {
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
            } else {
                SpacedColumn(
                    spacePx = 40,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 40.px),
                ) {
                    repeat(3) { index ->
                        ChannelListItem(
                            data = listOf(
                                ChannelListItemData.Thumbnail(
                                    channelAsset = Assets.Icons.USER_AVATAR,
                                    channelName = "Juxtopposed",
                                    daysAgo = "1 day",
                                    isChannelVerified = true,
                                    subscribersCount = "295K",
                                    thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                                    videoDuration = "12:07",
                                    videoTitle = "I Redesigned the ENTIRE YouTube UI from Scratch",
                                    viewCount = "120K",
                                ),
                                ChannelListItemData.Post(
                                    channelAsset = Assets.Icons.USER_AVATAR,
                                    channelName = "Juxtopposed",
                                    daysAgo = "1 day",
                                    isChannelVerified = true,
                                    subscribersCount = "295K",
                                    commentCount = "1.1K",
                                    dislikeCount = "12",
                                    likeCount = "2.9K",
                                    message = "it’s finally time for youtube. what are your biggest issues with it?",
                                ),
                            ),
                            initialIsExpanded = index == 0,
                        )
                        if (index != 2) HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun ChannelListItem(data: List<ChannelListItemData>, initialIsExpanded: Boolean) {
    if (data.isEmpty()) return

    var isExpanded by remember { mutableStateOf(initialIsExpanded) }
    val animatedArrowRotation by animateFloatAsState(if (isExpanded) 180f else 0f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Wrap(
            modifier = Modifier.fillMaxWidth(),
            horizontalGapPx = 16,
            verticalGapPx = 16
        ) {
            SpacedRow(15) {
                Image(src = data.first().channelAsset, width = 46, height = 46)
                Column {
                    SpacedRow(8) {
                        TextBox(
                            lineHeight = 28.3,
                            size = 18,
                            text = data.first().channelName,
                            weight = FontWeight.Medium,
                        )
                        if (data.first().isChannelVerified) {
                            Image(src = Assets.Icons.VERIFIED_BADGE, width = 15, height = 15)
                        }
                    }
                    TextBox(
                        color = Styles.VIDEO_CARD_SECONDARY_TEXT,
                        lineHeight = 23.1,
                        text = "${data.first().subscribersCount} subscribers",
                    )
                }
            }
            Spacer()
            SpacedRow(32) {
                AssetSvgButton(
                    id = "list_item_subscribe_button_${data.first().channelName}",
                    onClick = {},
                    startIconPath = Assets.Paths.NOTIFS_SELECTED,
                ) {
                    Text("Subscribed")
                }
                AssetImageButton(
                    asset = Assets.Icons.ARROW_DOWN,
                    modifier = Modifier.rotate(animatedArrowRotation.deg)
                ) { isExpanded = !isExpanded }
            }
        }

        AnimatedVisibility(isVisible = isExpanded, modifier = Modifier.fillMaxWidth()) {
            ScrollableSpacedRow {
                data.forEach { item ->
                    when (item) {
                        is ChannelListItemData.Post -> MessagePostCard(item)
                        is ChannelListItemData.Thumbnail -> VideoThumbnailCard(
                            channelAsset = item.channelAsset,
                            channelName = item.channelName,
                            daysSinceUploaded = item.daysAgo,
                            duration = item.videoDuration,
                            isVerified = item.isChannelVerified,
                            size = VideoThumbnailCardDefaults.SIZE,
                            thumbnailAsset = item.thumbnailAsset,
                            title = item.videoTitle,
                            views = item.viewCount,
                        )
                    }
                }
            }
        }
    }
}
