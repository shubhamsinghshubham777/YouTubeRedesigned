package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.utils.Constants
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.animatedColor
import com.google.youtube.utils.clickable
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberMouseEventAsState
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element

@Composable
fun SegmentedButtonPair(
    assetColorLeft: Color = Styles.WHITE,
    assetColorRight: Color = Styles.WHITE,
    assetPathLeft: String? = null,
    assetPathRight: String? = null,
    assetLeftStroked: Boolean = false,
    assetRightStroked: Boolean = false,
    containerColor: Color? = null,
    isDense: Boolean = false,
    isLeftLabelBold: Boolean = false,
    isRightLabelBold: Boolean = false,
    labelLeft: String? = null,
    labelRight: String? = null,
    onClickLeft: () -> Unit,
    onClickRight: () -> Unit,
    spacingPx: Int = 14,
) {
    val doesLeftButtonExist = remember(labelLeft, assetPathLeft) {
        labelLeft != null || assetPathLeft != null
    }
    val doesRightButtonExist = remember(labelRight, assetPathRight) {
        labelRight != null || assetPathRight != null
    }
    Row(
        modifier = Modifier
            .background(containerColor)
            .clip(Rect(100.px))
            .noShrink()
            .userSelect(UserSelect.None),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (doesLeftButtonExist) {
            val paddingTopBottom = remember(isDense, assetPathLeft) {
                when {
                    !isDense -> 8.px
                    isDense && assetPathLeft == null -> 8.px
                    else -> 4.px
                }
            }
            SegmentedItem(
                color = assetColorLeft,
                iconPath = assetPathLeft,
                pathStroked = assetLeftStroked,
                isDense = isDense,
                isLabelBold = isLeftLabelBold,
                label = labelLeft,
                modifier = Modifier.padding(
                    left = 16.px,
                    top = paddingTopBottom,
                    right = if (doesRightButtonExist) spacingPx.px else 16.px,
                    bottom = paddingTopBottom
                ),
                onClick = onClickLeft,
            )
        }
        // Vertical Divider (only shown if both buttons are available)
        if (doesLeftButtonExist && doesRightButtonExist) {
            Box(
                modifier = Modifier
                    .background(Styles.WHITE.copyf(alpha = 0.1f))
                    .size(
                        width = Constants.VERTICAL_DIVIDER_SIZE.width.px,
                        height = Constants.VERTICAL_DIVIDER_SIZE.height.px,
                    )
            )
        }
        if (doesRightButtonExist) {
            val paddingTopBottom = remember(isDense, assetPathRight) {
                when {
                    !isDense -> 8.px
                    isDense && assetPathRight == null -> 8.px
                    else -> 4.px
                }
            }
            SegmentedItem(
                color = assetColorRight,
                iconPath = assetPathRight,
                pathStroked = assetRightStroked,
                isDense = isDense,
                isLabelBold = isRightLabelBold,
                label = labelRight,
                modifier = Modifier.padding(
                    left = if (doesLeftButtonExist) spacingPx.px else 16.px,
                    top = paddingTopBottom,
                    right = 16.px,
                    bottom = paddingTopBottom,
                ),
                onClick = onClickRight,
            )
        }
    }
}

@Composable
private fun SegmentedItem(
    color: Color,
    iconPath: String?,
    pathStroked: Boolean,
    isDense: Boolean,
    isLabelBold: Boolean,
    label: String?,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    var elementRef by remember { mutableStateOf<Element?>(null) }
    val animatedContainerColor by rememberMouseEventAsState(elementRef).animatedColor()
    SpacedRow(
        ref = ref { e -> elementRef = e },
        spacePx = 7,
        modifier = Modifier
            .background(animatedContainerColor.toKobwebColor())
            .clickable(onClick = onClick)
            .then(modifier),
    ) {
        iconPath?.let { path ->
            AssetSvg(
                id = "segmented_item_$label",
                path = path,
                primaryColor = color,
                strokeOnly = pathStroked,
            )
        }
        label?.let { safeLabel ->
            TextBox(
                color = color,
                text = safeLabel,
                weight = if (isLabelBold) FontWeight.Medium else FontWeight.Normal,
                size = if (isDense) 14 else 16,
            )
        }
    }
}
