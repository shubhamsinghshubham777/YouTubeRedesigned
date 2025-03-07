package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import com.google.youtube.components.sections.TopBarDefaults
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.FilterRow
import com.google.youtube.components.widgets.ShortThumbnailCard
import com.google.youtube.components.widgets.ShortThumbnailCardDefaults
import com.google.youtube.models.ShortThumbnailDetails
import com.google.youtube.utils.Assets
import com.google.youtube.utils.BasicGrid
import com.google.youtube.utils.Constants
import com.google.youtube.utils.GridGap
import com.google.youtube.utils.Styles
import com.google.youtube.utils.hideScrollBar
import com.google.youtube.utils.limitTextWithEllipsis
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberIsShortWindowAsState
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.JustifyItems
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollSnapAlign
import com.varabyte.kobweb.compose.css.ScrollSnapStop
import com.varabyte.kobweb.compose.css.ScrollSnapType
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.justifyItems
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollSnapAlign
import com.varabyte.kobweb.compose.ui.modifiers.scrollSnapStop
import com.varabyte.kobweb.compose.ui.modifiers.scrollSnapType
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.Shape
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.plus
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text

@Composable
fun ShortsGrid(showPersonalisedFeedDialogState: MutableState<Boolean>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        FilterRow(showPersonalisedFeedDialogState = showPersonalisedFeedDialogState)
        BasicGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 27.px, bottom = 27.px),
            gridGap = GridGap(x = 16.px, y = 40.px),
            columnBuilder = { minmax(ShortThumbnailCardDefaults.SIZE.width.px, 1.fr) },
            justifyItems = JustifyItems.Center,
        ) {
            repeat(20) { index ->
                ShortThumbnailCard(
                    details = ShortThumbnailDetails(
                        id = index.toString(),
                        thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                        channelName = "DailyDoseOfInternet",
                        title = "Put this cat in jail",
                        views = "10M",
                        daysSinceUploaded = "3 weeks",
                    ),
                )
            }
        }
    }
}

@Composable
fun ShortDetails(
    id: String,
    onBackPressed: () -> Unit,
    shape: Shape = Rect(16.px),
) {
    val breakpoint = rememberBreakpoint()
    val isShortWindowState = rememberIsShortWindowAsState()
    val isSmallBreakpoint = remember(breakpoint) {
        breakpoint == Breakpoint.ZERO || breakpoint == Breakpoint.SM
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Div(
            Modifier
                .fillMaxWidth()
                .height(100.vh - TopBarDefaults.HEIGHT - Constants.CONTENT_PADDING)
                .hideScrollBar()
                .justifyItems(JustifyItems.Center)
                .overflow { y(Overflow.Scroll) }
                .padding(bottom = 15.vh)
                .scrollSnapType(ScrollSnapType.Y, ScrollSnapType.Strictness.Mandatory)
                .userSelect(UserSelect.None)
                .toAttrs(),
        ) {
            repeat(5) {
                val content = remember(isSmallBreakpoint) {
                    movableContentOf { modifier: Modifier ->
                        Box(
                            modifier = Modifier.clip(shape).fillMaxHeight().then(modifier),
                            contentAlignment = Alignment.BottomStart,
                        ) {
                            Image(
                                modifier = Modifier.fillMaxSize().objectFit(ObjectFit.Cover),
                                src = Assets.Thumbnails.THUMBNAIL_1,
                            )

                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .backgroundImage(
                                        linearGradient(LinearGradient.Direction.ToTop) {
                                            add(Colors.Black)
                                            add(Colors.Transparent)
                                        }
                                    )
                            )

                            Column(
                                modifier = Modifier.padding(topBottom = 24.px, leftRight = 21.px),
                                verticalArrangement = Arrangement.spacedBy(18.px)
                            ) {
                                val content = remember {
                                    movableContentOf {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(15.px),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Image(
                                                modifier = Modifier
                                                    .size(46.px)
                                                    .clip(Circle())
                                                    .noShrink(),
                                                src = Assets.Avatars.AVATAR_JACKSEPTICEYE,
                                            )

                                            Column(
                                                modifier = Modifier.weight(1),
                                                verticalArrangement = Arrangement.spacedBy(4.px)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .fontSize(18.px)
                                                        .fontWeight(FontWeight.Medium)
                                                        .limitTextWithEllipsis(),
                                                ) { Text("DailyDoseOfInternet") }
                                                Box(
                                                    modifier = Modifier
                                                        .fontSize(14.px)
                                                        .opacity(0.63f)
                                                        .limitTextWithEllipsis(),
                                                ) { Text("50K subscribers") }
                                            }
                                        }

                                        Box(
                                            modifier = Modifier
                                                .background(Styles.RED)
                                                .clip(Rect(24.px))
                                                .noShrink()
                                        ) {
                                            AssetSvgButton(
                                                id = "subscribe_button",
                                                onClick = {},
                                                text = "Subscribe",
                                            )
                                        }
                                    }
                                }

                                if (isSmallBreakpoint) {
                                    Column(
                                        modifier = Modifier.margin(
                                            right = ShortActionsDefaults.WIDTH + 16.px
                                        ),
                                        verticalArrangement = Arrangement.spacedBy(20.px)
                                    ) {
                                        content()
                                    }
                                } else {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(20.px),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        content()
                                    }
                                }

                                Box(Modifier.limitTextWithEllipsis(maxLines = 2)) {
                                    Text("Put this cat in jail")
                                }
                            }
                        }

                        ShortActions(
                            modifier = Modifier
                                .noShrink()
                                .margin(
                                    right = if (isSmallBreakpoint && !isShortWindowState.value) 24.px else 0.px,
                                    bottom = if (isSmallBreakpoint) 24.px else 0.px,
                                ),
                            isShortWindowState = isShortWindowState,
                        )
                    }
                }

                val modifier = remember(breakpoint) {
                    Modifier
                        .padding(topBottom = 12.px)
                        .scrollSnapAlign(ScrollSnapAlign.Start)
                        .scrollSnapStop(ScrollSnapStop.Always)
                        .size(
                            width = when (breakpoint) {
                                Breakpoint.ZERO -> 95.percent
                                Breakpoint.SM -> 450.px
                                Breakpoint.MD -> 500.px
                                Breakpoint.LG -> 560.px
                                Breakpoint.XL -> 600.px
                            },
                            height = if (isShortWindowState.value) 80.vh else 85.vh,
                        )
                }

                if (isSmallBreakpoint) {
                    Box(
                        modifier = modifier,
                        contentAlignment = Alignment.BottomEnd,
                    ) {
                        content(Modifier)
                    }
                } else {
                    Row(
                        modifier = modifier,
                        horizontalArrangement = Arrangement.spacedBy(25.px),
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        content(Modifier.weight(1))
                    }
                }
            }
        }

        Box(modifier = Modifier.margin(if (isSmallBreakpoint) 24.px else 0.px)) {
            AssetImageButton(
                asset = Assets.Icons.ARROW_LEFT,
                modifier = Modifier.background(Styles.ARROW_BUTTON_CONTAINER),
                onClick = onBackPressed
            )
        }
    }
}

@Composable
private fun ShortActions(
    isShortWindowState: State<Boolean>,
    modifier: Modifier = Modifier,
    isLiked: Boolean = false,
    likeCount: Int = 600,
    onLike: () -> Unit = {},
    isDisliked: Boolean = false,
    dislikeCount: Int = 35,
    onDislike: () -> Unit = {},
    commentCount: Int = 2765,
    onOpenComments: () -> Unit = {},
    shareCount: Int = 52,
    onShare: () -> Unit = {},
    onOpenMore: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .overflow { y(Overflow.Scroll) }
            .width(ShortActionsDefaults.WIDTH)
            .zIndex(1)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(if (isShortWindowState.value) 12.px else 21.px)
    ) {
        ShortAction.entries.forEach { action ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.px)
            ) {
                AssetImageButton(
                    modifier = Modifier
                        .background(Styles.ARROW_BUTTON_CONTAINER)
                        .padding(if (isShortWindowState.value) 8.px else 16.px),
                    asset = when (action) {
                        ShortAction.Like ->
                            if (isLiked) Assets.Icons.LIKED_SELECTED else Assets.Icons.LIKED

                        ShortAction.Dislike ->
                            if (isDisliked) Assets.Icons.DISLIKE_SELECTED else Assets.Icons.DISLIKE

                        ShortAction.Comment -> Assets.Icons.COMMENTS
                        ShortAction.Share -> Assets.Icons.SHARE
                        ShortAction.More -> Assets.Icons.MORE
                    },
                    onClick = when (action) {
                        ShortAction.Like -> onLike
                        ShortAction.Dislike -> onDislike
                        ShortAction.Comment -> onOpenComments
                        ShortAction.Share -> onShare
                        ShortAction.More -> onOpenMore
                    }
                )

                if (action != ShortAction.More && !isShortWindowState.value) {
                    Box(Modifier.fontWeight(FontWeight.Medium).fontSize(16.px)) {
                        Text(
                            when (action) {
                                ShortAction.Like -> likeCount
                                ShortAction.Dislike -> dislikeCount
                                ShortAction.Comment -> commentCount
                                ShortAction.Share -> shareCount
                                ShortAction.More -> 0
                            }.toString(),
                        )
                    }
                }
            }
        }
    }
}

private object ShortActionsDefaults {
    val WIDTH = 56.px
}

private enum class ShortAction { Like, Dislike, Comment, Share, More }
