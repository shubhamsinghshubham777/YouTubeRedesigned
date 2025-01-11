package com.google.youtube.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.translateY
import org.jetbrains.compose.web.css.px

@Composable
fun <T> FadeInOut(
    state: T,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit,
) {
    var currentState by remember { mutableStateOf(state) }
    val animatable = remember { Animatable(initialValue = 1f) }

    var currentOpacity by remember { mutableFloatStateOf(1f) }
    var currentTranslationY by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(state) {
        if (state != currentState) {
            animatable.animateTo(targetValue = 0f, animationSpec = tween(150)) {
                currentOpacity = value
                currentTranslationY = (1 - value) * 4f
            }
            currentState = state
            animatable.animateTo(targetValue = 1f, animationSpec = tween()) {
                currentOpacity = value
                currentTranslationY = (1 - value) * 4f
            }
        }
    }

    Box(
        modifier = modifier
            .opacity(currentOpacity)
            .translateY(currentTranslationY.px)
    ) { content(currentState) }
}
