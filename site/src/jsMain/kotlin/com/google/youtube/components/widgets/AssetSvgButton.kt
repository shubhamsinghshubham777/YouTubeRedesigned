package com.google.youtube.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Assets
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.PaddingValues
import com.google.youtube.utils.Styles
import com.google.youtube.utils.onMouseEvent
import com.google.youtube.utils.removeMouseEventListeners
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.dom.disposableRef
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
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

@Composable
fun AssetSvgButton(
    onClick: () -> Unit,
    id: String,
    type: AssetSvgButtonType = AssetSvgButtonType.Button,
    isSelected: Boolean = false,
    isOutlined: Boolean = false,
    startIconPath: String? = null,
    endIconPath: String? = null,
    iconSize: CSSLengthOrPercentageValue = if (type == AssetSvgButtonType.Button) 22.px else 24.px,
    text: String,
) {
    val isSelectedTransition = updateTransition(isSelected)
    val isOutlinedTransition = updateTransition(isOutlined)
    val typeTransition = updateTransition(type)
    val borderRadius = remember {
        if (typeTransition.currentState == AssetSvgButtonType.Button) 24.px else 8.px
    }
    var mouseEventState by remember { mutableStateOf(MouseEventState.Released) }
    val contentPadding = remember(startIconPath) {
        when (typeTransition.currentState) {
            AssetSvgButtonType.Button -> PaddingValues(
                left = if (startIconPath != null) 16.px else 24.px,
                top = 10.px,
                right = if (endIconPath != null) 16.px else 24.px,
                bottom = 10.px,
            )

            else -> PaddingValues(
                left = if (startIconPath != null) 12.px else 16.px,
                top = 6.px,
                right = if (endIconPath != null) 12.px else 16.px,
                bottom = 6.px,
            )
        }
    }
    val animatedContainerColor by animateColorAsState(
        if (isSelectedTransition.currentState)
            when (mouseEventState) {
                MouseEventState.Pressed -> Styles.WHITE.copyf(alpha = Styles.Opacity.PRESSED_SELECTED)
                MouseEventState.Hovered -> Styles.WHITE.copyf(alpha = Styles.Opacity.HOVERED_SELECTED)
                MouseEventState.Released -> Styles.WHITE
            }.toComposeColor()
        else
            when (mouseEventState) {
                MouseEventState.Pressed -> Styles.WHITE.copyf(alpha = Styles.Opacity.PRESSED)
                MouseEventState.Hovered -> Styles.WHITE.copyf(alpha = Styles.Opacity.HOVERED)
                MouseEventState.Released -> {
                    when {
                        isOutlinedTransition.currentState -> Colors.Transparent

                        typeTransition.currentState != AssetSvgButtonType.Button ->
                            Styles.WHITE.copyf(alpha = Styles.Opacity.HOVERED)

                        else -> Styles.WHITE.copyf(alpha = 0.05f)
                    }
                }
            }.toComposeColor()
    )
    val animatedContentColor by animateColorAsState(
        when {
            isSelectedTransition.currentState -> Colors.Black
            else -> Styles.WHITE
        }.toComposeColor()
    )

    Row(
        ref = disposableRef { element ->
            val mouseEventCallbacks = element.onMouseEvent(
                onHoveredAndPressed = { mouseEventState = MouseEventState.Pressed },
                onHovered = { mouseEventState = MouseEventState.Hovered },
                onReleased = { mouseEventState = MouseEventState.Released }
            )
            onDispose { element.removeMouseEventListeners(mouseEventCallbacks) }
        },
        modifier = Modifier
            .background(animatedContainerColor.toKobwebColor())
            .clip(Rect(borderRadius))
            .then(
                if (isOutlinedTransition.currentState) {
                    Modifier
                        .borderRadius(borderRadius)
                        .border(1.px, LineStyle.Solid, Styles.WHITE.copyf(alpha = 0.2f))
                } else Modifier
            )
            .cursor(Cursor.Pointer)
            .padding(
                left = contentPadding.left,
                top = contentPadding.top,
                right = contentPadding.right,
                bottom = contentPadding.bottom,
            )
            .onClick { onClick() }
            .color(animatedContentColor.toKobwebColor())
            .fontSize(if (typeTransition.currentState == AssetSvgButtonType.Button) 16.px else 14.px)
            .fontWeight(FontWeight.Medium)
            .userSelect(UserSelect.None),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconPath = when (typeTransition.currentState) {
            AssetSvgButtonType.FilterChip -> startIconPath ?: Assets.Paths.CHECK
            else -> startIconPath
        }

        iconPath?.let { safeIconPath ->
            when (typeTransition.currentState) {
                AssetSvgButtonType.FilterChip -> AnimatedVisibility(isSelectedTransition.currentState) {
                    AssetSvg(
                        id = "asset_svg_button_start_$id",
                        path = safeIconPath,
                        size = iconSize,
                        primaryColor = animatedContentColor.toKobwebColor(),
                    ) { marginRight(8.px) }
                }

                else -> AssetSvg(
                    id = "asset_svg_button_start_$id",
                    path = safeIconPath,
                    size = iconSize,
                    primaryColor = animatedContentColor.toKobwebColor(),
                ) { marginRight(8.px) }
            }
        }

        Box(
            modifier = Modifier
                .translateY(1.px)
                .thenIf(
                    typeTransition.currentState != AssetSvgButtonType.Button,
                    Modifier.padding(topBottom = 4.px),
                )
        ) { Text(text) }

        if (typeTransition.currentState != AssetSvgButtonType.FilterChip) {
            endIconPath?.let { safeIconRef ->
                AssetSvg(
                    id = "asset_svg_button_end_$id",
                    path = safeIconRef,
                    size = iconSize,
                    primaryColor = animatedContentColor.toKobwebColor(),
                ) { marginLeft(8.px) }
            }
        }
    }
}

enum class AssetSvgButtonType { Button, SelectableChip, FilterChip }
