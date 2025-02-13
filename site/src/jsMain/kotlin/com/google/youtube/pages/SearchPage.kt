package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.components.widgets.IconLabel
import com.google.youtube.components.widgets.SegmentedButtonPair
import com.google.youtube.components.widgets.Wrap
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Constants
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.Route
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.clickable
import com.google.youtube.utils.rememberIsLargeBreakpoint
import com.google.youtube.utils.rememberIsSmallBreakpoint
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.rowGap
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.px

@Suppress("UNUSED_PARAMETER")
@Composable
fun SearchPage(query: String) {
    SpacedColumn(spacePx = 40, modifier = Modifier.fillMaxWidth().padding(bottom = 40.px)) {
        Filters(modifier = Modifier.margin(bottom = 2.px))
        repeat(10) {
            ListCard(
                details = VideoThumbnailDetails(
                    id = "zryrtkfabzb",
                    thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                    channelAsset = Assets.Icons.USER_AVATAR,
                    title = "Classic Gingerbread Cookies Recipe",
                    channelName = "Irene Magazine",
                    isVerified = true,
                    views = "1.2M",
                    daysSinceUploaded = "",
                    duration = "12:07",
                    likeCount = "26K",
                    subscribersCount = "27K",
                    uploadDate = "11 Dec 2021",
                )
            )
        }
    }
}

@Composable
private fun ListCard(details: VideoThumbnailDetails) {
    val navigator = LocalNavigator.current
    val isSmallBreakpoint by rememberIsSmallBreakpoint()

    val videoThumbnail = remember {
        movableContentOf { isLargeThumbnail: Boolean ->
            Box(
                modifier = Modifier.thenIf(isLargeThumbnail) { Modifier.fillMaxWidth() },
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    modifier = Modifier
                        .borderRadius(15.9.px)
                        .then(
                            if (isLargeThumbnail) {
                                Modifier.aspectRatio(width = 16, height = 9).fillMaxWidth()
                            } else {
                                Modifier.size(
                                    width = Constants.SUGGESTION_THUMBNAIL_SIZE.width.px,
                                    height = Constants.SUGGESTION_THUMBNAIL_SIZE.height.px
                                )
                            }
                        ),
                    src = details.thumbnailAsset,
                )
                TextBox(
                    modifier = Modifier
                        .background(Styles.BLACK.copyf(alpha = 0.6f))
                        .borderRadius(6.px)
                        .margin(10.px)
                        .padding(leftRight = 6.px, topBottom = 3.px),
                    lineHeight = 15.9,
                    text = details.duration,
                    weight = FontWeight.Medium,
                )
            }
        }
    }

    val videoDetails = remember {
        movableContentOf { modifier: Modifier ->
            Row(modifier = modifier) {
                SpacedColumn(spacePx = 16, modifier = Modifier.weight(1)) {
                    TextBox(
                        lineHeight = 25,
                        maxLines = 2,
                        modifier = Modifier.weight(1),
                        size = 18,
                        text = details.title,
                        weight = FontWeight.Medium,
                    )
                    Wrap(columnGapPx = 15, rowGapPx = 15) {
                        SpacedRow(24) {
                            IconLabel(iconAsset = Assets.Icons.EYE, label = details.views)
                            details.uploadDate?.let { uploadDate ->
                                IconLabel(iconAsset = Assets.Icons.DATE, label = uploadDate)
                            }
                        }
                        details.likeCount?.let { likeCount ->
                            SegmentedButtonPair(
                                assetPathLeft = Assets.Paths.LIKED,
                                assetPathRight = Assets.Paths.DISLIKE,
                                containerColor = Styles.SURFACE_ELEVATED,
                                isDense = true,
                                labelLeft = likeCount,
                                onClickLeft = {},
                                onClickRight = {},
                            )
                        }
                    }
                    Wrap(columnGapPx = 16, rowGapPx = 16) {
                        SpacedRow(15) {
                            Image(src = Assets.Icons.USER_AVATAR, width = 28, height = 28)
                            SpacedRow(8) {
                                TextBox(text = details.channelName, lineHeight = 28.3)
                                if (details.isVerified) {
                                    Image(
                                        src = Assets.Icons.VERIFIED_BADGE,
                                        width = 15,
                                        height = 15
                                    )
                                }
                            }
                        }
                        details.subscribersCount?.let { subscribersCount ->
                            TextBox(
                                text = "$subscribersCount subscribers",
                                size = 14,
                                color = Styles.VIDEO_CARD_SECONDARY_TEXT
                            )
                        }
                    }
                }
                AssetImageButton(Assets.Icons.MORE) {}
            }
        }
    }

    val onClick: () -> Unit = remember {
        {
            details.id?.let { id -> navigator.pushRoute(Route.Video(id)) }
        }
    }

    if (isSmallBreakpoint) {
        SpacedColumn(
            spacePx = 16,
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
        ) {
            videoThumbnail(true)
            videoDetails(Modifier.fillMaxWidth())
        }
    } else {
        SpacedRow(
            spacePx = 16,
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
            centerContentVertically = false
        ) {
            videoThumbnail(false)
            videoDetails(Modifier.weight(1))
        }
    }
}

@Composable
private fun Filters(modifier: Modifier = Modifier) {
    val isLargeBreakpoint by rememberIsLargeBreakpoint()
    var isLayoutTypeGrid by remember { mutableStateOf(false) }
    var isWatchTypeWatched by remember { mutableStateOf<Boolean?>(null) }

    Wrap(columnGapPx = 8, rowGapPx = 8, modifier = Modifier.fillMaxWidth().then(modifier)) {
        AssetSvgButton(
            endIconPath = Assets.Paths.ARROW_DOWN,
            id = "type_button",
            isDense = true,
            onClick = {},
            secondaryText = "Type:",
            text = "All",
            type = AssetSvgButtonType.SelectableChip,
        )
        AssetSvgButton(
            endIconPath = Assets.Paths.ARROW_DOWN,
            id = "upload_date_button",
            isDense = true,
            onClick = {},
            secondaryText = "Upload Date:",
            text = "Any Time",
            type = AssetSvgButtonType.SelectableChip,
        )
        AssetSvgButton(
            endIconPath = Assets.Paths.ARROW_DOWN,
            id = "duration_button",
            isDense = true,
            onClick = {},
            secondaryText = "Duration:",
            text = "Any",
            type = AssetSvgButtonType.SelectableChip,
        )
        AssetSvgButton(
            endIconPath = Assets.Paths.ARROW_DOWN,
            id = "features_button",
            isDense = true,
            onClick = {},
            secondaryText = "Features:",
            text = "Any",
            type = AssetSvgButtonType.SelectableChip,
        )
        AssetSvgButton(
            endIconPath = Assets.Paths.ARROW_DOWN,
            id = "sort_by_button",
            isDense = true,
            onClick = {},
            secondaryText = "Sort by:",
            text = "Relevance",
            type = AssetSvgButtonType.SelectableChip,
        )
        if (isLargeBreakpoint) Spacer()
        SpacedRow(spacePx = 8, modifier = Modifier.flexWrap(FlexWrap.Wrap).rowGap(8.px)) {
            AssetSvgButton(
                isDense = true,
                id = "watch_status_all_button",
                isSelected = isWatchTypeWatched == null,
                onClick = { isWatchTypeWatched = null },
                text = "All",
                type = AssetSvgButtonType.SelectableChip,
            )
            AssetSvgButton(
                isDense = true,
                id = "watch_status_unwatched_button",
                isSelected = isWatchTypeWatched == false,
                onClick = { isWatchTypeWatched = false },
                text = "Unwatched",
                type = AssetSvgButtonType.SelectableChip,
            )
            AssetSvgButton(
                isDense = true,
                id = "watch_status_watched_button",
                isSelected = isWatchTypeWatched == true,
                onClick = { isWatchTypeWatched = true },
                text = "Watched",
                type = AssetSvgButtonType.SelectableChip,
            )
            // Divider
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .background(Styles.DIVIDER_LIGHTER)
                    .height(28.px)
                    .margin(leftRight = 8.px)
                    .width(1.29.px)
            )
            AssetSvgButton(
                isDense = true,
                id = "layout_type_grid_button",
                isSelected = isLayoutTypeGrid,
                onClick = { isLayoutTypeGrid = true },
                startIconPath = Assets.Paths.GRID,
                type = AssetSvgButtonType.SelectableChip,
            )
            AssetSvgButton(
                isDense = true,
                id = "layout_type_list_button",
                isSelected = !isLayoutTypeGrid,
                onClick = { isLayoutTypeGrid = false },
                startIconPath = Assets.Paths.LIST,
                type = AssetSvgButtonType.SelectableChip,
            )
        }
    }
}
