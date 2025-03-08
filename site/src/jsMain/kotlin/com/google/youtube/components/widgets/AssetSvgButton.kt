package com.google.youtube.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Asset
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.Styles
import com.google.youtube.utils.clickable
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberMouseEventAsState
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.CSSLengthOrPercentageValue
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element

@Composable
fun AssetSvgButton(
    onClick: () -> Unit,
    id: String,
    type: AssetSvgButtonType = AssetSvgButtonType.Button,
    iconPrimaryColor: Color? = null,
    iconSecondaryColor: Color? = null,
    isSelected: Boolean = false,
    isOutlined: Boolean = false,
    containerColor: Color = Styles.WHITE.copyf(alpha = if (isSelected) 1f else Styles.Opacity.ASSET_SVG_BUTTON),
    contentColor: Color = if (isSelected) Styles.BLACK else Styles.WHITE,
    startIconPath: String? = null,
    endIconPath: String? = null,
    iconSize: CSSLengthOrPercentageValue = if (type == AssetSvgButtonType.Button) 22.px else 24.px,
    text: String? = null,
    secondaryText: String? = null,
    isDense: Boolean = false,
    onEndIconClick: (() -> Unit)? = null,
    content: (@Composable RowScope.() -> Unit)? = null,
) {
    val updatedButtonType = updateTransition(type).currentState
    val updatedIsOutlined = updateTransition(isOutlined).currentState
    val updatedIsSelected = updateTransition(isSelected).currentState
    val updatedContainerColor = updateTransition(containerColor).currentState
    val updatedContentColor = updateTransition(contentColor).currentState
    val updatedIconPrimaryColor = updateTransition(iconPrimaryColor).currentState
    val updatedIconSecondaryColor = updateTransition(iconSecondaryColor).currentState

    val borderRadius = remember {
        if (updatedButtonType == AssetSvgButtonType.Button) 24.px else 8.px
    }
    var elementRef: Element? by remember { mutableStateOf(null) }
    val mouseEventState by rememberMouseEventAsState(elementRef)
    val animatedHighlightColor by animateColorAsState(
        Styles.WHITE.toRgb().toComposeColor().copy(
            alpha = when (mouseEventState) {
                MouseEventState.Pressed -> Styles.Opacity.PRESSED
                MouseEventState.Hovered -> Styles.Opacity.HOVERED
                MouseEventState.Released -> 0f
            }
        )
    )
    val animatedContainerColor by animateColorAsState(
        updatedContainerColor.toRgb().toComposeColor()
    )
    val animatedContentColor by animateColorAsState(updatedContentColor.toRgb().toComposeColor())
    val animatedIconPrimaryColor by animateColorAsState(
        when {
            updatedIconPrimaryColor != null -> updatedIconPrimaryColor
            updatedIsSelected -> Colors.Black
            else -> Styles.WHITE
        }.toRgb().toComposeColor()
    )
    val animatedIconSecondaryColor by animateColorAsState(
        when {
            updatedIconSecondaryColor != null -> updatedIconSecondaryColor
            updatedIsSelected -> Colors.Black
            else -> iconPrimaryColor ?: Styles.WHITE
        }.toRgb().toComposeColor()
    )

    Box(
        modifier = Modifier
            .noShrink()
            .height(
                when {
                    updatedButtonType == AssetSvgButtonType.Button -> if (isDense) 32.px else 40.px
                    else -> 36.px
                }
            )
            .background(animatedContainerColor.toKobwebColor())
            .clip(Rect(borderRadius)),
    ) {
        Row(
            ref = ref { elementRef = it },
            modifier = Modifier
                .borderRadius(borderRadius)
                .backgroundColor(animatedHighlightColor.toKobwebColor())
                .fillMaxHeight()
                .then(
                    if (updatedIsOutlined) {
                        Modifier
                            .borderRadius(borderRadius)
                            .border(1.px, LineStyle.Solid, Styles.WHITE.copyf(alpha = 0.2f))
                    } else Modifier
                )
                .padding(
                    left = animateFloatAsState(
                        when (updatedButtonType) {
                            AssetSvgButtonType.Button -> when {
                                startIconPath != null && isDense -> 12f
                                startIconPath != null -> 16f
                                else -> if (isDense) 12f else 24f
                            }

                            else -> if (startIconPath != null || content != null) 12f else 16f
                        }
                    ).value.px,
                    right = animateFloatAsState(
                        when (updatedButtonType) {
                            AssetSvgButtonType.Button -> when {
                                endIconPath != null && isDense -> 12f
                                endIconPath != null -> 16f
                                else -> if (isDense) 12f else 24f
                            }

                            else -> {
                                if (endIconPath != null || content != null || text == null) 12f
                                else 16f
                            }
                        }
                    ).value.px,
                )
                .clickable(onClick = onClick)
                .color(animatedContentColor.toKobwebColor())
                .fontSize(if (updatedButtonType == AssetSvgButtonType.Button) 16.px else 14.px)
                .fontWeight(if (isDense) FontWeight.Normal else FontWeight.Medium)
                .userSelect(UserSelect.None),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val iconPath = when (updatedButtonType) {
                AssetSvgButtonType.FilterChip -> startIconPath ?: Asset.Path.CHECK
                else -> startIconPath
            }

            AnimatedVisibility(
                isVisible = when (updatedButtonType) {
                    AssetSvgButtonType.FilterChip -> updatedIsSelected && iconPath != null
                    else -> iconPath != null
                }
            ) {
                AssetSvg(
                    id = "asset_svg_button_start_$id",
                    path = iconPath.orEmpty(),
                    size = iconSize,
                    primaryColor = animatedIconPrimaryColor.toKobwebColor(),
                    secondaryColor = animatedIconSecondaryColor.toKobwebColor()
                ) { if (text != null || content != null) marginRight(8.px) }
            }

            content?.invoke(this)

            text?.let { safeText ->
                Row(
                    modifier = Modifier
                        .thenIf(
                            updatedButtonType != AssetSvgButtonType.Button,
                            Modifier.padding(topBottom = if (isDense) 0.px else 4.px),
                        )
                        .fontSize(if (isDense) 14.px else 16.px),
                    horizontalArrangement = Arrangement.spacedBy(4.px)
                ) {
                    // TODO: Replace both of them with TextBox
                    secondaryText?.let { safeText ->
                        Box(modifier = Modifier.opacity(0.6f)) { Text(safeText) }
                    }
                    Box { Text(safeText) }
                }
            }

            AnimatedVisibility(
                isVisible = updatedButtonType != AssetSvgButtonType.FilterChip && endIconPath != null
            ) {
                AssetSvg(
                    id = "asset_svg_button_end_$id",
                    path = endIconPath.orEmpty(),
                    size = iconSize,
                    primaryColor = animatedIconPrimaryColor.toKobwebColor(),
                    secondaryColor = animatedIconSecondaryColor.toKobwebColor(),
                    onClick = onEndIconClick,
                ) { marginLeft(8.px) }
            }
        }
    }
}

enum class AssetSvgButtonType { Button, SelectableChip, FilterChip }
