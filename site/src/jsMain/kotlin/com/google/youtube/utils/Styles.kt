package com.google.youtube.utils

import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.graphics.Colors
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.rgba
import androidx.compose.ui.graphics.Color as ComposeColor

object Styles {
    val BACKGROUND = Color("#111111")
    val BACKGROUND_SELECTED = rgba(r = 255, g = 170, b = 187, a = 14)
    val RED: CSSColorValue = Color("#FF0033")
    val RED_BUTTON: CSSColorValue = Color("#CC2849")
    val RED_LIGHT: CSSColorValue = Color("#FFAABB")
    val WHITE: CSSColorValue = Colors.White
    val BORDER_COLOR: ComposeColor = ComposeColor(0XFF2A2A2A)
    val GRADIENT = linearGradient(
        dir = LinearGradient.Direction.ToRight,
        from = RED,
        to = Color("#F50057")
    )
    val RED_BACKGROUND = linearGradient(
        dir = LinearGradient.Direction.ToRight,
        from = rgba(r = 255, g = 255, b = 255, a = 6),
        to = rgba(r = 255, g = 0, b = 51, a = 6)
    )
    val HOVER_HIGHLIGHT = Color("#272727")

    object Opacity {
        const val TOP_BAR_CONTENT = .4f
    }
}
