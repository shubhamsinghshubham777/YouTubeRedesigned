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
import com.google.youtube.data.SearchDataProvider
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.utils.Asset
import com.google.youtube.utils.Constants
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.Route
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.Wrap
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
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.px

@Composable
fun SearchPage(query: String) {
    val searchDataProvider = remember { SearchDataProvider() }
    val results = remember(searchDataProvider) {
        searchDataProvider.getSearchedVideosForQuery(query)
    }
    SpacedColumn(spacePx = 40, modifier = Modifier.fillMaxWidth().padding(bottom = 40.px)) {
        Filters(modifier = Modifier.margin(bottom = 2.px))
        results.forEach { result -> ListCard(result) }
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
                        // TODO: Replace with Styles.Shape.CARD.cornerRadius
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
                    Wrap(horizontalGapPx = 15, verticalGapPx = 15) {
                        SpacedRow(24) {
                            IconLabel(iconAsset = Asset.Icon.EYE, label = details.views)
                            details.uploadDate?.let { uploadDate ->
                                IconLabel(iconAsset = Asset.Icon.DATE, label = uploadDate)
                            }
                        }
                        details.likeCount?.let { likeCount ->
                            SegmentedButtonPair(
                                assetPathLeft = Asset.Path.LIKED,
                                assetPathRight = Asset.Path.DISLIKE,
                                containerColor = Styles.SURFACE_ELEVATED,
                                isDense = true,
                                labelLeft = likeCount,
                            )
                        }
                    }
                    Wrap(horizontalGapPx = 16, verticalGapPx = 16) {
                        SpacedRow(15) {
                            Image(
                                modifier = Modifier.clip(Circle()),
                                src = details.channelAsset ?: Asset.Channel.JUXTOPPOSED,
                                width = 28,
                                height = 28,
                            )
                            SpacedRow(8) {
                                details.channelName?.let { channelName ->
                                    TextBox(text = channelName, lineHeight = 28.3)
                                }
                                if (details.isVerified) {
                                    Image(src = Asset.Icon.VERIFIED_BADGE, width = 18, height = 18)
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
                AssetImageButton(Asset.Icon.MORE) {}
            }
        }
    }

    val onClick: () -> Unit = remember {
        {
            navigator.pushRoute(Route.Video(details.id))
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

    Wrap(
        horizontalGapPx = 8,
        modifier = Modifier.fillMaxWidth().then(modifier),
    ) {
        AssetSvgButton(
            endIconPath = Asset.Path.ARROW_DOWN,
            id = "type_button",
            isDense = true,
            onClick = {},
            secondaryText = "Type:",
            text = "All",
            type = AssetSvgButtonType.SelectableChip,
        )
        AssetSvgButton(
            endIconPath = Asset.Path.ARROW_DOWN,
            id = "upload_date_button",
            isDense = true,
            onClick = {},
            secondaryText = "Upload Date:",
            text = "Any Time",
            type = AssetSvgButtonType.SelectableChip,
        )
        AssetSvgButton(
            endIconPath = Asset.Path.ARROW_DOWN,
            id = "duration_button",
            isDense = true,
            onClick = {},
            secondaryText = "Duration:",
            text = "Any",
            type = AssetSvgButtonType.SelectableChip,
        )
        AssetSvgButton(
            endIconPath = Asset.Path.ARROW_DOWN,
            id = "features_button",
            isDense = true,
            onClick = {},
            secondaryText = "Features:",
            text = "Any",
            type = AssetSvgButtonType.SelectableChip,
        )
        AssetSvgButton(
            endIconPath = Asset.Path.ARROW_DOWN,
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
                    .height(Constants.VERTICAL_DIVIDER_SIZE.height.px)
                    .margin(leftRight = 8.px)
                    .width(Constants.VERTICAL_DIVIDER_SIZE.width.px)
            )
            AssetSvgButton(
                isDense = true,
                id = "layout_type_grid_button",
                isSelected = isLayoutTypeGrid,
                onClick = { isLayoutTypeGrid = true },
                startIconPath = Asset.Path.GRID,
                type = AssetSvgButtonType.SelectableChip,
            )
            AssetSvgButton(
                isDense = true,
                id = "layout_type_list_button",
                isSelected = !isLayoutTypeGrid,
                onClick = { isLayoutTypeGrid = false },
                startIconPath = Asset.Path.LIST,
                type = AssetSvgButtonType.SelectableChip,
            )
        }
    }
}
