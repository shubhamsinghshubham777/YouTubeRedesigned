package com.google.youtube.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.roundToIntRect
import androidx.compose.ui.unit.toRect
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnDefaults
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.onMouseDown
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.thenIf
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.EventsListenerScope
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.w3c.dom.events.Event
import org.w3c.dom.events.MouseEvent
import kotlin.math.roundToInt

@Composable
fun ReorderableList(
    modifier: Modifier = Modifier,
    verticalSpacePx: Int? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    CompositionLocalProvider(LocalReorderableListState provides remember { ReorderableListState() }) {
        Column(
            modifier = modifier.scrollBehavior(ScrollBehavior.Smooth),
            verticalArrangement = verticalSpacePx?.let { space ->
                Arrangement.spacedBy(space.px)
            } ?: ColumnDefaults.VerticalArrangement,
            content = content,
        )
    }
}

@Composable
fun ReorderableListItem(
    modifier: Modifier = Modifier,
    onPressScale: Float = 0.9f,
    handle: @Composable () -> Unit,
    content: @Composable (@Composable () -> Unit) -> Unit,
) {
    val state = LocalReorderableListState.current
    var index by remember { mutableStateOf<Int?>(null) }
    val isDragged by remember { derivedStateOf { index == state.currentDragIndex } }
    var permanentRect by remember { mutableStateOf<IntRect?>(null) }
    val stateRect by animateRectAsState(
        index?.let { safeIndex ->
            state.rectForIndexAsState(safeIndex).value.toRect()
        } ?: Rect.Zero
    )
    val animatedScale by animateFloatAsState(targetValue = if (isDragged) onPressScale else 1f, animationSpec = tween())
    val animatedTranslationY by animateIntAsState(if (isDragged) state.translationY else 0)

    Box(
        ref = ref { e ->
            val localRect = e.getBoundingClientRect().toComposeRect().roundToIntRect()
            index = state.addRectAndGetIndex(localRect)
            permanentRect = localRect
        },
        modifier = Modifier
            .scale(animatedScale)
            .position(Position.Relative)
            .then(
                permanentRect?.let { safePermanentRect ->
                    stateRect.let { safeStateRect ->
                        Modifier.translateY(
                            safeStateRect.top
                                .minus(safePermanentRect.top)
                                .plus(animatedTranslationY)
                                .px
                        )
                    }
                } ?: Modifier
            )
            .thenIf(isDragged) { Modifier.zIndex(-1) }
            .userSelect(UserSelect.None)
            .onMouseEnter { if (!isDragged) index?.let(state::swapWithIndex) }
            .then(modifier),
    ) {
        content {
            Box(modifier = Modifier.onMouseDown { index?.let(state::startDragging) }) { handle() }
        }
    }
}

@Stable
class ReorderableListState {
    var currentDragIndex by mutableStateOf<Int?>(null)
        private set

    var translationY by mutableIntStateOf(0)
        private set

    private val rects = mutableStateListOf<IntRect>()

    private var mouseOffsetY by mutableStateOf<Int?>(null)
    private val autoScrollDirection by derivedStateOf {
        mouseOffsetY?.let { offsetY ->
            when {
                offsetY < AUTO_SCROLL_THRESHOLD -> AutoScrollDirection.Up
                offsetY > window.innerHeight - AUTO_SCROLL_THRESHOLD -> AutoScrollDirection.Down
                else -> AutoScrollDirection.None
            }
        } ?: AutoScrollDirection.None
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private var dragJob: Job? = null
    private var scrollJob: Job? = null

    private val mouseUpCallback = { _: Event -> stopDragging() }

    private val mouseMoveCallback = { event: Event ->
        translationY += (event as MouseEvent).asDynamic().movementY as? Int ?: 0
        mouseOffsetY = event.y.roundToInt()
    }

    fun addRectAndGetIndex(size: IntRect): Int {
        rects.add(size)
        return rects.lastIndex
    }

    fun rectForIndexAsState(index: Int): State<IntRect> {
        return derivedStateOf { rects[index] }
    }

    fun startDragging(index: Int) {
        document.body?.style?.cursor = "grabbing"
        translationY = 0
        currentDragIndex = index
        document.addEventListener(EventsListenerScope.MOUSEUP, mouseUpCallback)
        document.addEventListener(EventsListenerScope.MOUSEMOVE, mouseMoveCallback)
        dragJob = coroutineScope.launch {
            snapshotFlow { autoScrollDirection }.collect { direction ->
                scrollJob?.cancel()
                scrollJob = when (direction) {
                    AutoScrollDirection.Up -> launch {
                        while (window.scrollY.toInt() > 0) {
                            window.scrollBy(0.0, -AUTO_SCROLL_WINDOW_STEP)
                            translationY -= AUTO_SCROLL_TRANSLATION_Y_STEP
                            delay(AUTO_SCROLL_DELAY)
                        }
                    }

                    AutoScrollDirection.Down -> launch {
                        val maxHeight = document.body?.offsetHeight?.minus(window.innerHeight) ?: 0
                        while (window.scrollY.toInt() < maxHeight) {
                            window.scrollBy(0.0, AUTO_SCROLL_WINDOW_STEP)
                            translationY += AUTO_SCROLL_TRANSLATION_Y_STEP
                            delay(AUTO_SCROLL_DELAY)
                        }
                    }

                    else -> null
                }
            }
        }
    }

    private fun stopDragging() {
        document.body?.style?.cursor = "default"
        currentDragIndex = null
        document.removeEventListener(EventsListenerScope.MOUSEUP, mouseUpCallback)
        document.removeEventListener(EventsListenerScope.MOUSEMOVE, mouseMoveCallback)
        dragJob?.cancel()
    }

    fun swapWithIndex(index: Int) {
        if (index == currentDragIndex) return
        currentDragIndex?.let { safeDragIndex ->
            translationY -= rects[index].top - rects[safeDragIndex].top
            val temp = rects[index]
            rects[index] = rects[safeDragIndex]
            rects[safeDragIndex] = temp
        }
    }
}

val LocalReorderableListState = compositionLocalOf<ReorderableListState> {
    error("No state provided!")
}

private enum class AutoScrollDirection { Up, None, Down }

private const val AUTO_SCROLL_TRANSLATION_Y_STEP = 26
private const val AUTO_SCROLL_DELAY = 100L
private const val AUTO_SCROLL_WINDOW_STEP = 30.0
private const val AUTO_SCROLL_THRESHOLD = 100
