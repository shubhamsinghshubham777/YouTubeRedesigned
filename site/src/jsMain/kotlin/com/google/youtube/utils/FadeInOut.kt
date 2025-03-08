package com.google.youtube.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.opacity

@Composable
fun FadeInOut(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val transition = updateTransition(isVisible)
    val animatedOpacity by transition.animateFloat({ tween() }) { visible -> if (visible) 1f else 0f }

    Box(modifier = Modifier.opacity(animatedOpacity).then(modifier)) {
        with(transition) {
            if (!currentState && !targetState && !isRunning) return@Box
            content()
        }
    }
}
