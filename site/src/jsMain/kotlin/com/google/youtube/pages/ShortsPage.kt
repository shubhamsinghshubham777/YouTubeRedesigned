package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.sections.TopBarDefaults
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.FilterRow
import com.google.youtube.components.widgets.ShortThumbnailCard
import com.google.youtube.components.widgets.ShortThumbnailCardDefaults
import com.google.youtube.components.widgets.SubscribeButton
import com.google.youtube.data.ShortsDataProvider
import com.google.youtube.models.ShortThumbnailDetails
import com.google.youtube.utils.Asset
import com.google.youtube.utils.BasicGrid
import com.google.youtube.utils.Constants
import com.google.youtube.utils.GridGap
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.hideScrollBar
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberBreakpointAsState
import com.google.youtube.utils.rememberIsShortWindowAsState
import com.google.youtube.utils.rememberIsSmallBreakpoint
import com.varabyte.kobweb.browser.dom.observers.IntersectionObserver
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
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.dom.registerRefScope
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
import com.varabyte.kobweb.compose.ui.modifiers.onClick
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
import com.varabyte.kobweb.silk.components.overlay.PopupPlacement
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
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
import org.jetbrains.compose.web.dom.Video
import org.w3c.dom.Element
import org.w3c.dom.HTMLVideoElement

@Composable
fun ShortsGrid(showPersonalisedFeedDialogState: MutableState<Boolean>) {
    val shortsDataProvider = remember { ShortsDataProvider() }
    val shorts = remember(shortsDataProvider) { shortsDataProvider.provideAllShorts() }

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
            shorts.forEach { data -> ShortThumbnailCard(data) }
        }
    }
}

@Composable
fun ShortDetails(id: String, onBackPressed: () -> Unit) {
    val isSmallBreakpoint by rememberIsSmallBreakpoint()
    var containerRef by remember { mutableStateOf<Element?>(null) }

    // Data States
    val shortsDataProvider = remember { ShortsDataProvider() }
    val short = remember(shortsDataProvider) { shortsDataProvider.findShortById(id) }
    val suggestedShorts = remember(shortsDataProvider) {
        shortsDataProvider.getShortSuggestionsForId(id)
    }
    val allShorts = remember(short, suggestedShorts) {
        buildList {
            add(short)
            addAll(suggestedShorts)
        }
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
                .toAttrs {
                    ref { e ->
                        containerRef = e
                        onDispose {}
                    }
                },
        ) {
            allShorts.forEach { details ->
                ShortVideoPlayer(details = details, rootElement = containerRef)
            }
        }

        Box(modifier = Modifier.margin(if (isSmallBreakpoint) 24.px else 0.px)) {
            AssetImageButton(
                asset = Asset.Icon.ARROW_LEFT,
                modifier = Modifier.background(Styles.ARROW_BUTTON_CONTAINER),
                onClick = onBackPressed
            )
        }
    }
}

@Composable
private fun ShortVideoPlayer(
    details: ShortThumbnailDetails,
    rootElement: Element?,
    shape: Shape = Rect(16.px),
) {
    val breakpoint by rememberBreakpointAsState()
    val isShortWindowState = rememberIsShortWindowAsState()
    val isSmallBreakpoint by rememberIsSmallBreakpoint()

    // Player States
    var playerRef by remember { mutableStateOf<HTMLVideoElement?>(null) }
    val togglePlayPause: () -> Unit = remember(playerRef) {
        {
            playerRef?.let { player -> with(player) { if (paused) play() else pause() } }
        }
    }
    var isActive by remember { mutableStateOf(false) }

    val content = remember(isSmallBreakpoint) {
        movableContentOf { modifier: Modifier ->
            Box(
                ref = ref { e ->
                    val observer = IntersectionObserver(
                        IntersectionObserver.Options(root = rootElement, thresholds = listOf(0.5))
                    ) { entries ->
                        entries.firstOrNull()?.let { entry -> isActive = entry.isIntersecting }
                    }
                    observer.observe(e)
                },
                modifier = Modifier.clip(shape).fillMaxHeight().then(modifier),
                contentAlignment = Alignment.BottomStart,
            ) {
                Video(
                    attrs = Modifier
                        .background(Styles.BLACK)
                        .fillMaxSize()
                        .objectFit(ObjectFit.Cover)
                        .onClick { togglePlayPause() }
                        .toAttrs(),
                ) {
                    val videoPlayerRef = ref<HTMLVideoElement> { videoPlayer ->
                        playerRef = videoPlayer
                        with(videoPlayer) {
                            this.controls = false
                            this.loop = true
                            this.src = "/videos/SampleVideo_1280x720_10mb.mp4"
                            this.volume = 1.0
                        }
                    }
                    registerRefScope(videoPlayerRef)
                }

                // Gradient / Scrim
                Box(
                    Modifier
                        .fillMaxSize()
                        .backgroundImage(
                            linearGradient(LinearGradient.Direction.ToTop) {
                                add(Colors.Black.copyf(alpha = 0.41f))
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
                                    src = details.channelAsset,
                                )

                                Column(
                                    modifier = Modifier.weight(1),
                                    verticalArrangement = Arrangement.spacedBy(4.px)
                                ) {
                                    TextBox(
                                        maxLines = 1,
                                        size = 18,
                                        text = details.channelName,
                                        weight = FontWeight.Medium,
                                    )
                                    TextBox(
                                        maxLines = 1,
                                        modifier = Modifier.opacity(0.63f),
                                        size = 14,
                                        text = "${details.subscribersCount} subscribers",
                                    )
                                }
                            }

                            SubscribeButton(popupPlacement = PopupPlacement.TopLeft)
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

                    TextBox(text = details.title, maxLines = 2)
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

    LaunchedEffect(isActive) { if (isActive) playerRef?.play() else playerRef?.pause() }

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
                            if (isLiked) Asset.Icon.LIKED_SELECTED else Asset.Icon.LIKED

                        ShortAction.Dislike ->
                            if (isDisliked) Asset.Icon.DISLIKE_SELECTED else Asset.Icon.DISLIKE

                        ShortAction.Comment -> Asset.Icon.COMMENTS
                        ShortAction.Share -> Asset.Icon.SHARE
                        ShortAction.More -> Asset.Icon.MORE
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
