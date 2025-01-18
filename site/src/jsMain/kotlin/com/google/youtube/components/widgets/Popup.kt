package com.google.youtube.components.widgets

import androidx.compose.animation.core.animateRectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.toComposeRect
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.left
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent

@Composable
fun <T> Popup(
    anchor: Element?,
    targetState: T,
    modifier: Modifier = Modifier,
    isContentHovered: MutableState<Boolean>? = null,
    content: @Composable (T) -> Unit,
) {
    var anchorBounds by remember { mutableStateOf(anchor?.getBoundingClientRect()) }
    val animatedAnchorBounds by animateRectAsState(anchorBounds?.toComposeRect() ?: Rect.Zero)
    var mouseCoordinates by remember { mutableStateOf(0 to 0) }
    val isMouseInsideAnchorBounds by remember(anchorBounds) {
        derivedStateOf {
            anchorBounds?.toComposeRect()?.contains(
                Offset(
                    x = mouseCoordinates.first.toFloat(),
                    y = mouseCoordinates.second.toFloat()
                )
            ) ?: false
        }
    }
    var contentHeight by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(anchor) { anchor?.let { a -> anchorBounds = a.getBoundingClientRect() } }

    // Mouse observer
    DisposableEffect(Unit) {
        val mouseObserver = { event: Event ->
            with(event as MouseEvent) { mouseCoordinates = clientX to clientY }
        }
        document.addEventListener(type = EVENT_TYPE_MOUSE_MOVE, callback = mouseObserver)
        onDispose {
            document.removeEventListener(type = EVENT_TYPE_MOUSE_MOVE, callback = mouseObserver)
        }
    }

    Crossfade(
        targetState = isMouseInsideAnchorBounds || isContentHovered?.value == true,
        modifier = Modifier
            .position(Position.Fixed)
            .left(animatedAnchorBounds.right.px)
            .top(
                animatedAnchorBounds.top.coerceAtMost(
                    window.innerHeight
                        .minus(contentHeight?.toFloat() ?: 0f)
                        .minus(8f)
                ).px
            )
            .zIndex(1)
            .onMouseEnter { isContentHovered?.value = true }
            .onMouseLeave { isContentHovered?.value = false }
            .then(modifier),
        animateTranslationY = false,
    ) { shouldDisplayContent ->
        if (shouldDisplayContent) {
            Crossfade(
                targetState = targetState,
                animateTranslationY = false,
            ) { state ->
                Box(
                    ref = ref { element ->
                        contentHeight = element.getBoundingClientRect().height
                    }
                ) { content(state) }
            }
        }
    }
}

private const val EVENT_TYPE_MOUSE_MOVE = "mousemove"
