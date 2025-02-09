package com.google.youtube.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toArgb
import com.varabyte.kobweb.browser.dom.observers.ResizeObserver
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.WordBreak
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.flexShrink
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onKeyUp
import com.varabyte.kobweb.compose.ui.modifiers.onScroll
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.textOverflow
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.wordBreak
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.thenIfNotNull
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.breakpointFloor
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.px
import org.w3c.dom.DOMRect
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener
import org.w3c.dom.events.MouseEvent
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
fun rememberBreakpointAsState() = produceState(window.breakpointFloor) {
    val resizeListener = EventListener { value = window.breakpointFloor }

    window.addEventListener("resize", resizeListener)
    awaitDispose {
        window.removeEventListener("resize", resizeListener)
    }
}

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

// TODO: Use it throughout the codebase
@Composable
fun State<MouseEventState>.animatedColor(initialColor: Color = Colors.Transparent) =
    animateColorAsState(
        when (value) {
            MouseEventState.Pressed -> Styles.PRESS_HIGHLIGHT
            MouseEventState.Hovered -> Styles.HOVER_HIGHLIGHT
            MouseEventState.Released -> initialColor.toRgb()
        }.toComposeColor()
    )

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
infix fun Breakpoint.isSmallerThan(other: Breakpoint): Boolean = ordinal < other.ordinal

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

fun Modifier.hideScrollBar() = then(
    Modifier.styleModifier { property("scrollbar-width", "none") }
)

fun Modifier.limitTextWithEllipsis(maxLines: Int = 1, breakLetter: Boolean = true) = then(
    Modifier
        .attrsModifier {
            style {
                property("display", "-webkit-box")
                property("-webkit-line-clamp", maxLines)
                property("-webkit-box-orient", "vertical")
            }
        }
        .overflow(Overflow.Hidden)
        .textOverflow(TextOverflow.Ellipsis)
        // TODO: Check if this can be removed for a better experience
        .thenIf(breakLetter) { Modifier.wordBreak(WordBreak.BreakAll) }
)

fun DOMRect.toComposeRect() = Rect(
    left = left.toFloat(),
    top = top.toFloat(),
    right = right.toFloat(),
    bottom = bottom.toFloat()
)

fun Modifier.gridGap(
    x: CSSLengthOrPercentageNumericValue = 0.px,
    y: CSSLengthOrPercentageNumericValue = x,
) = styleModifier { property("grid-gap", "$y $x") }

fun Modifier.noShrink() = then(Modifier.flexShrink(0))

fun Modifier.clickable(
    noPointer: Boolean = false,
    onClick: (() -> Unit)? = null,
) = thenIf(onClick != null) {
    Modifier
        .onKeyUp { event -> if ((event.key == "Enter" || event.key == " ") && onClick != null) onClick.invoke() }
        .tabIndex(0)
        .thenIf(!noPointer) { Modifier.cursor(Cursor.Pointer) }
        .thenIfNotNull(onClick) { safeOnClick ->
            Modifier.onClick { event ->
                event.stopPropagation()
                safeOnClick()
            }
        }
        .userSelect(UserSelect.None)
}

@Composable
fun rememberWindowWidthAsState(): State<Int> = produceState(window.innerWidth) {
    val observer = ResizeObserver { entries ->
        entries.firstOrNull()?.let { entry ->
            value = entry.contentRect.width.toInt()
        }
    }

    document.documentElement?.let(observer::observe)

    awaitDispose {
        observer.disconnect()
        document.documentElement?.let(observer::unobserve)
    }
}

@Composable
fun rememberIsShortWindowAsState(threshold: Int = 650): State<Boolean> {
    return produceState(window.innerHeight < threshold) {
        val observer = ResizeObserver { entries ->
            entries.firstOrNull()?.let { entry ->
                value = entry.contentRect.height < threshold
            }
        }

        document.documentElement?.let(observer::observe)

        awaitDispose {
            observer.disconnect()
            document.documentElement?.let(observer::unobserve)
        }
    }
}

// TODO: Make it non-null & initialise with 0
@Composable
fun rememberElementWidthAsState(element: Element?) = produceState<Double?>(
    initialValue = null,
    key1 = element
) {
    value = element?.getBoundingClientRect()?.width

    val observer = ResizeObserver { entries ->
        entries.firstOrNull()?.let { entry -> value = entry.contentRect.width }
    }

    element?.let(observer::observe)

    awaitDispose {
        element?.let { e ->
            with(observer) {
                disconnect()
                unobserve(e)
            }
        }
    }
}

@Composable
fun rememberElementHeightAsState(element: Element?) = produceState(
    initialValue = 0.0,
    key1 = element
) {
    value = element?.getBoundingClientRect()?.height ?: 0.0

    val observer = ResizeObserver { entries ->
        entries.firstOrNull()?.let { entry -> value = entry.contentRect.height }
    }

    element?.let(observer::observe)

    awaitDispose {
        element?.let { e ->
            with(observer) {
                disconnect()
                unobserve(e)
            }
        }
    }
}
