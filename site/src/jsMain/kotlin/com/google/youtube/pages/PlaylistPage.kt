package com.google.youtube.pages

import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.components.widgets.IconLabel
import com.google.youtube.components.widgets.PlaylistListItem
import com.google.youtube.components.widgets.RadioButton
import com.google.youtube.components.widgets.SegmentedButtonPair
import com.google.youtube.components.widgets.context.RoundedSearchTextField
import com.google.youtube.data.PlaylistDataProvider
import com.google.youtube.models.PlaylistItemData
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.utils.Asset
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.ReorderableList
import com.google.youtube.utils.ReorderableListItem
import com.google.youtube.utils.ReorderableListState
import com.google.youtube.utils.Route
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.Wrap
import com.google.youtube.utils.clickable
import com.google.youtube.utils.rememberIsSmallBreakpoint
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import kotlinx.browser.window
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

@Composable
fun PlaylistPage(id: String) {
    val navigator = LocalNavigator.current
    val isSmallBreakpoint by rememberIsSmallBreakpoint()
    val isTouchDevice = remember {
        (jsTypeOf(window.asDynamic().ontouchstart) != "undefined" ||
                window.navigator.maxTouchPoints > 0 ||
                window.navigator.asDynamic().msMaxTouchPoints > 0)
    }

    val isGridModeSelected = remember { mutableStateOf(false) }
    val searchQueryState = remember { mutableStateOf("") }

    // Reorderable States
    // TODO: See if these states can be combined
    val smallBreakpointReorderableState = remember { ReorderableListState() }
    val largeBreakpointReorderableState = remember { ReorderableListState() }
    val activeReorderableState by remember {
        derivedStateOf {
            if (isSmallBreakpoint) smallBreakpointReorderableState
            else largeBreakpointReorderableState
        }
    }

    // Data States
    val playlistDataProvider = remember { PlaylistDataProvider() }
    val videos = remember(playlistDataProvider) {
        playlistDataProvider.getVideosForPlaylistWithId(id)
    }
    val activeThumbnailRef: String? = remember(activeReorderableState.topIndex) {
        videos.elementAtOrNull(activeReorderableState.topIndex)?.thumbnailAsset
    }
    val playlistDetails: PlaylistItemData = remember(playlistDataProvider, activeThumbnailRef) {
        val playlist = playlistDataProvider.getPlaylistForId(id)
        playlist.copy(thumbnailImageRef = activeThumbnailRef ?: playlist.thumbnailImageRef)
    }
    val isSelectedStates = remember { mutableStateListOf(*Array(size = videos.size) { false }) }

    SpacedColumn(spacePx = 32, modifier = Modifier.fillMaxWidth().padding(bottom = 32.px)) {
        // Bordered container
        Box(
            modifier = Modifier
                .border(1.px, LineStyle.Solid, Styles.WHITE.copyf(alpha = 0.14f))
                .borderRadius(24.px)
                .fillMaxWidth()
                .padding(24.px)
        ) {
            PlaylistListItem(
                data = playlistDetails,
                showThumbnailColorPalette = false,
                isEditable = true,
            )
        }

        // Filters
        Wrap(modifier = Modifier.fillMaxWidth(), verticalGapPx = 8) {
            RadioButton(
                onClick = {
                    val targetValue = isSelectedStates.contains(false)
                    for (i in isSelectedStates.indices) {
                        isSelectedStates[i] = targetValue
                    }
                },
                isSelected = remember { derivedStateOf { !isSelectedStates.contains(false) } }.value,
                modifier = Modifier.margin(right = 19.px),
            )
            Box(modifier = Modifier.weight(1)) {
                AssetSvgButton(
                    id = "sort_by_button",
                    type = AssetSvgButtonType.SelectableChip,
                    secondaryText = "Sort by:",
                    text = "Relevance",
                    isDense = true,
                    endIconPath = Asset.Path.ARROW_DOWN,
                    onClick = {},
                )
            }
            Row(modifier = Modifier.margin(leftRight = 16.px)) {
                AssetSvgButton(
                    id = "grid_mode_button",
                    type = AssetSvgButtonType.SelectableChip,
                    startIconPath = Asset.Path.GRID,
                    isSelected = isGridModeSelected.value,
                    onClick = { isGridModeSelected.value = true },
                )
                Box(modifier = Modifier.margin(left = 8.px)) {
                    AssetSvgButton(
                        id = "list_mode_button",
                        type = AssetSvgButtonType.SelectableChip,
                        startIconPath = Asset.Path.LIST,
                        isSelected = !isGridModeSelected.value,
                        onClick = { isGridModeSelected.value = false },
                    )
                }
            }
            RoundedSearchTextField(textState = searchQueryState, hintText = "Search playlists")
        }

        if (isSmallBreakpoint) {
            ReorderableList(
                state = smallBreakpointReorderableState,
                modifier = Modifier.fillMaxWidth(),
                verticalSpacePx = REORDERABLE_LIST_VERTICAL_SPACE_PX,
            ) {
                videos.forEachIndexed { index, details ->
                    ReorderableListItem(
                        modifier = Modifier.fillMaxWidth(),
                        onPressScale = REORDERABLE_LIST_ON_PRESS_SCALE,
                        handle = { GrabHandle() },
                        content = { handle ->
                            SmallListItem(
                                details = details,
                                isTouchDevice = isTouchDevice,
                                handle = handle,
                                isSelected = isSelectedStates[index],
                                onSelectionChanged = { isSelected ->
                                    isSelectedStates[index] = isSelected
                                },
                                onClick = { navigator.pushRoute(Route.Video(details.id)) },
                            )
                        },
                    )
                }
            }
        } else {
            ReorderableList(
                state = largeBreakpointReorderableState,
                modifier = Modifier.fillMaxWidth(),
                verticalSpacePx = REORDERABLE_LIST_VERTICAL_SPACE_PX,
            ) {
                videos.forEachIndexed { index, details ->
                    ReorderableListItem(
                        modifier = Modifier.fillMaxWidth(),
                        onPressScale = REORDERABLE_LIST_ON_PRESS_SCALE,
                        handle = { GrabHandle() },
                        content = { handle ->
                            LargeListItem(
                                details = details,
                                isTouchDevice = isTouchDevice,
                                handle = handle,
                                isSelected = isSelectedStates[index],
                                onSelectionChanged = { isSelected ->
                                    isSelectedStates[index] = isSelected
                                },
                                onClick = { navigator.pushRoute(Route.Video(details.id)) },
                            )
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun GrabHandle() {
    Image(
        modifier = Modifier.cursor(Cursor.Grab),
        src = Asset.Icon.DRAG_HANDLE,
        width = 24,
        height = 24
    )
}

@Composable
private fun SmallListItem(
    details: VideoThumbnailDetails,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    isTouchDevice: Boolean,
    handle: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    val updatedIsSelected = updateTransition(isSelected).currentState

    SpacedColumn(spacePx = 16, modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxWidth()) {
            Image(
                modifier = Modifier
                    .aspectRatio(259, 146)
                    .clip(Styles.Shape.CARD)
                    .fillMaxWidth(),
                src = details.thumbnailAsset,
            )

            DurationBadge(details.duration)
        }

        TitleText(details.title)

        SpacedRow(15) {
            SpacedRow(16) {
                RadioButtonWithHandle(
                    isSmallBreakpoint = true,
                    onSelectionChanged = onSelectionChanged,
                    updatedIsSelected = updatedIsSelected,
                    isTouchDevice = isTouchDevice,
                    handle = handle,
                )
                IconLabel(Asset.Icon.EYE, details.views)
            }
            Box(modifier = Modifier.margin(left = 9.px)) {
                IconLabel(Asset.Icon.DATE, details.uploadDate.orEmpty())
            }
        }

        ChannelInfo(details)

        SegmentedButtonPair(
            assetPathLeft = Asset.Path.LIKED,
            assetPathRight = Asset.Path.DISLIKE,
            containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
            isDense = true,
            labelLeft = details.likeCount,
            labelRight = details.dislikeCount,
        )
    }
}

@Composable
private fun LargeListItem(
    details: VideoThumbnailDetails,
    isSelected: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    isTouchDevice: Boolean,
    handle: @Composable () -> Unit,
    onClick: () -> Unit,
) {
    val updatedIsSelected = updateTransition(isSelected).currentState

    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).height(146.px)) {
        RadioButtonWithHandle(
            isSmallBreakpoint = false,
            onSelectionChanged = onSelectionChanged,
            updatedIsSelected = updatedIsSelected,
            isTouchDevice = isTouchDevice,
            handle = handle,
        )

        SpacedRow(
            spacePx = 16,
            modifier = Modifier.weight(1).overflow { x(Overflow.Hidden) },
            centerContentVertically = false,
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    modifier = Modifier
                        .clip(Styles.Shape.CARD)
                        .width(259.px),
                    src = details.thumbnailAsset,
                )

                DurationBadge(details.duration)
            }

            Row(modifier = Modifier.weight(1)) {
                SpacedColumn(spacePx = 16, modifier = Modifier.weight(1)) {
                    TitleText(details.title)

                    Wrap(15) {
                        SpacedRow(15) {
                            IconLabel(Asset.Icon.EYE, details.views)
                            Box(modifier = Modifier.margin(left = 9.px)) {
                                IconLabel(Asset.Icon.DATE, details.uploadDate.orEmpty())
                            }
                        }
                        SegmentedButtonPair(
                            assetPathLeft = Asset.Path.LIKED,
                            assetPathRight = Asset.Path.DISLIKE,
                            containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
                            isDense = true,
                            labelLeft = details.likeCount,
                            labelRight = details.dislikeCount,
                        )
                    }

                    ChannelInfo(details)
                }

                AssetImageButton(Asset.Icon.MORE) {}
            }
        }
    }
}

@Composable
private fun ChannelInfo(details: VideoThumbnailDetails) {
    SpacedRow(8) {
        Image(
            modifier = Modifier
                .clip(Circle())
                .margin(right = 7.px),
            src = Asset.Icon.USER_AVATAR,
            width = 28,
            height = 28,
        )
        details.channelName?.let { name -> TextBox(text = name) }
        if (details.isVerified) Image(src = Asset.Icon.VERIFIED_BADGE, width = 15, height = 15)
        TextBox(
            text = "${details.subscribersCount} subscribers",
            size = 14,
            color = Styles.VIDEO_CARD_SECONDARY_TEXT,
        )
    }
}

@Composable
private fun TitleText(text: String) {
    TextBox(
        text = text,
        size = 18,
        lineHeight = 25,
        color = Styles.OFF_WHITE,
        weight = FontWeight.Medium,
        maxLines = 1,
    )
}

@Composable
private fun DurationBadge(duration: String) {
    TextBox(
        lineHeight = 15.9,
        modifier = Modifier
            .background(Styles.BLACK.copyf(alpha = 0.6f))
            .clip(Styles.Shape.THUMBNAIL_DURATION_CONTAINER)
            .margin(10.px)
            .padding(leftRight = 6.px, topBottom = 4.px),
        text = duration,
        weight = FontWeight.Medium,
    )
}

@Composable
private fun RadioButtonWithHandle(
    isSmallBreakpoint: Boolean,
    onSelectionChanged: (Boolean) -> Unit,
    updatedIsSelected: Boolean,
    isTouchDevice: Boolean,
    handle: @Composable () -> Unit,
) {
    if (isSmallBreakpoint) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.px)) {
            if (!isTouchDevice) handle()
            RadioButton(
                onClick = { onSelectionChanged(!updatedIsSelected) },
                isSelected = updatedIsSelected,
            )
        }
    } else {
        Column(
            modifier = Modifier.margin(right = 19.px),
            verticalArrangement = Arrangement.spacedBy(34.px)
        ) {
            RadioButton(
                onClick = { onSelectionChanged(!updatedIsSelected) },
                isSelected = updatedIsSelected,
            )
            if (!isTouchDevice) handle()
        }
    }
}

private const val REORDERABLE_LIST_VERTICAL_SPACE_PX = 30
private const val REORDERABLE_LIST_ON_PRESS_SCALE = 0.99f
