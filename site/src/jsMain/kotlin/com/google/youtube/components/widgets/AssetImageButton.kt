package com.google.youtube.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.utils.ComposeColor
import com.google.youtube.utils.onMouseEvent
import com.google.youtube.utils.removeMouseEventListeners
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.dom.disposableRef
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.role
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.px

@Composable
fun AssetImageButton(
    asset: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    var backgroundColor by remember { mutableStateOf(ComposeColor.Transparent) }
    val animatedBackgroundColor by animateColorAsState(backgroundColor)
    Image(
        ref = disposableRef { element ->
            val mouseEventCallbacks = element.onMouseEvent(
                onHoveredAndPressed = { backgroundColor = ComposeColor.White.copy(alpha = .2392f) },
                onHovered = { backgroundColor = ComposeColor.White.copy(alpha = .1529f) },
                onReleased = { backgroundColor = ComposeColor.Transparent }
            )
            onDispose { element.removeMouseEventListeners(mouseEventCallbacks) }
        },
        modifier = Modifier
            .background(animatedBackgroundColor.toKobwebColor())
            .clip(Circle())
            .padding(8.px)
            .cursor(Cursor.Pointer)
            .role("button")
            .then(if (onClick != null) Modifier.onClick { onClick() } else Modifier)
            .then(modifier),
        src = asset,
    )
}
