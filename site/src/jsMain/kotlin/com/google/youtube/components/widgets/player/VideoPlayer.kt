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
import com.google.youtube.pages.SegmentedContentType
import com.google.youtube.utils.Assets
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
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onDoubleClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOut
import com.varabyte.kobweb.compose.ui.modifiers.onMouseOver
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import kotlinx.browser.window
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Video
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLVideoElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

@Composable
fun VideoPlayer(
    isTheaterModeOn: MutableState<Boolean>,
    selectedSegment: MutableState<SegmentedContentType>,
) {
    val isLargeBreakpoint by rememberIsLargeBreakpoint()

    var parentBoxRef by remember { mutableStateOf<Element?>(null) }
    var htmlVideoPlayerRef by remember { mutableStateOf<HTMLVideoElement?>(null) }
    var ccBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var settingsBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var liveChatBtnRef by remember { mutableStateOf<HTMLElement?>(null) }

    var isPlaying by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }

    var volume by remember { mutableFloatStateOf(1f) }
    val animatedVolBtnOpacity by animateFloatAsState(if (volume == 0f) 0.5f else 1f)
    var isVolumeBarHovered by remember { mutableStateOf(false) }

    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 250, easing = LinearEasing),
    )

    var isCCEnabled by remember { mutableStateOf(false) }

    val animatedControlsOpacity by animateFloatAsState(if (isHovered || !isPlaying) 1f else 0f)

    val playerWidth by rememberElementWidthAsState(parentBoxRef)
    val displayCCButton by remember { derivedStateOf { (playerWidth ?: 0.0) > 560 } }
    val displayQualityButton by remember { derivedStateOf { (playerWidth ?: 0.0) > 520 } }
    val displayChatButton by remember { derivedStateOf { (playerWidth ?: 0.0) > 480 } }
    val displayPIPButton by remember { derivedStateOf { (playerWidth ?: 0.0) > 440 } }
    var isFullScreenEnabled by remember { mutableStateOf(false) }

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

    Box(
        ref = ref { r -> parentBoxRef = r },
        modifier = Modifier
            .aspectRatio(width = 16, height = 7.75)
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
                .onClick { togglePlayPause() }
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

        // Controls
        Column(
            modifier = Modifier
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
                        .fillMaxWidth(percent = animatedProgress.percent)
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
                    asset = if (isPlaying) Assets.Icons.PAUSE else Assets.Icons.PLAY,
                    onClick = togglePlayPause,
                )

                AssetImageButton(Assets.Icons.NEXT) {}

                SpacedRow(
                    spacePx = 8,
                    modifier = Modifier
                        .onMouseEnter { isVolumeBarHovered = true }
                        .onMouseLeave { isVolumeBarHovered = false },
                ) {
                    AssetImageButton(
                        asset = Assets.Icons.VOLUME,
                        modifier = Modifier.opacity(animatedVolBtnOpacity),
                        onClick = toggleMute,
                    )
                    VolumeSlider(
                        progress = volume,
                        onProgressChanged = { vol -> htmlVideoPlayerRef?.volume = vol.toDouble() },
                    )
                }

                Spacer()

                if (displayCCButton) {
                    AssetImageButton(
                        asset = if (isCCEnabled) Assets.Icons.CC_SELECTED else Assets.Icons.CC,
                        onRefAvailable = { ccBtnRef = it },
                    ) { isCCEnabled = !isCCEnabled }

                    ccBtnRef?.let { Tooltip(ElementTarget.of(it), "Closed Captions (C)") }
                }

                if (displayQualityButton) AssetImageButton(Assets.Icons.QUALITY_4K)

                AssetImageButton(
                    asset = Assets.Icons.SETTINGS,
                    onRefAvailable = { settingsBtnRef = it }
                ) {}

                settingsBtnRef?.let { Tooltip(ElementTarget.of(it), "Settings") }

                if (displayChatButton) {
                    AssetImageButton(
                        asset = Assets.Icons.CHAT,
                        onRefAvailable = { liveChatBtnRef = it },
                    ) {
                        isTheaterModeOn.value = false
                        selectedSegment.value = SegmentedContentType.LiveChat
                    }

                    liveChatBtnRef?.let { Tooltip(ElementTarget.of(it), "Live Chat") }
                }

                if (displayPIPButton) AssetImageButton(Assets.Icons.PIP) {}

                if (isLargeBreakpoint && !isFullScreenEnabled) {
                    AssetImageButton(
                        asset = if (isTheaterModeOn.value) Assets.Icons.THEATER
                        else Assets.Icons.THEATER_SELECTED,
                        onClick = { isTheaterModeOn.value = !isTheaterModeOn.value },
                    )
                }

                AssetImageButton(
                    asset = if (isFullScreenEnabled) Assets.Icons.FULLSCREEN_SELECTED
                    else Assets.Icons.FULLSCREEN,
                    onClick = toggleFullScreen,
                )
            }
        }
    }
}
