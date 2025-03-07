package com.google.youtube.utils

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.FontStyle
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontStyle
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.textAlign
import com.varabyte.kobweb.compose.ui.thenIfNotNull
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

// TODO: Use it throughout the codebase
@Composable
fun TextBox(
    text: String,
    ref: ElementRefScope<HTMLElement>? = null,
    size: Int = 16,
    lineHeight: Number? = null,
    weight: FontWeight = FontWeight.Normal,
    style: FontStyle = FontStyle.Normal,
    family: String? = null,
    color: Color = Styles.WHITE,
    maxLines: Int? = null,
    textAlign: TextAlign? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        ref = ref,
        modifier = Modifier
            .color(color)
            .fontSize(size.px)
            .fontStyle(style)
            .fontWeight(weight)
            .thenIfNotNull(family) { Modifier.fontFamily(it) }
            .thenIfNotNull(lineHeight) { Modifier.lineHeight(it.px) }
            .thenIfNotNull(maxLines) { Modifier.limitTextWithEllipsis(it, breakLetter = false) }
            .thenIfNotNull(textAlign) { Modifier.textAlign(it) }
            .then(modifier)
    ) { Text(text) }
}
