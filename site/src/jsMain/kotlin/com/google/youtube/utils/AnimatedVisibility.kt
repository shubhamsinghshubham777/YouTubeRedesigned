package com.google.youtube.utils

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Size
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.width
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px

// TODO: Extend this composable to accept custom animations (like fade*, scale*, slide*Horizontally, slide*Vertically [* means in/out])
@Composable
fun AnimatedVisibility(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val isVisibleState = updateTransition(isVisible)
    // This state will only be initialised once
    var finalContentSize by remember { mutableStateOf<Size?>(null) }

    // These states will animate over time
    var currentContentSize by remember { mutableStateOf<Size?>(null) }
    var currentOpacity by remember { mutableFloatStateOf(0f) }
    val isOpacityZero by remember { derivedStateOf { currentOpacity == 0f } }

    // These states will be responsible for animating the states above
    val sizeAnimatable = remember { Animatable(0f) }
    val opacityAnimatable = remember { Animatable(0f) }
    val animSpec = remember { tween<Float>(durationMillis = ANIM_DURATION_MILLIS.toInt()) }

    LaunchedEffect(isVisibleState.currentState) {
        finalContentSize?.let { size ->
            if (isVisibleState.currentState) {
                launch {
                    sizeAnimatable.animateTo(1f, animSpec) { currentContentSize = size * value }
                }
                launch {
                    delay(ANIM_DURATION_MILLIS.div(2))
                    opacityAnimatable.animateTo(1f, animSpec) { currentOpacity = value }
                }
            } else {
                launch { opacityAnimatable.animateTo(0f, animSpec) { currentOpacity = value } }
                launch {
                    delay(ANIM_DURATION_MILLIS.div(2))
                    sizeAnimatable.animateTo(0f, animSpec) { currentContentSize = size * value }
                }
            }
        }
    }

    Box(
        ref = ref { element ->
            finalContentSize = Size(element.offsetWidth.toFloat(), element.offsetHeight.toFloat())
            currentContentSize = Size.Zero
            currentOpacity = 0f
        },
        modifier = Modifier
            .then(
                currentContentSize?.let { size ->
                    Modifier.width(size.width.px).height(size.height.px)
                } ?: Modifier
            )
            .opacity(currentOpacity)
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        if (finalContentSize == null || isVisibleState.currentState || !isOpacityZero) content()
    }
}

private const val ANIM_DURATION_MILLIS = 300L
