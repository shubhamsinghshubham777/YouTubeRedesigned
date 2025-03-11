package com.google.youtube.components.widgets.context

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.utils.Asset
import com.google.youtube.utils.PaddingValues
import com.google.youtube.utils.Styles
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.Background
import com.varabyte.kobweb.compose.css.BackgroundPosition
import com.varabyte.kobweb.compose.css.CSSPosition
import com.varabyte.kobweb.compose.css.background
import com.varabyte.kobweb.compose.css.backgroundImage
import com.varabyte.kobweb.compose.css.backgroundPosition
import com.varabyte.kobweb.compose.css.color
import com.varabyte.kobweb.compose.css.functions.url
import com.varabyte.kobweb.compose.ui.graphics.Color
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.StyleScope
import org.jetbrains.compose.web.css.backgroundImage
import org.jetbrains.compose.web.css.backgroundRepeat
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.flexShrink
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.outline
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingLeft
import org.jetbrains.compose.web.css.paddingRight
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.TextInput
import org.w3c.dom.HTMLInputElement

@Composable
fun TextField(
    textState: MutableState<String>,
    hintText: String,
    onElementAvailable: ((HTMLInputElement) -> Unit)? = null,
    containerColor: Color? = null,
    contentPadding: PaddingValues = PaddingValues(16.px),
    leadingAsset: String? = null,
    showBorder: Boolean = true,
    onEnterKeyPressed: (() -> Unit)? = null,
    style: StyleScope.() -> Unit = { width(100.percent) },
) {
    var isFocused by remember { mutableStateOf(false) }
    val animatedBorderColor by animateColorAsState(
        Styles.WHITE.copyf(alpha = if (isFocused) 1f else 0.15f).toComposeColor()
    )
    TextInput(value = textState.value) {
        onElementAvailable?.let { callback ->
            ref { element ->
                callback(element)
                onDispose {}
            }
        }
        onClick { it.stopPropagation() }
        onInput { event -> textState.value = event.value }
        if (showBorder) {
            onFocus { isFocused = true }
            onBlur { isFocused = false }
        }
        onKeyUp { event ->
            event.stopPropagation()
            if (event.key == "Enter") onEnterKeyPressed?.invoke()
        }
        style {
            containerColor?.let { bg -> background(bg) } ?: run { background(Background.None) }
            leadingAsset?.let { asset ->
                backgroundImage(url(asset))
                backgroundPosition(BackgroundPosition.of(CSSPosition(16.px, 7.px)))
                backgroundRepeat("no-repeat")
            }
            leadingAsset?.let(::backgroundImage)
            if (showBorder) border(
                1.px,
                LineStyle.Solid,
                animatedBorderColor.toKobwebColor()
            ) else border(0.px)
            borderRadius(8.px)
            color(Styles.WHITE.toString())
            flexShrink(0)
            fontSize(16.px)
            outline("none")
            paddingBottom(contentPadding.bottom)
            paddingLeft(contentPadding.left)
            paddingRight(contentPadding.right)
            paddingTop(contentPadding.top)
            style()
        }
        placeholder(hintText)
    }
}

@Composable
fun RoundedSearchTextField(
    textState: MutableState<String>,
    hintText: String,
    widthPx: Int = 314,
    style: (StyleScope.() -> Unit)? = null,
) {
    TextField(
        containerColor = Styles.SURFACE_ELEVATED,
        contentPadding = PaddingValues(left = 48.px, top = 10.px, right = 16.px, bottom = 10.px),
        hintText = hintText,
        leadingAsset = Asset.Icon.SEARCH_DIM,
        textState = textState,
        showBorder = false,
    ) {
        borderRadius(24.px)
        width(widthPx.px)
        style?.invoke(this)
    }
}
