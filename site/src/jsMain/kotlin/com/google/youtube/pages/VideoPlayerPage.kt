package com.google.youtube.pages

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.components.widgets.player.PlayerAndComments
import com.google.youtube.components.widgets.player.SegmentedContent
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Styles
import com.google.youtube.utils.clickable
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberIsLargeBreakpoint
import com.google.youtube.utils.rememberWindowWidthAsState
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.PointerEvents
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.pointerEvents
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.thenIf
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.times
import org.jetbrains.compose.web.css.vh

@Composable
fun VideoPlayerPage(
    videoId: String,
    modifier: Modifier = Modifier,
) {
    val isLargeBreakpoint by rememberIsLargeBreakpoint()
    val isTheaterModeOn = remember { mutableStateOf(false) }

    // Segment States
    val selectedSegment = remember { mutableStateOf(SegmentedContentType.Suggestions) }
    val isSegmentedContentVisible = remember { mutableStateOf(false) }
    val windowWidth by rememberWindowWidthAsState()
    val animatedFixedSegmentSizeFactor by animateFloatAsState(
        when {
            !isLargeBreakpoint -> 0f
            isLargeBreakpoint && isTheaterModeOn.value -> 0f
            else -> 1f
        }
    )
    val shouldDisplayFixedSegmentContent by remember {
        derivedStateOf { animatedFixedSegmentSizeFactor != 0f }
    }
    val animatedFloatingSegmentWidth by animateFloatAsState(
        if (isSegmentedContentVisible.value) 473f.coerceAtMost(windowWidth * 0.8f)
        else 0f,
    )

    val segmentContent = remember {
        movableContentOf { modifier: Modifier ->
            SegmentedContent(videoId = videoId, state = selectedSegment, modifier = modifier)
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
        Row(modifier = Modifier.fillMaxSize().then(modifier)) {
            PlayerAndComments(
                modifier = Modifier.weight(1),
                isTheaterModeOn = isTheaterModeOn,
                isSegmentedContentVisible = isSegmentedContentVisible,
                selectedSegment = selectedSegment,
                videoId = videoId,
                segmentedContent = if (!shouldDisplayFixedSegmentContent && isLargeBreakpoint) {
                    {
                        segmentContent(Modifier.width(SUGGESTIONS_SECTION_MAX_WIDTH))
                    }
                } else null,
            )
            Box(modifier = Modifier.width(34.px * animatedFixedSegmentSizeFactor))
            if (shouldDisplayFixedSegmentContent) {
                segmentContent(
                    Modifier
                        .noShrink()
                        .opacity(animatedFixedSegmentSizeFactor)
                        .width(SUGGESTIONS_SECTION_MAX_WIDTH * animatedFixedSegmentSizeFactor)
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
            Row(
                modifier = Modifier
                    .background(Styles.SURFACE_ELEVATED)
                    .borderRadius(12.px)
                    .height(75.vh)
                    .margin(left = 12.px)
                    .overflow { y(Overflow.Scroll) }
                    .width(animatedFloatingSegmentWidth.px)
                    .zIndex(1),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(left = 16.px, top = 16.px, right = 16.px),
                    content = { segmentContent(Modifier.fillMaxSize()) },
                )
            }
        }
    }
}

enum class SegmentedContentType(val label: String) {
    Suggestions("Suggestions"),
    Transcripts("Transcripts"),
    LiveChat("Live Chat")
}

private val SUGGESTIONS_SECTION_MAX_WIDTH = 411.px
