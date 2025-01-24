package com.google.youtube.utils

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.GridEntry
import com.varabyte.kobweb.compose.css.GridTrackBuilderInRepeat
import com.varabyte.kobweb.compose.css.JustifyItems
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.alignItems
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateColumns
import com.varabyte.kobweb.compose.ui.modifiers.justifyItems
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.AlignItems
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div

@Composable
fun BasicGrid(
    modifier: Modifier = Modifier,
    gridGap: GridGap = GridGap.Zero,
    justifyItems: JustifyItems = JustifyItems.Stretch,
    alignItems: AlignItems = AlignItems.Stretch,
    columnBuilder: (GridTrackBuilderInRepeat.() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Div(
        Modifier
            .display(DisplayStyle.Grid)
            .thenIf(columnBuilder != null) {
                Modifier.gridTemplateColumns {
                    repeat(GridEntry.Repeat.Auto.Type.AutoFit, columnBuilder!!)
                }
            }
            .justifyItems(justifyItems)
            .alignItems(alignItems)
            .gridGap(x = gridGap.x, y = gridGap.y)
            .then(modifier)
            .toAttrs()
    ) {
        content()
    }
}

data class GridGap(
    val x: CSSLengthOrPercentageNumericValue,
    val y: CSSLengthOrPercentageNumericValue = x,
) {
    companion object {
        val Zero = GridGap(0.px, 0.px)
    }
}
