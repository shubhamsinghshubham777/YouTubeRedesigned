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
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.css.px

@Composable
fun <T> Crossfade(
    targetState: T,
    modifier: Modifier = Modifier,
    animateTranslationY: Boolean = true,
    onStateChange: (() -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    var currentState by remember { mutableStateOf(targetState) }
    val animatable = remember { Animatable(initialValue = 1f) }

    var currentOpacity by remember { mutableFloatStateOf(1f) }
    var currentTranslationY by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(targetState) {
        if (targetState != currentState) {
            animatable.animateTo(targetValue = 0f, animationSpec = tween(150)) {
                currentOpacity = value
                if (animateTranslationY) currentTranslationY = (1 - value) * translationYDelta
            }
            onStateChange?.invoke()
            currentState = targetState
            delay(150)
            animatable.animateTo(targetValue = 1f, animationSpec = tween()) {
                currentOpacity = value
                if (animateTranslationY) currentTranslationY = (1 - value) * translationYDelta
            }
        } else if (
            !currentOpacity.hasAnimationEnded ||
            (!currentTranslationY.hasAnimationEnded && animateTranslationY)
        ) {
            animatable.animateTo(targetValue = 1f, animationSpec = tween()) {
                currentOpacity = value
                if (animateTranslationY) currentTranslationY = (1 - value) * translationYDelta
            }
        }
    }

    Box(
        modifier = modifier
            .opacity(currentOpacity)
            .translateY(currentTranslationY.px)
    ) { content(currentState) }
}

private val Float.hasAnimationEnded: Boolean get() = this == 0f || this == 1f
private const val translationYDelta = 4f
