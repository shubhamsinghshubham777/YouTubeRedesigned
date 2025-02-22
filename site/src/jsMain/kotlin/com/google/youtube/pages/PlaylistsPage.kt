package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.components.widgets.IconLabel
import com.google.youtube.components.widgets.Wrap
import com.google.youtube.components.widgets.context_menu.RoundedSearchTextField
import com.google.youtube.models.PlaylistItemData
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Constants
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.Route
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.clickable
import com.google.youtube.utils.generateColorPalette
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberIsSmallBreakpoint
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.CSSLengthOrPercentageValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun PlaylistsPage() {
    val isGridModeSelected = remember { mutableStateOf(false) }
    val searchQueryState = remember { mutableStateOf("") }

    SpacedColumn(spacePx = 28, modifier = Modifier.fillMaxWidth().padding(bottom = 28.px)) {
        TopBar(isGridModeSelected, searchQueryState)
        remember {
            List(10) { index ->
                PlaylistItemData(
                    id = "collection_$index",
                    name = "Redesigns $index",
                    channelName = "Juxtopposed",
                    thumbnailImageRef = Assets.Thumbnails.THUMBNAIL_1,
                    channelImageRef = Assets.Icons.USER_AVATAR,
                    isChannelVerified = true,
                    subscriberCount = "288K",
                    viewsCount = 50,
                    videosCount = 15,
                    totalDuration = "2:51:23",
                )
            }
        }.forEach { data ->
            PlaylistListItem(data)
        }
    }
}

@Composable
private fun PlaylistListItem(data: PlaylistItemData) {
    val navigator = LocalNavigator.current
    val isSmallBreakpoint by rememberIsSmallBreakpoint()
    val content = remember {
        movableContentOf {
            SpacedRow(
                spacePx = 16,
                modifier = Modifier.fillMaxWidth(),
                centerContentVertically = false,
            ) {
                SpacedColumn(
                    spacePx = 24,
                    modifier = Modifier.weight(1).margin(top = 9.px),
                ) {
                    TextBox(
                        text = data.name,
                        size = 18,
                        weight = FontWeight.Medium,
                        lineHeight = 25,
                    )
                    Wrap(8) {
                        Image(src = Assets.Icons.USER_AVATAR, width = 28, height = 28)
                        TextBox(
                            text = data.channelName,
                            modifier = Modifier.margin(left = 7.px)
                        )
                        Image(
                            src = Assets.Icons.VERIFIED_BADGE,
                            width = 15,
                            height = 15
                        )
                        TextBox(
                            text = "${data.subscriberCount} subscribers",
                            size = 14,
                            color = Styles.VIDEO_CARD_SECONDARY_TEXT
                        )
                    }
                    Wrap(horizontalGapPx = 24, verticalGapPx = 8) {
                        IconLabel(
                            iconAsset = Assets.Icons.EYE,
                            label = data.viewsCount.toString(),
                            secondaryLabel = "views",
                        )
                        IconLabel(
                            iconAsset = Assets.Icons.PLAY,
                            label = data.videosCount.toString(),
                            secondaryLabel = "videos",
                        )
                        IconLabel(
                            iconAsset = Assets.Icons.DURATION,
                            label = data.totalDuration,
                            secondaryLabel = "duration",
                        )
                    }
                    Wrap(horizontalGapPx = 15, modifier = Modifier.fillMaxWidth()) {
                        AssetSvgButton(
                            id = "play_all_button_${data.name}",
                            isDense = true,
                            startIconPath = Assets.Paths.PLAY,
                            text = "Play All",
                            isSelected = true,
                            onClick = {},
                        )
                        AssetSvgButton(
                            id = "share_button_${data.name}",
                            isDense = true,
                            startIconPath = Assets.Paths.SHARE,
                            text = "Share",
                            onClick = {},
                        )
                        AssetSvgButton(
                            id = "add_video_button_${data.name}",
                            isDense = true,
                            startIconPath = Assets.Paths.ADD_SOLO,
                            text = "Add video",
                            onClick = {},
                        )
                        AssetSvgButton(
                            id = "download_button_${data.name}",
                            isDense = true,
                            startIconPath = Assets.Paths.DOWNLOAD,
                            text = "Download",
                            onClick = {},
                        )
                    }
                }
                AssetImageButton(
                    asset = Assets.Icons.MORE,
                    modifier = Modifier.margin(top = 9.px),
                    onClick = {},
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigator.pushRoute(Route.Playlist(id = data.id)) },
    ) {
        if (isSmallBreakpoint) {
            SpacedColumn(
                spacePx = 8,
                modifier = Modifier.fillMaxWidth()
            ) {
                StackedThumbnail(
                    assetRef = Assets.Thumbnails.THUMBNAIL_1,
                    modifier = Modifier.fillMaxWidth(),
                )
                content()
            }
        } else {
            SpacedRow(
                spacePx = 16,
                modifier = Modifier.fillMaxWidth(),
                centerContentVertically = false,
            ) {
                StackedThumbnail(assetRef = Assets.Thumbnails.THUMBNAIL_1)
                content()
            }
        }
    }
}

@Composable
private fun StackedThumbnail(
    assetRef: String,
    modifier: Modifier = Modifier,
    borderRadius: CSSLengthOrPercentageValue = 20.px,
) {
    var paletteColors by remember { mutableStateOf<List<String>?>(null) }
    val commonModifier = remember(paletteColors) {
        Modifier
            .height(5.px)
            .borderRadius(topLeft = borderRadius, topRight = borderRadius)
    }

    LaunchedEffect(assetRef) { paletteColors = generateColorPalette(assetRef) }

    SpacedColumn(
        spacePx = 2,
        centerContentHorizontally = true,
        modifier = Modifier.noShrink().then(modifier),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(85.percent)
                .then(
                    paletteColors?.let { colors -> Modifier.background(Color(colors[0])) }
                        ?: Modifier.display(DisplayStyle.None),
                )
                .then(commonModifier)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(91.percent)
                .then(
                    paletteColors?.let { colors -> Modifier.background(Color(colors[1])) }
                        ?: Modifier.display(DisplayStyle.None),
                )
                .then(commonModifier)
        )
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(
                        width = Constants.SUGGESTION_THUMBNAIL_SIZE.width,
                        height = Constants.SUGGESTION_THUMBNAIL_SIZE.height
                    )
                    .borderRadius(borderRadius)
                    .objectFit(ObjectFit.Cover),
                src = assetRef,
            )
            SpacedRow(
                spacePx = 8,
                modifier = Modifier
                    .background(Styles.BLACK.copyf(alpha = 0.6f))
                    .borderRadius(6.px)
                    .margin(10.px)
                    .padding(left = 12.px, top = 2.px, right = 8.px, bottom = 2.px),
            ) {
                Image(src = Assets.Icons.PLAYLISTS, width = 24, height = 24)
                TextBox(text = "15", weight = FontWeight.Medium, lineHeight = 15.9)
            }
        }
    }
}

@Composable
private fun TopBar(
    isGridModeSelected: MutableState<Boolean>,
    searchQueryState: MutableState<String>,
) {
    Wrap(modifier = Modifier.fillMaxWidth(), horizontalGapPx = 8) {
        Box(modifier = Modifier.weight(1)) {
            AssetSvgButton(
                id = "sort_by_button",
                type = AssetSvgButtonType.SelectableChip,
                secondaryText = "Sort by:",
                text = "Relevance",
                isDense = true,
                endIconPath = Assets.Paths.ARROW_DOWN,
                onClick = {},
            )
        }
        AssetSvgButton(
            id = "grid_mode_button",
            type = AssetSvgButtonType.SelectableChip,
            startIconPath = Assets.Paths.GRID,
            isSelected = isGridModeSelected.value,
            onClick = { isGridModeSelected.value = true },
        )
        AssetSvgButton(
            id = "list_mode_button",
            type = AssetSvgButtonType.SelectableChip,
            startIconPath = Assets.Paths.LIST,
            isSelected = !isGridModeSelected.value,
            onClick = { isGridModeSelected.value = false },
        )
        RoundedSearchTextField(
            textState = searchQueryState,
            hintText = "Search playlists"
        ) { marginLeft(8.px) }
    }
}
