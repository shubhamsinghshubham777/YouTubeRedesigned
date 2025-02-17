package com.google.youtube.components.widgets

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.clickable
import com.google.youtube.utils.noShrink
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import org.jetbrains.compose.web.css.px

@Composable
fun CategoryTab(
    label: String,
    isSelected: Boolean,
    isDense: Boolean = true,
    secondaryLabel: String? = null,
    onClick: () -> Unit,
) {
    val updatedIsSelected = updateTransition(isSelected).currentState
    val animatedIndicatorHeight by animateFloatAsState(if (updatedIsSelected) 3f else 0f)
    val animatedOpacity by animateFloatAsState(if (updatedIsSelected) 1f else 0.5f)
    val textSize = remember(isDense) { if (isDense) 14 else 20 }

    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .height(48.px)
            .noShrink()
            .opacity(animatedOpacity)
            .padding(leftRight = 16.px)
            .userSelect(UserSelect.None),
        contentAlignment = Alignment.Center
    ) {
        SpacedRow(spacePx = 8) {
            TextBox(text = label, weight = FontWeight.Medium, size = textSize)
            secondaryLabel?.let { safeLabel ->
                TextBox(text = safeLabel, color = Styles.WHITE.copyf(alpha = 0.6f), size = textSize)
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .backgroundImage(Styles.Gradient.RED_TO_PINK)
                .borderRadius(topLeft = 3.px, topRight = 3.px)
                .size(width = if (isDense) 25.px else 65.px, height = animatedIndicatorHeight.px)
        )
    }
}
