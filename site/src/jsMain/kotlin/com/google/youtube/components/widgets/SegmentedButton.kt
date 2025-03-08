package com.google.youtube.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Asset
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.Styles
import com.google.youtube.utils.clickable
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberMouseEventAsState
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.CSSLengthNumericValue
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.FlexBasis
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextOverflow
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.css.overflow
import com.varabyte.kobweb.compose.css.textOverflow
import com.varabyte.kobweb.compose.css.whiteSpace
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexBasis
import com.varabyte.kobweb.compose.ui.modifiers.flexGrow
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.div
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element

@Composable
fun SegmentedButton(
    segments: List<String>,
    selectedIndex: Int,
    containerHeight: CSSLengthOrPercentageNumericValue = 36.px,
    borderWidth: CSSLengthNumericValue = 1.px,
    borderColor: com.varabyte.kobweb.compose.ui.graphics.Color = Color(0xFF2C2C2C).toKobwebColor(),
    borderLineStyle: LineStyle = LineStyle.Solid,
    onSegmentClick: (Int) -> Unit,
) {
    val currentSelectedIndex = updateTransition(selectedIndex).currentState
    Row(modifier = Modifier.fillMaxWidth()) {
        segments.forEachIndexed { index, segmentLabel ->
            var buttonRef: Element? by remember { mutableStateOf(null) }
            val mouseEventState by rememberMouseEventAsState(buttonRef)
            val animatedContainerColor by animateColorAsState(
                if (index == currentSelectedIndex) when (mouseEventState) {
                    MouseEventState.Pressed -> Color.White.copy(Styles.Opacity.PRESSED_SELECTED)
                    MouseEventState.Hovered -> Color.White.copy(Styles.Opacity.HOVERED_SELECTED)
                    MouseEventState.Released -> Color.White
                }
                else when (mouseEventState) {
                    MouseEventState.Pressed -> Color.White.copy(Styles.Opacity.PRESSED)
                    MouseEventState.Hovered -> Color.White.copy(Styles.Opacity.HOVERED)
                    MouseEventState.Released -> Color.Transparent
                }
            )
            val animatedContentColor by animateColorAsState(
                if (index == currentSelectedIndex) Color.Black
                else Color(0xFFAAAAAA)
            )

            Row(
                ref = ref { element -> buttonRef = element },
                modifier = Modifier
                    .background(animatedContainerColor.toKobwebColor())
                    .border {
                        width(
                            top = borderWidth,
                            bottom = borderWidth,
                            left = if (index == segments.lastIndex) 0.px else borderWidth,
                            right = if (index == 0) 0.px else borderWidth,
                        )
                        color(borderColor)
                        style(borderLineStyle)
                    }
                    .borderRadius(
                        topLeft = if (index == 0) containerHeight / 2 else 0.px,
                        bottomLeft = if (index == 0) containerHeight / 2 else 0.px,
                        topRight = if (index == segments.lastIndex) containerHeight / 2 else 0.px,
                        bottomRight = if (index == segments.lastIndex) containerHeight / 2 else 0.px,
                    )
                    .color(animatedContentColor.toKobwebColor())
                    .clickable { onSegmentClick(index) }
                    .fillMaxWidth((100 / segments.size).percent)
                    .height(containerHeight)
                    .padding(leftRight = 8.px),
                horizontalArrangement = Arrangement.spacedBy(8.px, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(
                    modifier = Modifier
                        .flexBasis(FlexBasis.Auto)
                        .flexGrow(0)
                        .noShrink(),
                    isVisible = index == currentSelectedIndex
                ) {
                    AssetSvg(
                        id = "segmented_button_check_icon_$index",
                        path = Asset.Path.CHECK,
                        primaryColor = animatedContentColor.toKobwebColor(),
                    )
                }
                P(
                    attrs = {
                        style {
                            fontSize(14.px)
                            fontWeight(org.jetbrains.skia.FontWeight.MEDIUM)
                            overflow(Overflow.Hidden)
                            textOverflow(TextOverflow.Ellipsis)
                            whiteSpace(WhiteSpace.NoWrap)
                        }
                    }
                ) {
                    Text(segmentLabel)
                }
            }
        }
    }
}
