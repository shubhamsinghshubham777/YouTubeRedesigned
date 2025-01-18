package com.google.youtube.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.google.youtube.utils.Styles
import com.google.youtube.utils.onMouseEvent
import com.google.youtube.utils.removeMouseEventListeners
import com.google.youtube.utils.toComposeColor
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
    var backgroundColor by remember { mutableStateOf(Color.Transparent) }
    val animatedBackgroundColor by animateColorAsState(backgroundColor)
    Image(
        ref = disposableRef { element ->
            // TODO: Replace this code with `rememberMouseEventAsState`
            val mouseEventCallbacks = element.onMouseEvent(
                onHoveredAndPressed = { backgroundColor = Styles.PRESS_HIGHLIGHT.toComposeColor() },
                onHovered = { backgroundColor = Styles.HOVER_HIGHLIGHT.toComposeColor() },
                onReleased = { backgroundColor = Color.Transparent }
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
