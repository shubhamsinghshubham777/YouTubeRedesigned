package com.google.youtube.utils

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowDefaults
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLElement

// TODO: Use it throughout the codebase
@Composable
fun SpacedRow(
    spacePx: Number,
    ref: ElementRefScope<HTMLElement>? = null,
    modifier: Modifier = Modifier,
    centerContentVertically: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        ref = ref,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacePx.px),
        verticalAlignment = if (centerContentVertically) Alignment.CenterVertically
        else RowDefaults.VerticalAlignment,
        content = content,
    )
}
