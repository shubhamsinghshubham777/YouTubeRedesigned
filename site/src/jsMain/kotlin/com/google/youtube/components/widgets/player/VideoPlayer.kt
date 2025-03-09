package com.google.youtube.components.widgets.player

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.ContextMenu
import com.google.youtube.components.widgets.ContextMenuAnimationDirection
import com.google.youtube.components.widgets.context.ContextMenuPage
import com.google.youtube.components.widgets.context.VideoOptionPage
import com.google.youtube.pages.SegmentedContentType
import com.google.youtube.utils.Asset
import com.google.youtube.utils.Constants
import com.google.youtube.utils.FadeInOut
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.rememberElementWidthAsState
import com.google.youtube.utils.rememberIsLargeBreakpoint
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.dom.registerRefScope
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
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onDoubleClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import kotlinx.browser.window
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Video
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLVideoElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

@Composable
fun VideoPlayer(
    videoId: String,
    isTheaterModeOn: MutableState<Boolean>,
    selectedSegment: MutableState<SegmentedContentType>,
) {
    val isLargeBreakpoint by rememberIsLargeBreakpoint()

    var parentBoxRef by remember { mutableStateOf<Element?>(null) }
    var htmlVideoPlayerRef by remember { mutableStateOf<HTMLVideoElement?>(null) }
    var playPauseBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var nextBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var muteBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var ccBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var videoQualityBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var settingsBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var liveChatBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var pipBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var theaterModeBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var fullScreenBtnRef by remember { mutableStateOf<HTMLElement?>(null) }

    var isPlaying by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }
    var isCCEnabled by remember { mutableStateOf(false) }
    var isFullScreenEnabled by remember { mutableStateOf(false) }

    // Popup States
    var openedPopupType by remember { mutableStateOf<PopupType?>(null) }
    val popupModifier = remember { Modifier.margin(bottom = 8.px, right = 8.px).width(315.px) }
    val animatedSettingsBtnRotation by animateFloatAsState(
        if (openedPopupType == PopupType.VideoOptions) 45f else 0f
    )
    val annotationSwitchState = remember { mutableStateOf(false) }
    val stickyOnScrollState = remember { mutableStateOf(false) }
    val stableVolumeState = remember { mutableStateOf(false) }
    val ambientModeState = remember { mutableStateOf(false) }
    val selectedVideoQuality = remember { mutableStateOf(VideoQuality.P4K) }

    var volume by remember { mutableFloatStateOf(1f) }
    val animatedVolBtnOpacity by animateFloatAsState(if (volume == 0f) 0.5f else 1f)
    var isVolumeBarHovered by remember { mutableStateOf(false) }

    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgressState = animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 250, easing = LinearEasing),
    )

    val animatedControlsOpacity by animateFloatAsState(
        if (isHovered || !isPlaying || openedPopupType != null) 1f else 0f
    )

    val playerWidth by rememberElementWidthAsState(parentBoxRef)
    val displayCCButton by remember { derivedStateOf { (playerWidth ?: 0.0) > 560 } }
    val displayQualityButton by remember { derivedStateOf { (playerWidth ?: 0.0) > 520 } }
    val displayChatButton by remember { derivedStateOf { (playerWidth ?: 0.0) > 480 } }
    val displayPIPButton by remember { derivedStateOf { (playerWidth ?: 0.0) > 440 } }

    val toggleFullScreen: () -> Unit = remember(parentBoxRef, isFullScreenEnabled) {
        {
            if (isFullScreenEnabled) window.document.exitFullscreen()
            else parentBoxRef?.requestFullscreen()

            isFullScreenEnabled = !isFullScreenEnabled
        }
    }

    val togglePlayPause: () -> Unit = remember(htmlVideoPlayerRef) {
        {
            htmlVideoPlayerRef?.let { player -> with(player) { if (paused) play() else pause() } }
        }
    }

    val toggleMute: () -> Unit = remember(htmlVideoPlayerRef) {
        {
            htmlVideoPlayerRef?.let { player ->
                player.volume = if (player.volume == 0.0) 1.0 else 0.0
            }
        }
    }

    LaunchedEffect(htmlVideoPlayerRef) {
        htmlVideoPlayerRef?.let { player ->
            with(player) {
                onplay = {
                    isPlaying = true
                    null
                }
                onpause = {
                    isPlaying = false
                    null
                }
                ontimeupdate = {
                    progress = (currentTime / duration).times(100).toFloat()
                    null
                }
                onvolumechange = {
                    volume = this.volume.toFloat()
                    null
                }
                play()
            }
        }
    }

    DisposableEffect(Unit) {
        val keyUpListener: (Event) -> Unit = { event: Event ->
            val key = (event as KeyboardEvent).key
            htmlVideoPlayerRef?.let { player ->
                when (key) {
                    "t" -> isTheaterModeOn.value = !isTheaterModeOn.value
                    "f" -> toggleFullScreen()
                    " ", "k" -> togglePlayPause()
                    "ArrowLeft" -> player.currentTime -= 3.0
                    "j" -> player.currentTime -= 10.0
                    "ArrowRight" -> player.currentTime += 3.0
                    "l" -> player.currentTime += 10.0
                    "m" -> toggleMute()
                    "c" -> isCCEnabled = !isCCEnabled
                    "Escape" -> openedPopupType = null
                }
            }
        }

        val keyDownListener: (Event) -> Unit = { event: Event ->
            val key = (event as KeyboardEvent).key
            htmlVideoPlayerRef?.let { player ->
                when (key) {
                    "ArrowDown" -> {
                        event.preventDefault()
                        player.volume -= 0.2
                    }

                    "ArrowUp" -> {
                        event.preventDefault()
                        player.volume += 0.2
                    }
                }
            }
        }

        window.addEventListener("keyup", keyUpListener)
        window.addEventListener("keydown", keyDownListener)
        onDispose {
            window.removeEventListener("keyup", keyUpListener)
            window.removeEventListener("keydown", keyDownListener)
        }
    }

    LaunchedEffect(videoId) { htmlVideoPlayerRef?.currentTime = 0.0 }

    // Close popup on video quality change
    LaunchedEffect(selectedVideoQuality.value) { openedPopupType = null }

    Box(
        ref = ref { r -> parentBoxRef = r },
        modifier = Modifier
            .aspectRatio(width = 16, height = 9)
            .maxHeight(85.vh)
            .clip(Rect(14.px))
            .fillMaxSize()
            .onMouseOver { isHovered = true }
            .onMouseOut { isHovered = false },
        contentAlignment = Alignment.BottomCenter,
    ) {
        Video(
            attrs = Modifier
                .fillMaxSize()
                .background(Styles.BLACK)
                .onClick {
                    if (openedPopupType != null) openedPopupType = null
                    else togglePlayPause()
                }
                .onDoubleClick { toggleFullScreen() }
                .toAttrs()
        ) {
            val videoPlayerRef = ref<HTMLVideoElement> { videoPlayer ->
                htmlVideoPlayerRef = videoPlayer
                with(videoPlayer) {
                    this.controls = false
                    this.src = "/videos/SampleVideo_1280x720_10mb.mp4"
                    this.volume = 1.0
                }
            }
            registerRefScope(videoPlayerRef)
        }

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            // All Player Popups
            Box(
                modifier = Modifier.align(Alignment.End).weight(1),
                contentAlignment = Alignment.BottomCenter,
            ) {
                // Video Options Popup
                FadeInOut(
                    isVisible = openedPopupType == PopupType.VideoOptions,
                    modifier = popupModifier,
                ) {
                    var selectedPopupPage by remember { mutableStateOf(VideoOptionPage.Main) }

                    ContextMenu(
                        modifier = Modifier.fillMaxWidth(),
                        state = selectedPopupPage,
                        directionProvider = { page ->
                            when (page) {
                                VideoOptionPage.Main -> ContextMenuAnimationDirection.ToRight
                                VideoOptionPage.MoreOptions -> ContextMenuAnimationDirection.ToLeft
                            }
                        },
                    ) { page ->
                        when (page) {
                            VideoOptionPage.Main -> ContextMenuPage.videoOptionsMain(
                                onMoreOptionsClick = {
                                    selectedPopupPage = VideoOptionPage.MoreOptions
                                },
                            )

                            VideoOptionPage.MoreOptions -> ContextMenuPage.videoOptionsMore(
                                annotationSwitchState = annotationSwitchState,
                                stickyOnScrollState = stickyOnScrollState,
                                stableVolumeState = stableVolumeState,
                                ambientModeState = ambientModeState,
                                onBackClick = { selectedPopupPage = VideoOptionPage.Main },
                            )
                        }
                    }
                }

                // Video Quality Popup
                FadeInOut(
                    isVisible = openedPopupType == PopupType.QualityOptions,
                    modifier = popupModifier,
                ) {
                    ContextMenu(modifier = Modifier.fillMaxWidth(), state = Unit) {
                        ContextMenuPage.videoQuality(qualityState = selectedVideoQuality)
                    }
                }
            }

            // Controls
            Column(
                modifier = Modifier
                    .onClick { openedPopupType = null }
                    .opacity(animatedControlsOpacity)
                    .backgroundImage(
                        linearGradient(LinearGradient.Direction.ToTop) {
                            add(Styles.BLACK.copyf(alpha = 0.5f))
                            add(Colors.Transparent)
                        }
                    )
                    .fillMaxWidth()
                    .padding(leftRight = 12.px, bottom = 6.px)
                    .zIndex(1),
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
                            .fillMaxWidth(percent = animatedProgressState.value.percent)
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
                        asset = if (isPlaying) Asset.Icon.PAUSE else Asset.Icon.PLAY,
                        onRefAvailable = { playPauseBtnRef = it },
                        onClick = togglePlayPause,
                    )

                    playPauseBtnRef?.let {
                        Tooltip(
                            target = ElementTarget.of(it),
                            text = "${if (isPlaying) "Pause" else "Play"} (Space / k)",
                            showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                        )
                    }

                    AssetImageButton(
                        asset = Asset.Icon.NEXT,
                        onRefAvailable = { nextBtnRef = it },
                    ) {}

                    nextBtnRef?.let {
                        Tooltip(
                            target = ElementTarget.of(it),
                            text = "Next",
                            showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                        )
                    }

                    SpacedRow(
                        spacePx = 8,
                        modifier = Modifier
                            .onMouseEnter { isVolumeBarHovered = true }
                            .onMouseLeave { isVolumeBarHovered = false },
                    ) {
                        AssetImageButton(
                            asset = Asset.Icon.VOLUME,
                            modifier = Modifier.opacity(animatedVolBtnOpacity),
                            onRefAvailable = { muteBtnRef = it },
                            onClick = toggleMute,
                        )
                        muteBtnRef?.let {
                            Tooltip(
                                target = ElementTarget.of(it),
                                text = "${if (volume == 0f) "Unmute" else "Mute"} (m)",
                                showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                            )
                        }
                        VolumeSlider(
                            progress = volume,
                            onProgressChanged = { vol ->
                                htmlVideoPlayerRef?.volume = vol.toDouble()
                            },
                        )
                    }

                    Spacer()

                    if (displayCCButton) {
                        AssetImageButton(
                            asset = if (isCCEnabled) Asset.Icon.CC_SELECTED else Asset.Icon.CC,
                            onRefAvailable = { ccBtnRef = it },
                        ) { isCCEnabled = !isCCEnabled }

                        ccBtnRef?.let {
                            Tooltip(
                                target = ElementTarget.of(it),
                                text = "Closed Captions (c)",
                                showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                            )
                        }
                    }

                    if (displayQualityButton) {
                        AssetImageButton(
                            asset = when (selectedVideoQuality.value) {
                                VideoQuality.P144 -> Asset.Icon.QUALITY_144
                                VideoQuality.P240 -> Asset.Icon.QUALITY_240
                                VideoQuality.P360 -> Asset.Icon.QUALITY_360
                                VideoQuality.P480 -> Asset.Icon.QUALITY_480
                                VideoQuality.P720 -> Asset.Icon.QUALITY_720
                                VideoQuality.P1080 -> Asset.Icon.QUALITY_HD
                                VideoQuality.P4K -> Asset.Icon.QUALITY_4K
                            },
                            onRefAvailable = { videoQualityBtnRef = it },
                        ) {
                            openedPopupType = if (openedPopupType == PopupType.QualityOptions) null
                            else PopupType.QualityOptions
                        }

                        videoQualityBtnRef?.let {
                            Tooltip(
                                target = ElementTarget.of(it),
                                text = "Quality",
                                showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                            )
                        }
                    }

                    AssetImageButton(
                        modifier = Modifier.rotate(animatedSettingsBtnRotation.deg),
                        asset = Asset.Icon.SETTINGS,
                        onRefAvailable = { settingsBtnRef = it },
                    ) {
                        openedPopupType = if (openedPopupType == PopupType.VideoOptions) null
                        else PopupType.VideoOptions
                    }

                    settingsBtnRef?.let {
                        Tooltip(
                            target = ElementTarget.of(it),
                            text = "Settings",
                            showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                        )
                    }

                    if (displayChatButton && !isFullScreenEnabled) {
                        AssetImageButton(
                            asset = Asset.Icon.CHAT,
                            onRefAvailable = { liveChatBtnRef = it },
                        ) {
                            isTheaterModeOn.value = false
                            selectedSegment.value = SegmentedContentType.LiveChat
                        }

                        liveChatBtnRef?.let {
                            Tooltip(
                                target = ElementTarget.of(it),
                                text = "Live Chat",
                                showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                            )
                        }
                    }

                    if (displayPIPButton) {
                        AssetImageButton(
                            asset = Asset.Icon.PIP,
                            onRefAvailable = { pipBtnRef = it },
                        ) {}

                        pipBtnRef?.let {
                            Tooltip(
                                target = ElementTarget.of(it),
                                text = "Picture-in-Picture mode",
                                showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                            )
                        }
                    }

                    if (isLargeBreakpoint && !isFullScreenEnabled) {
                        AssetImageButton(
                            asset = if (isTheaterModeOn.value) Asset.Icon.THEATER
                            else Asset.Icon.THEATER_SELECTED,
                            onRefAvailable = { theaterModeBtnRef = it },
                            onClick = { isTheaterModeOn.value = !isTheaterModeOn.value },
                        )

                        theaterModeBtnRef?.let {
                            Tooltip(
                                target = ElementTarget.of(it),
                                text = "Theater Mode${if (isTheaterModeOn.value) " Off" else " On"} (t)",
                                showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                            )
                        }
                    }

                    AssetImageButton(
                        asset = if (isFullScreenEnabled) Asset.Icon.FULLSCREEN_SELECTED
                        else Asset.Icon.FULLSCREEN,
                        onRefAvailable = { fullScreenBtnRef = it },
                        onClick = toggleFullScreen,
                    )

                    fullScreenBtnRef?.let {
                        Tooltip(
                            target = ElementTarget.of(it),
                            text = "Fullscreen (f)",
                            showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                        )
                    }
                }
            }
        }
    }
}

enum class VideoQuality { P144, P240, P360, P480, P720, P1080, P4K }
private enum class PopupType { VideoOptions, QualityOptions }
