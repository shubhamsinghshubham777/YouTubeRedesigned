package com.google.youtube.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.toArgb
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.onScroll
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import androidx.compose.ui.graphics.Color as ComposeColor

fun ComposeColor.toKobwebColor(): Color = Color.argb(this.toArgb())

fun Color.Rgb.toComposeColor(): ComposeColor {
    return ComposeColor(red = red, green = green, blue = blue, alpha = alpha)
}

enum class MouseEventState { Pressed, Hovered, Released }

data class MouseEventCallbacks(
    val onHover: (MouseEvent) -> Unit,
    val onExit: (MouseEvent) -> Unit,
    val onPress: (MouseEvent) -> Unit,
    val onRelease: (MouseEvent) -> Unit,
)

@Composable
fun rememberMouseEventAsState(element: Element?): State<MouseEventState> {
    return produceState(MouseEventState.Released, element) {
        val callback = element?.onMouseEvent(
            onHoveredAndPressed = { value = MouseEventState.Pressed },
            onHovered = { value = MouseEventState.Hovered },
            onReleased = { value = MouseEventState.Released }
        )
        awaitDispose {
            callback?.let { safeCallback -> element.removeMouseEventListeners(safeCallback) }
        }
    }
}

fun Element.onMouseEvent(
    onHoveredAndPressed: (MouseEvent) -> Unit,
    onHovered: (MouseEvent) -> Unit,
    onReleased: (MouseEvent) -> Unit,
): MouseEventCallbacks {
    var isHovered = false
    var isPressed = false

    val onHoverCallback = { event: Event ->
        isHovered = true
        if (!isPressed) onHovered(event as MouseEvent)
    }
    val onExitCallback = { event: Event ->
        isHovered = false
        if (!isPressed || !isHovered) {
            onReleased(event as MouseEvent)
            isPressed = false
        }
    }
    val onPressCallback = { event: Event ->
        isPressed = true
        if (isHovered) onHoveredAndPressed(event as MouseEvent)
    }
    val onReleaseCallback = { event: Event ->
        isPressed = false
        if (!isHovered) onReleased(event as MouseEvent)
        else onHovered(event as MouseEvent)
    }

    addEventListener("mouseover", onHoverCallback)
    addEventListener("mouseout", onExitCallback)
    addEventListener("pointerdown", onPressCallback)
    addEventListener("pointerup", onReleaseCallback)

    return MouseEventCallbacks(
        onHover = onHoverCallback,
        onExit = onExitCallback,
        onPress = onPressCallback,
        onRelease = onReleaseCallback
    )
}

@Suppress("UNCHECKED_CAST")
fun Element.removeMouseEventListeners(callbacks: MouseEventCallbacks) {
    removeEventListener(type = "mouseover", callback = callbacks.onHover as (Event) -> Unit)
    removeEventListener(type = "mouseout", callback = callbacks.onExit as (Event) -> Unit)
    removeEventListener(type = "pointerdown", callback = callbacks.onPress as (Event) -> Unit)
    removeEventListener(type = "pointerup", callback = callbacks.onRelease as (Event) -> Unit)
}

infix fun Breakpoint.isGreaterThan(other: Breakpoint): Boolean = ordinal > other.ordinal

fun Modifier.bindScrollState(state: MutableState<HorizontalScrollState>) = then(
    Modifier.onScroll { event ->
        val target = event.target as HTMLElement
        when {
            target.scrollLeft == 0.0 &&
                    state.value != HorizontalScrollState.ReachedStart ->
                state.value = HorizontalScrollState.ReachedStart

            target.scrollWidth - (target.scrollLeft + target.clientWidth) <= 0.5 ->
                state.value = HorizontalScrollState.ReachedEnd

            state.value != HorizontalScrollState.Scrolling ->
                state.value = HorizontalScrollState.Scrolling
        }
    }
)
