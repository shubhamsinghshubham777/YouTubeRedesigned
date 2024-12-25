package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import com.google.youtube.utils.Styles
import com.varabyte.kobweb.compose.dom.svg.Path
import com.varabyte.kobweb.compose.dom.svg.Svg
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSLengthOrPercentageValue
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.px

@Composable
fun AssetSvg(
    path: String,
    size: CSSLengthOrPercentageValue = 24.px,
    color: CSSColorValue = Styles.WHITE,
    onClick: (() -> Unit)? = null,
    style: (StyleScope.() -> Unit)? = null,
) {
    Svg(
        attrs = {
            fill(color)
            height(size)
            style { style?.invoke(this) }
            width(size)
            onClick { onClick?.invoke() }
        }
    ) {
        Path { d(path) }
    }
}
