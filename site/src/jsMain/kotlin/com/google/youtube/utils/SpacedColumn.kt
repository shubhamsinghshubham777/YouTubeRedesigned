package com.google.youtube.utils

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.ColumnDefaults
import com.varabyte.kobweb.compose.foundation.layout.ColumnScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLElement

// TODO: Use it throughout the codebase
@Composable
fun SpacedColumn(
    spacePx: Number,
    ref: ElementRefScope<HTMLElement>? = null,
    centerContentHorizontally: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        ref = ref,
        modifier = modifier,
        horizontalAlignment = if (centerContentHorizontally) Alignment.CenterHorizontally
        else ColumnDefaults.HorizontalAlignment,
        verticalArrangement = Arrangement.spacedBy(spacePx.px),
        content = content,
    )
}
