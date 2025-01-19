package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.utils.Assets
import com.google.youtube.utils.HorizontalScrollState
import com.google.youtube.utils.Styles
import com.google.youtube.utils.bindScrollState
import com.google.youtube.utils.hideScrollBar
import com.google.youtube.utils.isGreaterThan
import com.google.youtube.utils.limitTextWithEllipsis
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexShrink
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element

@Composable
fun ColoredBorderContainer(
    onNegativeCTA: () -> Unit,
    onPositiveCTA: () -> Unit,
    color: Color,
    title: String,
    modifier: Modifier = Modifier,
    asset: String? = null,
    contentGapPx: Int = 15,
    scrollPixels: Double = 300.0,
    content: @Composable RowScope.() -> Unit,
) {
    val isLargeScreen = rememberBreakpoint().isGreaterThan(Breakpoint.SM)
    var scrollableRowElement by remember { mutableStateOf<Element?>(null) }
    val horizontalScrollState = remember { mutableStateOf(HorizontalScrollState.ReachedStart) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.px, LineStyle.Solid, color)
            .borderRadius(20.px)
            .padding(topBottom = CONTAINER_PADDING)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(CONTAINER_PADDING)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(leftRight = CONTAINER_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1).margin(right = 16.px),
                horizontalArrangement = Arrangement.spacedBy(8.px),
                verticalAlignment = Alignment.CenterVertically
            ) {
                asset?.let { safeAsset -> Image(src = safeAsset, width = 24, height = 24) }
                Box(
                    modifier = Modifier
                        .limitTextWithEllipsis(maxLines = 2)
                        .fontFamily(Styles.Fonts.ROBOTO_CONDENSED)
                        .fontSize(24.px)
                        .fontWeight(FontWeight.Medium)
                ) { Text(title) }
            }

            Row(
                modifier = Modifier.flexShrink(0),
                horizontalArrangement = Arrangement.spacedBy(12.px)
            ) {
                AssetSvgButton(
                    id = "negative_cta_$title",
                    text = if (isLargeScreen) "Not interested" else null,
                    dense = true,
                    startIconPath = Assets.Paths.CLOSE,
                    onClick = onNegativeCTA,
                )
                AssetSvgButton(
                    id = "positive_cta_$title",
                    text = if (isLargeScreen) "Show me more" else null,
                    dense = true,
                    startIconPath = Assets.Paths.LIKED,
                    onClick = onPositiveCTA,
                    containerColor = color,
                )
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                ref = ref { element -> scrollableRowElement = element },
                modifier = Modifier
                    .bindScrollState(horizontalScrollState)
                    .fillMaxWidth()
                    .hideScrollBar()
                    .overflow { x(Overflow.Scroll) }
                    .padding(leftRight = CONTAINER_PADDING)
                    .scrollBehavior(ScrollBehavior.Smooth),
                horizontalArrangement = Arrangement.spacedBy(contentGapPx.px),
                content = content
            )

            RowScrollButtons(
                elementToControl = scrollableRowElement,
                horizontalScrollState = horizontalScrollState,
                containerPadding = CONTAINER_PADDING,
                scrollPixels = scrollPixels + contentGapPx,
                gradientColor = Styles.SURFACE,
                centerVertically = true,
            )
        }
    }
}

private val CONTAINER_PADDING = 30.px
