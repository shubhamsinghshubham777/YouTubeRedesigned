package com.google.youtube.components.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.models.PlaylistItemData
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
import com.google.youtube.utils.generateColorPalette
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberIsSmallBreakpoint
import com.varabyte.kobweb.browser.dom.observers.ResizeObserver
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.dom.disposableRef
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
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.CSSLengthOrPercentageValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.w3c.dom.DOMRectReadOnly

@Composable
fun PlaylistListItem(
    data: PlaylistItemData?,
    showThumbnailColorPalette: Boolean = true,
    isEditable: Boolean = false,
) {
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
                        text = data?.name.orEmpty(),
                        size = 18,
                        weight = FontWeight.Medium,
                        lineHeight = 25,
                    )
                    Wrap(8) {
                        Image(src = Asset.Icon.USER_AVATAR, width = 28, height = 28)
                        TextBox(
                            text = data?.channelName.orEmpty(),
                            modifier = Modifier.margin(left = 7.px)
                        )
                        if (data?.isChannelVerified == true) {
                            Image(
                                src = Asset.Icon.VERIFIED_BADGE,
                                width = 15,
                                height = 15
                            )
                        }
                        TextBox(
                            text = "${data?.subscriberCount ?: 0} subscribers",
                            size = 14,
                            color = Styles.VIDEO_CARD_SECONDARY_TEXT
                        )
                    }
                    Wrap(horizontalGapPx = 24, verticalGapPx = 8) {
                        IconLabel(
                            iconAsset = Asset.Icon.EYE,
                            label = (data?.viewsCount ?: 0).toString(),
                            secondaryLabel = "views",
                        )
                        IconLabel(
                            iconAsset = Asset.Icon.PLAY,
                            label = data?.videosCount.toString(),
                            secondaryLabel = "videos",
                        )
                        IconLabel(
                            iconAsset = Asset.Icon.DURATION,
                            label = data?.totalDuration.orEmpty(),
                            secondaryLabel = "duration",
                        )
                    }
                    Wrap(horizontalGapPx = 15, modifier = Modifier.fillMaxWidth()) {
                        AssetSvgButton(
                            id = "play_all_button_${data?.name.orEmpty()}",
                            isDense = true,
                            startIconPath = Asset.Path.PLAY,
                            text = "Play All",
                            isSelected = true,
                            onClick = {},
                        )
                        AssetSvgButton(
                            id = "share_button_${data?.name.orEmpty()}",
                            isDense = true,
                            startIconPath = Asset.Path.SHARE,
                            text = "Share",
                            onClick = {},
                        )
                        AssetSvgButton(
                            id = "add_video_button_${data?.name.orEmpty()}",
                            isDense = true,
                            startIconPath = Asset.Path.ADD_SOLO,
                            text = "Add video",
                            onClick = {},
                        )
                        AssetSvgButton(
                            id = "download_button_${data?.name.orEmpty()}",
                            isDense = true,
                            startIconPath = Asset.Path.DOWNLOAD,
                            text = "Download",
                            onClick = {},
                        )
                    }
                }
                AssetImageButton(
                    asset = Asset.Icon.MORE,
                    modifier = Modifier.thenIf(!isEditable) { Modifier.margin(top = 9.px) },
                    onClick = {},
                )
            }
        }
    }
    val onViewPlaylist: () -> Unit = remember {
        {
            data?.id?.let { id -> navigator.pushRoute(Route.Playlist(id = id)) }
        }
    }
    val onPlayAll: () -> Unit = remember { {} }

    Box(modifier = Modifier.fillMaxWidth().clickable(onClick = onViewPlaylist)) {
        if (isSmallBreakpoint) {
            SpacedColumn(spacePx = 8, modifier = Modifier.fillMaxWidth()) {
                StackedThumbnail(
                    assetRef = Asset.Thumbnails.THUMBNAIL_1,
                    videosCount = if (isEditable) null else data?.videosCount,
                    modifier = Modifier.fillMaxWidth(),
                    showThumbnailColorPalette = showThumbnailColorPalette,
                    onViewPlaylist = if (isEditable) null else onViewPlaylist,
                    onPlayAll = if (isEditable) null else onPlayAll,
                )
                content()
            }
        } else {
            SpacedRow(
                spacePx = 16,
                modifier = Modifier.fillMaxWidth(),
                centerContentVertically = false,
            ) {
                StackedThumbnail(
                    assetRef = Asset.Thumbnails.THUMBNAIL_1,
                    videosCount = if (isEditable) null else data?.videosCount,
                    showThumbnailColorPalette = showThumbnailColorPalette,
                    onViewPlaylist = if (isEditable) null else onViewPlaylist,
                    onPlayAll = if (isEditable) null else onPlayAll,
                )
                content()
            }
        }
    }
}

@Composable
private fun StackedThumbnail(
    onViewPlaylist: (() -> Unit)? = null,
    onPlayAll: (() -> Unit)? = null,
    assetRef: String,
    videosCount: Int?,
    showThumbnailColorPalette: Boolean,
    modifier: Modifier = Modifier,
    borderRadius: CSSLengthOrPercentageValue = 20.px,
) {
    var paletteColors by remember { mutableStateOf<List<String>?>(null) }
    val commonModifier = remember(paletteColors) {
        Modifier
            .height(5.px)
            .borderRadius(topLeft = borderRadius, topRight = borderRadius)
    }
    val showHoveredControls = remember { onViewPlaylist != null && onPlayAll != null }
    var imageRect by remember { mutableStateOf<DOMRectReadOnly?>(null) }
    var isImageHovered by remember { mutableStateOf(false) }
    val hoveredControlsAnimatedOpacity by animateFloatAsState(if (isImageHovered) 1f else 0f)

    LaunchedEffect(assetRef, showThumbnailColorPalette) {
        if (showThumbnailColorPalette) { paletteColors = generateColorPalette(assetRef) }
    }

    SpacedColumn(
        spacePx = 2,
        centerContentHorizontally = true,
        modifier = Modifier.noShrink().then(modifier),
    ) {
        if (showThumbnailColorPalette) {
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
        }
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxSize()
                .thenIf(showHoveredControls) {
                    Modifier
                        .onMouseEnter { isImageHovered = true }
                        .onMouseLeave { isImageHovered = false }
                },
        ) {
            Image(
                ref = if (!showHoveredControls) null else disposableRef { e ->
                    imageRect = e.getBoundingClientRect()
                    val observer = ResizeObserver { entries ->
                        entries.firstOrNull()?.let { entry -> imageRect = entry.contentRect }
                    }
                    observer.observe(e)
                    onDispose {
                        with(observer) {
                            disconnect()
                            unobserve(e)
                        }
                    }
                },
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
            videosCount?.let { count ->
                SpacedRow(
                    spacePx = 8,
                    modifier = Modifier
                        .background(Styles.BLACK.copyf(alpha = 0.6f))
                        .borderRadius(6.px)
                        .margin(10.px)
                        .padding(left = 12.px, top = 2.px, right = 8.px, bottom = 2.px),
                ) {
                    Image(src = Asset.Icon.PLAYLISTS, width = 24, height = 24)
                    TextBox(
                        text = count.toString(),
                        weight = FontWeight.Medium,
                        lineHeight = 15.9,
                    )
                }
            } ?: run {
                AssetImageButton(
                    modifier = Modifier.clip(Circle()).margin(10.px).padding(7.px),
                    asset = Asset.Icon.EDIT,
                    containerColor = Styles.BLACK.copyf(alpha = 0.6f),
                    onClick = {},
                )
            }
            if (showHoveredControls) {
                imageRect?.let { rect ->
                    Box(
                        modifier = Modifier
                            .background(Styles.BLACK.copyf(alpha = 0.5f))
                            .borderRadius(borderRadius)
                            .opacity(hoveredControlsAnimatedOpacity)
                            .size(width = rect.width.px, height = rect.height.px),
                        contentAlignment = Alignment.Center,
                    ) {
                        SpacedRow(15) {
                            AssetSvgButton(
                                containerColor = Styles.BLACK.copyf(alpha = 0.4f),
                                id = "view_playlist_button",
                                onClick = { onViewPlaylist?.invoke() },
                                text = "View Playlist",
                            )
                            AssetSvgButton(
                                containerColor = Styles.BLACK.copyf(alpha = 0.4f),
                                id = "play_all_button",
                                onClick = { onPlayAll?.invoke() },
                                text = "Play All",
                            )
                        }
                    }
                }
            }
        }
    }
}
