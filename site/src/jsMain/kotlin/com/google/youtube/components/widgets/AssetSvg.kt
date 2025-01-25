package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.youtube.utils.Styles
import com.varabyte.kobweb.compose.dom.svg.Defs
import com.varabyte.kobweb.compose.dom.svg.LinearGradient
import com.varabyte.kobweb.compose.dom.svg.Path
import com.varabyte.kobweb.compose.dom.svg.SVGFillRule
import com.varabyte.kobweb.compose.dom.svg.Stop
import com.varabyte.kobweb.compose.dom.svg.Svg
import com.varabyte.kobweb.compose.dom.svg.SvgId
import org.jetbrains.compose.web.css.CSSColorValue
import org.jetbrains.compose.web.css.CSSLengthOrPercentageValue
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun AssetSvg(
    id: String,
    path: String,
    size: CSSLengthOrPercentageValue = 24.px,
    primaryColor: CSSColorValue = Styles.WHITE,
    secondaryColor: CSSColorValue = primaryColor,
    onClick: (() -> Unit)? = null,
    style: (StyleScope.() -> Unit)? = null,
) {
    Svg(
        attrs = {
            height(size)
            width(size)
            onClick?.let { safeOnClick ->
                onClick { event ->
                    event.stopPropagation()
                    safeOnClick()
                }
            }
            style { style?.invoke(this) }
            // TODO: This is dangerous as we need to ensure all icons are of size 24 x 24 (hoist it as param instead)
            viewBox(0, 0, 24, 24)
        }
    ) {
        val gradientId = remember(id) {
            SvgId("fill_gradient_${id.replace(' ', '_')}")
        }
        Defs {
            LinearGradient(gradientId) {
                Stop {
                    offset(0.percent)
                    style {
                        property("stop-color", primaryColor)
                        property("stop-opacity", 1)
                    }
                }
                Stop {
                    offset(100.percent)
                    style {
                        property("stop-color", secondaryColor)
                        property("stop-opacity", 1)
                    }
                }
            }
        }
        Path {
            d(path)
            fill(gradientId)
            fillRule(SVGFillRule.EvenOdd)
        }
    }
}
