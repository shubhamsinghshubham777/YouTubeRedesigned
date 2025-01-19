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
import com.google.youtube.utils.Assets
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.Styles
import com.google.youtube.utils.rememberMouseEventAsState
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.flexShrink
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.translateY
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
    containerColor: Color? = null,
    iconPrimaryColor: Color? = null,
    iconSecondaryColor: Color? = null,
    isSelected: Boolean = false,
    isOutlined: Boolean = false,
    startIconPath: String? = null,
    endIconPath: String? = null,
    iconSize: CSSLengthOrPercentageValue = if (type == AssetSvgButtonType.Button) 22.px else 24.px,
    text: String? = null,
    dense: Boolean = false,
    content: (@Composable RowScope.() -> Unit)? = null,
) {
    val updatedButtonType = updateTransition(type).currentState
    val updatedIsOutlined = updateTransition(isOutlined).currentState
    val updatedIsSelected = updateTransition(isSelected).currentState
    val borderRadius = remember {
        if (updatedButtonType == AssetSvgButtonType.Button) 24.px else 8.px
    }
    var elementRef: Element? by remember { mutableStateOf(null) }
    val mouseEventState by rememberMouseEventAsState(elementRef)
    val animatedContainerColor by animateColorAsState(
        (containerColor ?: Styles.WHITE).toRgb().copyf(
            alpha = mouseEventState.alphaFromMouseState(isSelected = updatedIsSelected)
        ).toComposeColor()
    )
    val animatedContentColor by animateColorAsState(
        when {
            updatedIsSelected -> Colors.Black
            else -> Styles.WHITE
        }.toComposeColor()
    )
    val animatedIconPrimaryColor by animateColorAsState(
        when {
            updatedIsSelected -> Colors.Black
            else -> iconPrimaryColor?.toRgb() ?: Styles.WHITE
        }.toComposeColor()
    )
    val animatedIconSecondaryColor by animateColorAsState(
        when {
            updatedIsSelected -> Colors.Black
            else -> iconSecondaryColor?.toRgb() ?: Styles.WHITE
        }.toComposeColor()
    )

    Box(
        modifier = Modifier
            .flexShrink(0)
            .height(
                when {
                    updatedButtonType == AssetSvgButtonType.Button -> if (dense) 32.px else 40.px
                    else -> 36.px
                }
            )
            .background(animatedContainerColor.toKobwebColor().darkened(0.2f))
            .clip(Rect(borderRadius)),
    ) {
        Row(
            ref = ref { elementRef = it },
            modifier = Modifier
                .fillMaxHeight()
                .then(
                    if (updatedIsOutlined) {
                        Modifier
                            .borderRadius(borderRadius)
                            .border(1.px, LineStyle.Solid, Styles.WHITE.copyf(alpha = 0.2f))
                    } else Modifier
                )
                .cursor(Cursor.Pointer)
                .padding(
                    left = animateFloatAsState(
                        when (updatedButtonType) {
                            AssetSvgButtonType.Button -> when {
                                startIconPath != null && dense -> 12f
                                startIconPath != null -> 16f
                                else -> 24f
                            }

                            else -> if (startIconPath != null || content != null) 12f else 16f
                        }
                    ).value.px,
                    right = animateFloatAsState(
                        when (updatedButtonType) {
                            AssetSvgButtonType.Button -> when {
                                endIconPath != null && dense -> 12f
                                dense -> 16f
                                else -> 24f
                            }

                            else -> if (endIconPath != null || content != null) 12f else 16f
                        }
                    ).value.px,
                )
                .onClick { onClick() }
                .color(animatedContentColor.toKobwebColor())
                .fontSize(if (updatedButtonType == AssetSvgButtonType.Button) 16.px else 14.px)
                .fontWeight(if (dense) FontWeight.Normal else FontWeight.Medium)
                .userSelect(UserSelect.None),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val iconPath = when (updatedButtonType) {
                AssetSvgButtonType.FilterChip -> startIconPath ?: Assets.Paths.CHECK
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
                Box(
                    modifier = Modifier
                        .translateY(1.px)
                        .thenIf(
                            updatedButtonType != AssetSvgButtonType.Button,
                            Modifier.padding(topBottom = if (dense) 0.px else 4.px),
                        )
                        .fontSize(if (dense) 14.px else 16.px)
                ) { Text(safeText) }
            }

            AnimatedVisibility(
                isVisible = updatedButtonType != AssetSvgButtonType.FilterChip && endIconPath != null
            ) {
                AssetSvg(
                    id = "asset_svg_button_end_$id",
                    path = endIconPath.orEmpty(),
                    size = iconSize,
                    primaryColor = animatedIconPrimaryColor.toKobwebColor(),
                    secondaryColor = animatedIconSecondaryColor.toKobwebColor()
                ) { marginLeft(8.px) }
            }
        }
    }
}

private fun MouseEventState.alphaFromMouseState(isSelected: Boolean) = if (isSelected) when (this) {
    MouseEventState.Pressed -> Styles.Opacity.PRESSED_SELECTED
    MouseEventState.Hovered -> Styles.Opacity.HOVERED_SELECTED
    MouseEventState.Released -> 1f
} else when (this) {
    MouseEventState.Pressed -> Styles.Opacity.PRESSED
    MouseEventState.Hovered -> Styles.Opacity.HOVERED
    MouseEventState.Released -> 0.15f
}

enum class AssetSvgButtonType { Button, SelectableChip, FilterChip }
