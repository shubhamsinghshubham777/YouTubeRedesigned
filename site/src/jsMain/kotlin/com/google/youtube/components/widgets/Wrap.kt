package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.gap
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.px

@Composable
fun Wrap(
    columnGapPx: Int = 0,
    rowGapPx: Int = 0,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .flexWrap(FlexWrap.Wrap)
            .gap(rowGap = rowGapPx.px, columnGap = columnGapPx.px)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}
