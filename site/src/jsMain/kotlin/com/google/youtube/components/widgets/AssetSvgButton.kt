package com.google.youtube.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.PaddingValues
import com.google.youtube.utils.Styles
import com.google.youtube.utils.onMouseEvent
import com.google.youtube.utils.removeMouseEventListeners
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.dom.disposableRef
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Composable
fun AssetSvgButton(
    onClick: () -> Unit,
    id: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    path: String? = null,
    text: String,
) {
    val isSelectedState = updateTransition(isSelected)
    var mouseEventState by remember { mutableStateOf(MouseEventState.Released) }
    val contentPadding = remember(path) {
        if (path != null) ButtonDefaults.ButtonWithIconContentPadding
        else ButtonDefaults.ContentPadding
    }
    val animatedContainerColor by animateColorAsState(
        when {
            isSelectedState.currentState && mouseEventState == MouseEventState.Pressed ->
                Colors.White.copyf(alpha = Styles.Opacity.PRESSED_SELECTED)

            isSelectedState.currentState && mouseEventState == MouseEventState.Hovered ->
                Colors.White.copyf(alpha = Styles.Opacity.HOVERED_SELECTED)

            isSelectedState.currentState && mouseEventState == MouseEventState.Released ->
                Colors.White

            !isSelectedState.currentState && mouseEventState == MouseEventState.Pressed ->
                Colors.White.copyf(alpha = Styles.Opacity.PRESSED)

            !isSelectedState.currentState && mouseEventState == MouseEventState.Hovered ->
                Colors.White.copyf(alpha = Styles.Opacity.HOVERED)

            else -> Colors.White.copyf(alpha = 0.05f)
        }.toComposeColor()
    )
    val animatedContentColor by animateColorAsState(
        when {
            isSelectedState.currentState -> Colors.Black
            else -> Colors.White
        }.toComposeColor()
    )

    Row(
        ref = disposableRef { element ->
            val mouseEventCallbacks = element.onMouseEvent(
                onHoveredAndPressed = { mouseEventState = MouseEventState.Pressed },
                onHovered = { mouseEventState = MouseEventState.Hovered },
                onReleased = { mouseEventState = MouseEventState.Released }
            )
            onDispose { element.removeMouseEventListeners(mouseEventCallbacks) }
        },
        modifier = Modifier
            .background(animatedContainerColor.toKobwebColor())
            .clip(Rect(24.px))
            .cursor(Cursor.Pointer)
            .padding(
                left = contentPadding.left,
                top = contentPadding.top,
                right = contentPadding.right,
                bottom = contentPadding.bottom,
            )
            .onClick { onClick() }
            .color(animatedContentColor.toKobwebColor())
            .fontSize(17.px)
            .fontWeight(FontWeight.Medium)
            .userSelect(UserSelect.None)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(8.px),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        path?.let { safeIconRef ->
            AssetSvg(
                id = "asset_svg_button_$id",
                path = safeIconRef,
                size = 22.px,
                primaryColor = animatedContentColor.toKobwebColor(),
            )
        }
        Box(modifier = Modifier.translateY(1.px)) { Text(text) }
    }
}

object ButtonDefaults {
    val ContentPadding: PaddingValues = PaddingValues(leftRight = 24.px, topBottom = 10.px)
    val ButtonWithIconContentPadding: PaddingValues = PaddingValues(
        left = 16.px,
        top = 10.px,
        right = 24.px,
        bottom = 10.px,
    )
}
