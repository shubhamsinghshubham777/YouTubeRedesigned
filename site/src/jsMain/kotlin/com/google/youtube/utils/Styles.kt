package com.google.youtube.utils

import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors

object Styles {
    val SURFACE: Color.Rgb = Color.rgb(17, 17, 17)
    val BACKGROUND_SELECTED: Color.Rgb = Color.rgba(255, 170, 187, 0.14f)
    val BORDER_COLOR: Color.Rgb = Color.rgb(42, 42, 42)
    val HOVER_HIGHLIGHT: Color.Rgb = Color.rgba(255, 255, 255, 0.15f)
    val PRESS_HIGHLIGHT: Color.Rgb = Color.rgba(255, 255, 255, 0.24f)
    val OFF_WHITE: Color.Rgb = Color.rgb(241, 241, 241)
    val PINK: Color.Rgb = Color.rgb(245, 0, 87)
    val RED: Color.Rgb = Color.rgb(255, 0, 51)
    val RED_BUTTON: Color.Rgb = Color.rgb(204, 40, 73)
    val RED_LIGHT: Color.Rgb = Color.rgb(255, 170, 187)
    val WHITE: Color.Rgb = Colors.White

    object Gradients {
        val RED_TO_PINK: Pair<Color.Rgb, Color.Rgb> = RED to PINK
        val RED_BACKGROUND = linearGradient(
            dir = LinearGradient.Direction.ToRight,
            from = Color.rgba(r = 255, g = 255, b = 255, a = 6),
            to = Color.rgba(r = 255, g = 0, b = 51, a = 6)
        )
    }

    object Opacity {
        const val TOP_BAR_CONTENT = .4f
    }
}
