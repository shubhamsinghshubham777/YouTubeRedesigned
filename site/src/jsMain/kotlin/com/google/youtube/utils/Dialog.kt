package com.google.youtube.utils

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.lerp
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.BoxScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scale
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.thenIfNotNull
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent

@Composable
fun Dialog(
    isDisplayed: Boolean,
    modifier: Modifier = Modifier,
    onDismissed: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    var displayContent by remember { mutableStateOf(isDisplayed) }
    var animatedBackdropColor by remember {
        mutableStateOf(Color.Black.copy(alpha = if (isDisplayed) 0.5f else 0f))
    }
    var animatedContentOpacity by remember { mutableFloatStateOf(if (isDisplayed) 1f else 0f) }
    var animatedContentScale by remember { mutableStateOf(1f) }

    LaunchedEffect(isDisplayed) {
        if (isDisplayed && !displayContent) {
            displayContent = true

            Animatable(initialValue = Color.Black.copy(alpha = 0f)).animateTo(
                targetValue = Color.Black.copy(alpha = 0.5f),
                animationSpec = tween()
            ) { animatedBackdropColor = value }

            androidx.compose.animation.core.Animatable(initialValue = 0f).animateTo(
                targetValue = 1f,
                animationSpec = tween()
            ) {
                animatedContentOpacity = value
                animatedContentScale = lerp(start = 0.97f, stop = 1f, fraction = value)
            }
        } else if (displayContent) {
            androidx.compose.animation.core.Animatable(initialValue = 1f).animateTo(
                targetValue = 0f,
                animationSpec = tween()
            ) {
                animatedContentOpacity = value
                animatedContentScale = lerp(start = 1f, stop = 0.97f, fraction = 1 - value)
            }

            Animatable(initialValue = Color.Black.copy(alpha = 0.5f)).animateTo(
                targetValue = Color.Black.copy(alpha = 0f),
                animationSpec = tween()
            ) {
                animatedBackdropColor = value
            }

            displayContent = false
        }
    }

    DisposableEffect(Unit) {
        val keyDownListener = { event: Event ->
            if ((event as KeyboardEvent).key == "Escape") onDismissed?.invoke()
        }

        window.addEventListener("keydown", keyDownListener)
        onDispose { window.removeEventListener("keydown", keyDownListener) }
    }

    if (displayContent) {
        Box(
            modifier = Modifier
                .size(width = 100.vw, height = 100.vh)
                .background(animatedBackdropColor.toKobwebColor())
                .thenIfNotNull(onDismissed) { safeOnDismissed ->
                    Modifier.onClick { safeOnDismissed() }
                }
                .position(Position.Fixed)
                .top(0.px)
                .then(modifier),
            contentAlignment = Alignment.Center,
            content = {
                Box(
                    modifier = Modifier
                        .onClick { event -> event.stopPropagation() }
                        .opacity(animatedContentOpacity)
                        .scale(animatedContentScale),
                    contentAlignment = Alignment.Center,
                    content = content
                )
            },
        )
    }
}
