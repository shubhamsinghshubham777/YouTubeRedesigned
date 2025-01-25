package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Assets
import com.google.youtube.utils.HorizontalScrollState
import com.google.youtube.utils.Styles
import com.varabyte.kobweb.compose.css.PointerEvents
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.BoxScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.pointerEvents
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import org.jetbrains.compose.web.css.CSSSizeValue
import org.jetbrains.compose.web.css.CSSUnit
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.plus
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element

@Composable
fun BoxScope.RowScrollButtons(
    elementToControl: Element?,
    horizontalScrollState: MutableState<HorizontalScrollState>,
    containerPadding: CSSSizeValue<CSSUnit.px>,
    scrollPixels: Double,
    centerVertically: Boolean = false,
    gradientColor: Color = Styles.MISSED_VIDEOS_CONTAINER,
) {
    repeat(2) { index ->
        val startItem = index == 0
        AnimatedVisibility(
            isVisible = if (startItem) horizontalScrollState.value != HorizontalScrollState.ReachedStart
            else horizontalScrollState.value != HorizontalScrollState.ReachedEnd,
            modifier = Modifier
                .position(Position.Absolute)
                .align(if (startItem) Alignment.CenterStart else Alignment.CenterEnd)
                .zIndex(1),
        ) {
            Box(
                modifier = Modifier
                    .backgroundImage(
                        linearGradient(
                            dir = if (startItem) LinearGradient.Direction.ToRight
                            else LinearGradient.Direction.ToLeft
                        ) {
                            add(gradientColor)
                            add(Colors.Transparent)
                        }
                    )
                    .width(56.px)
                    .height(elementToControl?.clientHeight?.px?.plus(containerPadding) ?: 0.px)
                    .pointerEvents(PointerEvents.None),
                contentAlignment = when {
                    startItem && centerVertically -> Alignment.CenterStart
                    startItem -> Alignment.TopStart
                    !startItem && centerVertically -> Alignment.CenterEnd
                    else -> Alignment.TopEnd
                }
            ) {
                AssetImageButton(
                    if (startItem) Assets.Icons.ARROW_LEFT else Assets.Icons.ARROW_RIGHT,
                    modifier = Modifier
                        .margin(leftRight = 22.px, top = if (centerVertically) 0.px else 96.px)
                        .background(Styles.ARROW_BUTTON_CONTAINER)
                        .pointerEvents(PointerEvents.Auto)
                ) {
                    elementToControl?.scrollBy(
                        x = scrollPixels.times(if (startItem) -1 else 1),
                        y = 0.0
                    )
                }
            }
        }
    }
}
