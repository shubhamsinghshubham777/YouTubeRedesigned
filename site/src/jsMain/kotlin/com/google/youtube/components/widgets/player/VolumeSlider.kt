package com.google.youtube.components.widgets.player

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.web.events.SyntheticMouseEvent
import com.google.youtube.utils.Styles
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.PointerEvents
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.onMouseDown
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.onMouseMove
import com.varabyte.kobweb.compose.ui.modifiers.onMouseUp
import com.varabyte.kobweb.compose.ui.modifiers.pointerEvents
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.px

@Composable
fun VolumeSlider(
    progress: Float,
    onProgressChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    trackColor: Color = Styles.WHITE.copyf(alpha = 0.25f),
    progressColor: Color = Styles.WHITE.copyf(alpha = 0.75f),
    thumbColor: Color = Styles.WHITE,
) {
    val updatedProgress = updateTransition(progress).currentState
    val animatedProgress by animateFloatAsState(updatedProgress)
    var width by remember { mutableStateOf<Double?>(null) }
    var isDragging by remember { mutableStateOf(false) }
    val progressFromEvent: (SyntheticMouseEvent) -> Unit = remember {
        { e ->
            width?.let { safeWidth ->
                onProgressChanged((e.offsetX / safeWidth).toFloat())
            }
        }
    }

    Box(
        ref = ref { e -> width = e.getBoundingClientRect().width },
        modifier = Modifier
            .width(80.px)
            .height(48.px)
            .cursor(Cursor.Pointer)
            .onMouseDown { e ->
                isDragging = true
                progressFromEvent(e)
            }
            .thenIf(isDragging) { Modifier.onMouseMove(progressFromEvent) }
            .onMouseUp { isDragging = false }
            .onMouseLeave { isDragging = false }
            .then(modifier),
        contentAlignment = Alignment.CenterStart,
    ) {
        // Track
        Box(
            Modifier
                .background(trackColor)
                .fillMaxWidth()
                .height(4.px)
                .pointerEvents(PointerEvents.None)
        )

        // Progress
        width?.let { safeWidth ->
            Box(
                Modifier
                    .background(progressColor)
                    .height(4.px)
                    .pointerEvents(PointerEvents.None)
                    .width((animatedProgress * safeWidth).px)
            )

            // Thumb
            Box(
                Modifier
                    .background(thumbColor)
                    .clip(Circle())
                    .pointerEvents(PointerEvents.None)
                    .size(12.px)
                    .translateX((animatedProgress * safeWidth).px - 8.px)
            )
        }
    }
}
