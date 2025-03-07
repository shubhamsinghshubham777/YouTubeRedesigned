package com.google.youtube.utils

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowDefaults
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.gap
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.px

@Composable
fun Wrap(
    horizontalGapPx: Int = 0,
    verticalGapPx: Int = horizontalGapPx,
    centerVertically: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .flexWrap(FlexWrap.Wrap)
            .gap(rowGap = verticalGapPx.px, columnGap = horizontalGapPx.px)
            .then(modifier),
        verticalAlignment = if (centerVertically) Alignment.CenterVertically else RowDefaults.VerticalAlignment,
        content = content,
    )
}
