package com.google.youtube.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.Styles
import com.google.youtube.utils.clickable
import com.google.youtube.utils.rememberMouseEventAsState
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element

@Composable
fun Switch(
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    reactToMouseEvents: Boolean = true,
    onSelectionChange: (Boolean) -> Unit,
) {
    var elementRef: Element? by remember { mutableStateOf(null) }
    val mouseEventState by rememberMouseEventAsState(elementRef)

    val animatedIsSelected = updateTransition(isSelected).currentState
    val animatedThumbSize by animateFloatAsState(
        when (mouseEventState) {
            MouseEventState.Pressed -> if (animatedIsSelected) 20f else 14f
            MouseEventState.Hovered -> if (animatedIsSelected) 18f else 12f
            MouseEventState.Released -> if (animatedIsSelected) 16f else 10f
        }
    )
    val animatedContainerColor by animateColorAsState(
        if (animatedIsSelected) Styles.SWITCH_BACKGROUND_SELECTED.toComposeColor()
        else Color.Transparent
    )
    val animatedThumbColor by animateColorAsState(
        (if (animatedIsSelected) Styles.WHITE else Styles.SWITCH_OUTLINE).toComposeColor()
    )
    val animatedSpacerWeight by animateFloatAsState(if (animatedIsSelected) 1f else 0f)

    Box(
        ref = if (reactToMouseEvents) ref { ref -> elementRef = ref } else null,
        modifier = modifier
            .thenIf(reactToMouseEvents) { Modifier.clickable { onSelectionChange(!isSelected) } }
            .size(width = 40.px, height = 24.px),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(2.px, LineStyle.Solid, Styles.SWITCH_OUTLINE)
                .borderRadius(100.px)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(animatedContainerColor.toKobwebColor())
                .borderRadius(100.px)
        )
        Row(
            modifier = Modifier.fillMaxSize().padding(leftRight = 0.px),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(animatedSpacerWeight.coerceIn(0.275f, 0.85f)))
            Box(
                modifier = Modifier
                    .background(animatedThumbColor.toKobwebColor())
                    .clip(Circle())
                    .size(animatedThumbSize.px)
            )
        }
    }
}
