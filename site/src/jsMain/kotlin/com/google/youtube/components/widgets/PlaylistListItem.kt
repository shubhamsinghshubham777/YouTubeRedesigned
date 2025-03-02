package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.CSSLengthOrPercentageValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

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
                        Image(src = Assets.Icons.USER_AVATAR, width = 28, height = 28)
                        TextBox(
                            text = data?.channelName.orEmpty(),
                            modifier = Modifier.margin(left = 7.px)
                        )
                        if (data?.isChannelVerified == true) {
                            Image(
                                src = Assets.Icons.VERIFIED_BADGE,
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
                            iconAsset = Assets.Icons.EYE,
                            label = (data?.viewsCount ?: 0).toString(),
                            secondaryLabel = "views",
                        )
                        IconLabel(
                            iconAsset = Assets.Icons.PLAY,
                            label = data?.videosCount.toString(),
                            secondaryLabel = "videos",
                        )
                        IconLabel(
                            iconAsset = Assets.Icons.DURATION,
                            label = data?.totalDuration.orEmpty(),
                            secondaryLabel = "duration",
                        )
                    }
                    Wrap(horizontalGapPx = 15, modifier = Modifier.fillMaxWidth()) {
                        AssetSvgButton(
                            id = "play_all_button_${data?.name.orEmpty()}",
                            isDense = true,
                            startIconPath = Assets.Paths.PLAY,
                            text = "Play All",
                            isSelected = true,
                            onClick = {},
                        )
                        AssetSvgButton(
                            id = "share_button_${data?.name.orEmpty()}",
                            isDense = true,
                            startIconPath = Assets.Paths.SHARE,
                            text = "Share",
                            onClick = {},
                        )
                        AssetSvgButton(
                            id = "add_video_button_${data?.name.orEmpty()}",
                            isDense = true,
                            startIconPath = Assets.Paths.ADD_SOLO,
                            text = "Add video",
                            onClick = {},
                        )
                        AssetSvgButton(
                            id = "download_button_${data?.name.orEmpty()}",
                            isDense = true,
                            startIconPath = Assets.Paths.DOWNLOAD,
                            text = "Download",
                            onClick = {},
                        )
                    }
                }
                AssetImageButton(
                    asset = Assets.Icons.MORE,
                    modifier = Modifier.thenIf(!isEditable) { Modifier.margin(top = 9.px) },
                    onClick = {},
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigator.pushRoute(Route.Playlist(id = data?.id ?: return@clickable)) },
    ) {
        if (isSmallBreakpoint) {
            SpacedColumn(spacePx = 8, modifier = Modifier.fillMaxWidth()) {
                StackedThumbnail(
                    assetRef = Assets.Thumbnails.THUMBNAIL_1,
                    videosCount = if (isEditable) null else data?.videosCount,
                    modifier = Modifier.fillMaxWidth(),
                    showThumbnailColorPalette = showThumbnailColorPalette,
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
                    assetRef = Assets.Thumbnails.THUMBNAIL_1,
                    videosCount = if (isEditable) null else data?.videosCount,
                    showThumbnailColorPalette = showThumbnailColorPalette,
                )
                content()
            }
        }
    }
}

@Composable
private fun StackedThumbnail(
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

    LaunchedEffect(assetRef, showThumbnailColorPalette) {
        if (showThumbnailColorPalette) {
            paletteColors = generateColorPalette(assetRef)
        }
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
            videosCount?.let { count ->
                SpacedRow(
                    spacePx = 8,
                    modifier = Modifier
                        .background(Styles.BLACK.copyf(alpha = 0.6f))
                        .borderRadius(6.px)
                        .margin(10.px)
                        .padding(left = 12.px, top = 2.px, right = 8.px, bottom = 2.px),
                ) {
                    Image(src = Assets.Icons.PLAYLISTS, width = 24, height = 24)
                    TextBox(
                        text = count.toString(),
                        weight = FontWeight.Medium,
                        lineHeight = 15.9,
                    )
                }
            } ?: run {
                AssetImageButton(
                    modifier = Modifier.clip(Circle()).margin(10.px).padding(7.px),
                    asset = Assets.Icons.EDIT,
                    containerColor = Styles.BLACK.copyf(alpha = 0.6f),
                    onClick = {},
                )
            }
        }
    }
}
