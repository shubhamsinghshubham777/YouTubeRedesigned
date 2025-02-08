package com.google.youtube.pages

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.SegmentedButton
import com.google.youtube.components.widgets.comments.CommentsSection
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.animatedColor
import com.google.youtube.utils.clickable
import com.google.youtube.utils.isGreaterThan
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberBreakpointAsState
import com.google.youtube.utils.rememberElementWidthAsState
import com.google.youtube.utils.rememberMouseEventAsState
import com.google.youtube.utils.rememberWindowWidthAsState
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.PointerEvents
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.aspectRatio
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.columnGap
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.pointerEvents
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.rowGap
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.times
import org.jetbrains.compose.web.css.vh
import org.w3c.dom.Element

@Composable
fun VideoPlayerPage(
    videoId: String,
    modifier: Modifier = Modifier,
) {
    val breakpoint by rememberBreakpointAsState()
    val isLargeBreakpoint by remember { derivedStateOf { breakpoint.isGreaterThan(Breakpoint.LG) } }
    val isTheaterModeOn = remember { mutableStateOf(false) }

    // Segment States
    val selectedSegmentIndexState = remember { mutableIntStateOf(0) }
    val isSegmentedContentVisible = remember { mutableStateOf(false) }
    val windowWidth by rememberWindowWidthAsState()
    val animatedFixedSegmentSizeFactor by animateFloatAsState(
        when {
            !isLargeBreakpoint -> 0f
            isLargeBreakpoint && isTheaterModeOn.value -> 0f
            else -> 1f
        }
    )
    val animatedFloatingSegmentWidth by animateFloatAsState(
        if (isSegmentedContentVisible.value) 473f.coerceAtMost(windowWidth * 0.8f)
        else 0f,
    )
    val animatedSegmentOpenerRotation by animateFloatAsState(
        if (isSegmentedContentVisible.value) 180f else 0f
    )
    val segmentContent = remember {
        movableContentOf { modifier: Modifier ->
            SegmentedContent(
                modifier = modifier,
                indexState = selectedSegmentIndexState
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
        Row(modifier = Modifier.fillMaxSize().then(modifier)) {
            PlayerAndComments(
                modifier = Modifier.weight(1),
                isTheaterModeOn = isTheaterModeOn,
                videoId = videoId,
            )
            Box(modifier = Modifier.width(34.px * animatedFixedSegmentSizeFactor))
            if (animatedFixedSegmentSizeFactor != 0f) {
                segmentContent(
                    Modifier
                        .opacity(animatedFixedSegmentSizeFactor)
                        .width(411.px * animatedFixedSegmentSizeFactor)
                )
            }
        }
        if (!isLargeBreakpoint) {
            AnimatedVisibility(
                isVisible = isSegmentedContentVisible.value,
                modifier = Modifier
                    .fillMaxSize()
                    .thenIf(!isSegmentedContentVisible.value) { Modifier.pointerEvents(PointerEvents.None) }
                    .zIndex(1),
                skipDelay = true,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Styles.SURFACE.copyf(alpha = 0.5f))
                        .clickable(noPointer = true) { isSegmentedContentVisible.value = false }
                )
            }
            Box(modifier = Modifier.zIndex(1), contentAlignment = Alignment.CenterEnd) {
                Row(
                    modifier = Modifier
                        .background(Styles.SURFACE_ELEVATED)
                        .borderRadius(12.px)
                        .height(75.vh)
                        .margin(left = 12.px)
                        .overflow { y(Overflow.Scroll) }
                        .width(animatedFloatingSegmentWidth.px),
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(left = 16.px, top = 16.px, right = 16.px),
                            content = { segmentContent(Modifier.fillMaxSize()) },
                        )
                    },
                )
                AssetImageButton(
                    modifier = Modifier
                        .rotate(animatedSegmentOpenerRotation.deg)
                        .margin(right = 4.px),
                    asset = Assets.Icons.ARROW_LEFT,
                    containerColor = Styles.SURFACE_ELEVATED,
                    onClick = { isSegmentedContentVisible.value = !isSegmentedContentVisible.value }
                )
            }
        }
    }
}

@Composable
private fun SegmentedContent(indexState: MutableIntState, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxSize().noShrink().then(modifier)) {
        SegmentedButton(
            segments = listOf("Suggestions", "Transcripts", "Live Chat"),
            selectedIndex = indexState.value,
            onSegmentClick = { index -> indexState.value = index },
        )
        Crossfade(targetState = indexState.value) { animatedIndex ->
            when (animatedIndex) {
                0 -> SpacedColumn(spacePx = 24, modifier = Modifier.padding(topBottom = 24.px)) {
                    repeat(3) {
                        SuggestionSection(
                            author = "Juxtopossed",
                            videos = List(3) {
                                VideoThumbnailDetails(
                                    thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                                    channelAsset = Assets.Avatars.AVATAR_JACKSEPTICEYE,
                                    title = "I Redesigned the ENTIRE Spotify UI from Scratch",
                                    channelName = "Juxtopposed",
                                    isVerified = true,
                                    views = "1.7M",
                                    daysSinceUploaded = "5 months",
                                    duration = "10:24",
                                )
                            }
                        )
                    }
                }

                1 -> TextBox(
                    modifier = Modifier.fillMaxSize(),
                    text = "Your transcripts will show here"
                )

                else -> TextBox(modifier = Modifier.fillMaxSize(), text = "Live Chat")
            }
        }
    }
}

@Composable
private fun SuggestionSection(author: String, videos: List<VideoThumbnailDetails>) {
    SpacedColumn(spacePx = 14, modifier = Modifier.fillMaxWidth()) {
        TextBox(
            modifier = Modifier.margin(bottom = 2.px),
            text = "From $author",
            size = 14,
        )
        videos.forEach { video ->
            var rowRef by remember { mutableStateOf<Element?>(null) }
            val animatedContainerColor by rememberMouseEventAsState(rowRef).animatedColor()
            SpacedRow(
                ref = ref { e -> rowRef = e },
                spacePx = 8,
                modifier = Modifier
                    .borderRadius(8.px)
                    .fillMaxWidth()
                    .background(animatedContainerColor.toKobwebColor())
                    .clickable {},
            ) {
                Box(
                    modifier = Modifier
                        .clip(Rect(8.px))
                        .noShrink()
                        .size(width = 168.px, height = 94.px),
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize().objectFit(ObjectFit.Cover),
                        src = video.thumbnailAsset
                    )
                    TextBox(
                        modifier = Modifier
                            .background(Styles.BLACK.copyf(alpha = 0.6f))
                            .clip(Rect(4.px))
                            .margin(4.px)
                            .padding(leftRight = 4.px, topBottom = 2.px),
                        text = video.duration,
                    )
                }
                SpacedColumn(4) {
                    TextBox(
                        color = Styles.VIDEO_CARD_PRIMARY_TEXT,
                        lineHeight = 20,
                        maxLines = 2,
                        size = 14,
                        text = video.title,
                        weight = FontWeight.Medium,
                    )
                    Column {
                        SpacedRow(4) {
                            TextBox(
                                color = Styles.VIDEO_CARD_SECONDARY_TEXT,
                                lineHeight = 18,
                                size = 12,
                                text = video.channelName,
                            )
                            if (video.isVerified) {
                                Image(
                                    modifier = Modifier.size(12.px),
                                    src = Assets.Icons.VERIFIED_BADGE,
                                )
                            }
                        }
                        TextBox(
                            color = Styles.VIDEO_CARD_SECONDARY_TEXT,
                            lineHeight = 18,
                            size = 12,
                            text = "${video.views} views • ${video.daysSinceUploaded} ago",
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerAndComments(
    modifier: Modifier = Modifier,
    videoId: String,
    isTheaterModeOn: MutableState<Boolean>,
) {
    var isDescriptionBoxExpanded by remember { mutableStateOf(false) }
    var descriptionToggleRef by remember { mutableStateOf<Element?>(null) }
    val descriptionToggleMouseEventState by rememberMouseEventAsState(descriptionToggleRef)

    Column(modifier = modifier) {
        VideoPlayer(isTheaterModeOn = isTheaterModeOn)

        Box(modifier = Modifier.height(13.px))

        TextBox(
            maxLines = 2,
            size = 20,
            text = "I Redesigned the ENTIRE YouTube UI from Scratch",
            weight = FontWeight.SemiBold,
        )
        Box(modifier = Modifier.height(12.5.px))

        // Video Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .display(DisplayStyle.Flex)
                .flexWrap(FlexWrap.Wrap)
                .columnGap(20.px)
                .rowGap(20.px),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SpacedRow(20) {
                SpacedRow(15) {
                    Image(
                        modifier = Modifier.size(46.px).clip(Circle()),
                        src = Assets.Icons.USER_AVATAR,
                    )
                    SpacedColumn(1.29) {
                        SpacedRow(8) {
                            TextBox(
                                text = "Juxtopposed",
                                size = 18,
                                lineHeight = 28.3,
                                weight = FontWeight.Medium,
                            )
                            Image(src = Assets.Icons.VERIFIED_BADGE)
                        }
                        TextBox(
                            text = "288K subscribers",
                            size = 14,
                            color = Styles.VIDEO_CARD_SECONDARY_TEXT,
                        )
                    }
                }
                AssetSvgButton(
                    id = "subscribe_button",
                    onClick = {},
                    containerColor = Styles.PINK_DARKENED,
                    contentColor = Styles.WHITE,
                    text = "Subscribe",
                )
            }
            SpacedRow(24) {
                IconLabel(iconAsset = Assets.Icons.EYE, label = "120K")
                IconLabel(iconAsset = Assets.Icons.DATE, label = "12 Nov 24")
            }
            SpacedRow(
                spacePx = 10,
                modifier = Modifier
                    .display(DisplayStyle.Flex)
                    .flexWrap(FlexWrap.Wrap)
                    .rowGap(16.px)
            ) {
                SegmentedLikeDislikeButtons()
                AssetImageButton(
                    asset = Assets.Icons.SHARE,
                    containerColor = BUTTON_CONTAINER_COLOR,
                ) {}
                AssetImageButton(
                    asset = Assets.Icons.ADD_TO_PLAYLIST,
                    containerColor = BUTTON_CONTAINER_COLOR,
                ) {}
                AssetImageButton(
                    asset = Assets.Icons.WATCH_LATER,
                    containerColor = BUTTON_CONTAINER_COLOR,
                ) {}
                AssetImageButton(
                    asset = Assets.Icons.MORE_HORIZONTAL,
                    containerColor = BUTTON_CONTAINER_COLOR,
                ) {}
            }
        }

        Box(modifier = Modifier.height(20.5.px))

        // Description
        Box(
            modifier = Modifier
                .border(1.px, LineStyle.Solid, Styles.WHITE.copyf(alpha = 0.1f))
                .borderRadius(15.43.px)
                .fillMaxWidth()
                .padding(leftRight = 24.px, topBottom = 20.px)
                .whiteSpace(WhiteSpace.PreLine)
        ) {
            SpacedColumn(16) {
                TextBox(
                    text = "I tried to redesign YouTube's UI and make it more user-friendly." +
                            "\n\nHope you enjoy!\n\nSubscribe to my YouTube channel for more " +
                            "updates.",
                    maxLines = if (isDescriptionBoxExpanded) null else 3,
                )
                TextBox(
                    ref = ref { e -> descriptionToggleRef = e },
                    modifier = Modifier
                        .clickable { isDescriptionBoxExpanded = !isDescriptionBoxExpanded }
                        .thenIf(descriptionToggleMouseEventState == MouseEventState.Hovered) {
                            Modifier.textDecorationLine(TextDecorationLine.Underline)
                        },
                    color = Styles.WHITE.copyf(alpha = 0.47f),
                    text = if (isDescriptionBoxExpanded) "Show Less" else "Show More",
                )
            }
        }

        Box(modifier = Modifier.height(31.px))

        // Comments
        CommentsSection(modifier = Modifier.fillMaxWidth(), videoId = videoId)
    }
}

@Composable
private fun SegmentedLikeDislikeButtons() {
    Row(
        modifier = Modifier
            .background(BUTTON_CONTAINER_COLOR)
            .clip(Rect(100.px))
            .userSelect(UserSelect.None),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SegmentedLikeDislikeButtonItem(
            modifier = Modifier.padding(left = 16.px, top = 8.px, right = 14.px, bottom = 8.px),
            iconAsset = Assets.Icons.LIKED,
            label = "20K",
            isLabelBold = true,
            onClick = {}
        )
        Box(
            modifier = Modifier
                .background(Styles.WHITE.copyf(alpha = 0.1f))
                .size(width = 1.29.px, height = 28.px)
        )
        SegmentedLikeDislikeButtonItem(
            modifier = Modifier.padding(left = 14.px, top = 8.px, right = 16.px, bottom = 8.px),
            iconAsset = Assets.Icons.DISLIKE,
            label = "100",
            onClick = {}
        )
    }
}

@Composable
private fun SegmentedLikeDislikeButtonItem(
    iconAsset: String,
    label: String,
    modifier: Modifier = Modifier,
    isLabelBold: Boolean = false,
    onClick: () -> Unit,
) {
    var elementRef by remember { mutableStateOf<Element?>(null) }
    val animatedContainerColor by rememberMouseEventAsState(elementRef).animatedColor()
    SpacedRow(
        ref = ref { e -> elementRef = e },
        spacePx = 7,
        modifier = Modifier
            .background(animatedContainerColor.toKobwebColor())
            .clickable(onClick = onClick)
            .then(modifier),
    ) {
        Image(src = iconAsset)
        TextBox(text = label, weight = if (isLabelBold) FontWeight.Medium else FontWeight.Normal)
    }
}

@Composable
private fun IconLabel(
    iconAsset: String,
    label: String,
) {
    SpacedRow(4) {
        Image(
            modifier = Modifier.opacity(0.3f),
            src = iconAsset
        )
        TextBox(label)
    }
}

@Composable
private fun VideoPlayer(isTheaterModeOn: MutableState<Boolean>) {
    var ref by remember { mutableStateOf<Element?>(null) }
    val breakpoint by rememberBreakpointAsState()
    val isLargeBreakpoint by remember { derivedStateOf { breakpoint.isGreaterThan(Breakpoint.LG) } }

    var isPlaying by remember { mutableStateOf(false) }

    val mouseEventState by rememberMouseEventAsState(ref)
    val controlsOpacity by animateFloatAsState(
        if (mouseEventState != MouseEventState.Released || !isPlaying) 1f else 0f
    )

    val width by rememberElementWidthAsState(ref)
    val displayCCButton by remember { derivedStateOf { (width ?: 0.0) > 560 } }
    val displayQualityButton by remember { derivedStateOf { (width ?: 0.0) > 520 } }
    val displayChatButton by remember { derivedStateOf { (width ?: 0.0) > 480 } }
    val displayPIPButton by remember { derivedStateOf { (width ?: 0.0) > 440 } }

    Box(
        ref = ref { r -> ref = r },
        modifier = Modifier
            .aspectRatio(width = 16, height = 7.75)
            .background(Colors.DimGray)
            .clip(Rect(14.px))
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        // Controls
        Column(
            modifier = Modifier
                .opacity(controlsOpacity)
                .backgroundImage(
                    linearGradient(LinearGradient.Direction.ToTop) {
                        add(Styles.BLACK.copyf(alpha = 0.25f))
                        add(Colors.Transparent)
                    }
                )
                .fillMaxWidth()
                .padding(leftRight = 12.px, bottom = 6.px),
            verticalArrangement = Arrangement.spacedBy(6.px),
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(3.px)) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Styles.WHITE.copyf(alpha = 0.25f))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(percent = 40.percent)
                        .fillMaxHeight()
                        .background(Styles.WHITE.copyf(alpha = 0.58f))
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.px),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AssetImageButton(
                    if (isPlaying) Assets.Icons.PAUSE else Assets.Icons.PLAY
                ) { isPlaying = !isPlaying }
                AssetImageButton(Assets.Icons.NEXT) {}
                AssetImageButton(Assets.Icons.VOLUME) {}
                Spacer()
                if (displayCCButton) AssetImageButton(Assets.Icons.CC) {}
                if (displayQualityButton) AssetImageButton(Assets.Icons.QUALITY_4K)
                AssetImageButton(Assets.Icons.SETTINGS) {}
                if (displayChatButton) AssetImageButton(Assets.Icons.CHAT) {}
                if (displayPIPButton) AssetImageButton(Assets.Icons.PIP) {}
                if (isLargeBreakpoint) {
                    AssetImageButton(
                        asset = if (isTheaterModeOn.value) Assets.Icons.THEATER
                        else Assets.Icons.THEATER_SELECTED,
                        onClick = { isTheaterModeOn.value = !isTheaterModeOn.value },
                    )
                }
                AssetImageButton(Assets.Icons.FULLSCREEN) {}
            }
        }
    }
}

private val BUTTON_CONTAINER_COLOR = Styles.WHITE.copyf(alpha = 0.05f)
