package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.clickable
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.thenIf
import org.w3c.dom.HTMLElement

@Composable
fun UnderlinedToggleText(
    isSelected: Boolean,
    mouseEventState: MouseEventState,
    activeText: String = "Show Less",
    inactiveText: String = "Show More",
    ref: ElementRefScope<HTMLElement>? = null,
    onClick: () -> Unit,
) {
    TextBox(
        ref = ref,
        modifier = Modifier
            .clickable(onClick = onClick)
            .thenIf(mouseEventState == MouseEventState.Hovered) {
                Modifier.textDecorationLine(TextDecorationLine.Underline)
            },
        color = Styles.WHITE.copyf(alpha = 0.47f),
        text = if (isSelected) activeText else inactiveText,
    )
}
