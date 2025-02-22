package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import com.google.youtube.utils.Constants
import com.google.youtube.utils.Styles
import com.google.youtube.utils.noShrink
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.width
import org.jetbrains.compose.web.css.px

// TODO: Use this divider throughout the codebase
@Composable
fun VerticalDivider() {
    Box(
        Modifier
            .background(Styles.DIVIDER_LIGHTER)
            .height(Constants.VERTICAL_DIVIDER_SIZE.height.px)
            .width(Constants.VERTICAL_DIVIDER_SIZE.width.px)
            .noShrink()
    )
}
