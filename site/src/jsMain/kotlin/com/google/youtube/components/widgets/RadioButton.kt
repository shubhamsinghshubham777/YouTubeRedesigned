package com.google.youtube.components.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.google.youtube.utils.Styles
import com.google.youtube.utils.clickable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

@Composable
fun RadioButton(
    onClick: () -> Unit,
    isSelected: Boolean,
    color: Color = Styles.WHITE.copyf(alpha = 0.2f),
    modifier: Modifier = Modifier,
) {
    val animatedIsSelected = updateTransition(isSelected).currentState
    val animatedSelectedDotSize by animateFloatAsState(if (animatedIsSelected) 16f else 0f)

    Box(
        modifier = Modifier
            .border(2.px, LineStyle.Solid, color)
            .borderRadius(24.px)
            .clickable(onClick = onClick)
            .clip(Circle())
            .size(24.px)
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .backgroundColor(color)
                .borderRadius(16.px)
                .clip(Circle())
                .size(animatedSelectedDotSize.px)
        )
    }
}
