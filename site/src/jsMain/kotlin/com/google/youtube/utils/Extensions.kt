package com.google.youtube.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toArgb
import com.varabyte.kobweb.browser.dom.observers.ResizeObserver
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.WordBreak
import com.varabyte.kobweb.compose.css.overflow
import com.varabyte.kobweb.compose.css.textOverflow
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.flexShrink
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onKeyUp
import com.varabyte.kobweb.compose.ui.modifiers.onScroll
import com.varabyte.kobweb.compose.ui.modifiers.tabIndex
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.wordBreak
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.thenIfNotNull
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.breakpointFloor
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.StyleScope
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

// TODO: Use this throughout the codebase
@Composable
fun rememberIsLargeBreakpoint(): State<Boolean> {
    val breakpoint by rememberBreakpointAsState()
    return remember { derivedStateOf { breakpoint.isGreaterThan(Breakpoint.LG) } }
}

@Composable
fun rememberIsSmallBreakpoint(): State<Boolean> {
    val breakpoint by rememberBreakpointAsState()
    return remember { derivedStateOf { breakpoint.isSmallerThan(Breakpoint.MD) } }
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

fun StyleScope.limitTextWithEllipsis(maxLines: Int = 1) {
    property("display", "-webkit-box")
    property("-webkit-line-clamp", maxLines)
    property("-webkit-box-orient", "vertical")
    overflow(Overflow.Hidden)
    textOverflow(TextOverflow.Ellipsis)
}

fun Modifier.limitTextWithEllipsis(maxLines: Int = 1, breakLetter: Boolean = true) = then(
    Modifier
        .attrsModifier { style { limitTextWithEllipsis(maxLines) } }
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

// TODO: Include hover & press highlight logic in it
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

fun addDurations(duration1: String, duration2: String): String {
    fun parseDuration(duration: String): Triple<Int, Int, Int> {
        // Reverse to handle varying formats
        val parts = duration.split(":").map { it.toInt() }.reversed()

        val seconds = parts.getOrElse(0) { 0 }
        val minutes = parts.getOrElse(1) { 0 }
        val hours = parts.getOrElse(2) { 0 }

        return Triple(hours, minutes, seconds)
    }

    fun pad(value: Int): String {
        return if (value < 10) "0$value" else value.toString()
    }

    val (h1, m1, s1) = parseDuration(duration1)
    val (h2, m2, s2) = parseDuration(duration2)

    var totalSeconds = s1 + s2
    var totalMinutes = m1 + m2 + totalSeconds / 60
    val totalHours = h1 + h2 + totalMinutes / 60

    totalSeconds %= 60
    totalMinutes %= 60

    return "${totalHours}:${pad(totalMinutes)}:${pad(totalSeconds)}"
}

/**
 * Adds two strings representing numbers with optional ordinal suffixes (K, M, etc.).
 *
 * Supported ordinals:
 * - K (Thousand) - Multiplier 10^3
 * - M (Million) - Multiplier 10^6
 * - G (Gillion/Billion - depending on context, here assumed Billion) - Multiplier 10^9
 *
 * Case-insensitive ordinals are accepted (k, m, g, etc.).
 *
 * If no ordinal is present, it's treated as a regular number.
 *
 * @param str1 The first string number with optional ordinal.
 * @param str2 The second string number with optional ordinal.
 * @return A string representing the sum, formatted with the largest possible ordinal,
 *         or just the number if no ordinal is applicable.
 *         Returns "0" if both inputs are effectively zero or invalid.
 *         Returns "NaN" if parsing fails and results in NaN.
 */
fun addOrdinals(str1: String, str2: String): String {
    val ordinalMultipliers = mapOf(
        "K" to 1000.0,
        "M" to 1000000.0,
        "G" to 1000000000.0
    )

    fun parseOrdinalString(ordinalStr: String): Double {
        val upperCaseStr = ordinalStr.uppercase()
        var numberString = upperCaseStr
        var multiplier = 1.0

        for ((suffix, value) in ordinalMultipliers) {
            if (upperCaseStr.endsWith(suffix)) {
                numberString = upperCaseStr.substring(0, upperCaseStr.length - suffix.length)
                multiplier = value
                break
            }
        }

        return numberString.toDoubleOrNull()?.let { it * multiplier } ?: 0.0
    }

    fun formatOutput(value: Double): String {
        if (value == 0.0) return "0"
        if (value.isNaN()) return "NaN"

        val reverseOrdinalMultipliers = ordinalMultipliers.entries.sortedByDescending { it.value }

        for ((suffix, limit) in reverseOrdinalMultipliers) {
            if (value >= limit) {
                // Use JavaScript's toFixed via dynamic interop
                var formattedValue = (value / limit).asDynamic().toFixed(1) as String
                formattedValue = formattedValue.trimEnd('0').trimEnd('.')
                return "${formattedValue}${suffix}"
            }
        }
        return value.asDynamic().toFixed(0) as String // Format as integer using toFixed(0)
    }

    val value1 = parseOrdinalString(str1)
    val value2 = parseOrdinalString(str2)

    val sum = value1 + value2

    return formatOutput(sum)
}

// Extension function for String to trim trailing characters (similar to Java's trimEnd)
private fun String.trimEnd(char: Char): String {
    var endIndex = this.length - 1
    while (endIndex >= 0 && this[endIndex] == char) {
        endIndex--
    }
    return substring(0, endIndex + 1)
}
