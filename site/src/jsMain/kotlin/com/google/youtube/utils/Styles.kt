package com.google.youtube.utils

import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.silk.theme.shapes.Rect
import org.jetbrains.compose.web.css.px

object Styles {
    // TODO: Create its own object named Color
    val SURFACE: Color.Rgb = Color.rgb(17, 17, 17)
    val SURFACE_ELEVATED: Color.Rgb = Color.rgb(31, 31, 31)
    val BACKGROUND_SELECTED: Color.Rgb = Color.rgba(255, 170, 187, 0.14f)
    val BORDER_COLOR: Color.Rgb = Color.rgb(42, 42, 42)
    val HOVER_HIGHLIGHT: Color.Rgb = Color.rgba(255, 255, 255, 0.15f)
    val PRESS_HIGHLIGHT: Color.Rgb = Color.rgba(255, 255, 255, 0.24f)
    val OFF_WHITE: Color.Rgb = Color.rgb(241, 241, 241)
    val PINK: Color.Rgb = Color.rgb(245, 0, 87)
    val RED: Color.Rgb = Color.rgb(255, 0, 51)
    val RED_BUTTON_BACKGROUND: Color.Rgb = Color.rgb(255, 50, 91)
    val SWITCH_BACKGROUND_SELECTED: Color.Rgb = Color.rgb(204, 39, 73)
    val RED_LIGHT: Color.Rgb = Color.rgb(255, 170, 187)
    val WHITE: Color.Rgb = Colors.White
    val BLACK: Color.Rgb = Colors.Black
    val SUBSCRIPTIONS_COUNT_BADGE_CONTAINER: Color.Rgb = Color.rgb(48, 48, 48)
    val SWITCH_OUTLINE: Color.Rgb = Color.rgb(121, 116, 126)
    val MISSED_VIDEOS_CONTAINER: Color.Rgb = Color.rgb(32, 34, 36)
    val ARROW_BUTTON_CONTAINER: Color.Rgb = Color.rgb(63, 63, 63)
    val VIDEO_CARD_PRIMARY_TEXT: Color.Rgb = Color.rgb(241, 241, 241)
    val VIDEO_CARD_SECONDARY_TEXT: Color.Rgb = Color.rgb(170, 170, 170)
    val VIDEO_CARD_DURATION_CONTAINER: Color.Rgb = Color.rgba(0, 0, 0, 0.6f)
    val DIVIDER: Color.Rgb = WHITE.copyf(alpha = 0.14f)
    val DIVIDER_LIGHTER: Color.Rgb = WHITE.copyf(alpha = 0.11f)
    val PURPLE_BORDER: Color.Rgb = Color.rgba(176, 130, 255, 0.37f)
    val BLUE_BORDER: Color.Rgb = Color.rgba(130, 191, 255, 0.37f)

    object Gradient {
        val RED_TO_PINK = linearGradient(LinearGradient.Direction.ToRight) {
            add(RED)
            add(PINK)
        }
    }

    object Opacity {
        const val ASSET_SVG_BUTTON = 0.1f
        const val HOVERED = 0.15f
        const val HOVERED_SELECTED = 1f - HOVERED
        const val PRESSED = 0.2f
        const val PRESSED_SELECTED = 1f - PRESSED
        const val TOP_BAR_CONTENT = 0.4f
    }

    // TODO: Rename to Font
    object Fonts {
        const val ROBOTO_CONDENSED = "Roboto Condensed"
    }

    object FontSize {
        val SMALL = 12.px
    }

    object Shape {
        val CARD = Rect(15.9.px)
    }
}
