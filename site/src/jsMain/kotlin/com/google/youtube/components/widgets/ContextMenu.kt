package com.google.youtube.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.google.youtube.utils.Assets
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.Styles
import com.google.youtube.utils.clickable
import com.google.youtube.utils.limitTextWithEllipsis
import com.google.youtube.utils.rememberMouseEventAsState
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element

@Composable
fun <T> ContextMenu(
    state: T,
    modifier: Modifier = Modifier,
    isElevated: Boolean = true,
    content: (T) -> List<ContextMenuChild>,
) {
    val heightFromState: (T) -> Float = remember {
        { state ->
            val list = content(state)
            list
                .sumOf { contextMenuChildren -> contextMenuChildren.heightPx }
                .plus(
                    if ((list.firstOrNull() as? ContextMenuChild.ListItem)?.isTopItem == true) 0
                    else 2 * CONTAINER_PADDING_VERTICAL
                )
                .toFloat()
        }
    }

    val isAnimationRunningState = remember { mutableStateOf(false) }
    var currentState by remember { mutableStateOf(state) }
    val animatedIsElevated = updateTransition(isElevated).currentState
    val animatedContainerColor by animateColorAsState(
        Styles.SURFACE_ELEVATED.copyf(alpha = if (animatedIsElevated) 1f else 0.5f).toComposeColor()
    )
    var animatedContainerHeight by remember { mutableFloatStateOf(heightFromState(state)) }
    var contextMenuWidth by remember { mutableFloatStateOf(0f) }

    val animationSpec = remember { tween<Float>() }

    var firstItemOpacity by remember { mutableFloatStateOf(1f) }
    var secondItemOpacity by remember { mutableFloatStateOf(0f) }

    var firstItemTranslationX by remember { mutableFloatStateOf(0f) }
    var secondItemTranslationX by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(state) {
        if (state != currentState && contextMenuWidth != 0f) {
            isAnimationRunningState.value = true
            launch {
                Animatable(initialValue = 0f).animateTo(
                    targetValue = 1f,
                    animationSpec = animationSpec
                ) {
                    firstItemOpacity = 1f - value
                    secondItemOpacity = value
                }
            }

            launch {
                Animatable(initialValue = 0f).animateTo(
                    targetValue = -contextMenuWidth,
                    animationSpec = animationSpec
                ) { firstItemTranslationX = value }
            }

            val animationJob = launch {
                launch {
                    Animatable(initialValue = animatedContainerHeight).animateTo(
                        targetValue = heightFromState(state),
                        animationSpec = animationSpec
                    ) { animatedContainerHeight = value }
                }

                Animatable(initialValue = contextMenuWidth).animateTo(
                    targetValue = 0f,
                    animationSpec = animationSpec
                ) { secondItemTranslationX = value }
            }

            // Wait for all animations to complete
            animationJob.join()

            // Reset states
            currentState = state
            firstItemOpacity = 1f
            secondItemOpacity = 0f
            firstItemTranslationX = 0f
            secondItemTranslationX = contextMenuWidth
            isAnimationRunningState.value = false
        }
    }

    Box(
        ref = ref { element ->
            if (contextMenuWidth == 0f) contextMenuWidth = element.offsetWidth.toFloat()
        },
        modifier = Modifier
            .background(animatedContainerColor.toKobwebColor())
            .clip(Rect(20.px))
            .minWidth(200.px)
            .height(animatedContainerHeight.px)
            .overflow(Overflow.Scroll)
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        content(currentState).toComposables(
            modifier = Modifier
                .translateX(firstItemTranslationX.px)
                .opacity(firstItemOpacity),
            isAnimationRunningState = isAnimationRunningState
        )
        if (state != currentState) {
            content(state).toComposables(
                modifier = Modifier
                    .translateX(secondItemTranslationX.px)
                    .opacity(secondItemOpacity),
                isAnimationRunningState = isAnimationRunningState
            )
        }
    }
}

@Composable
private fun List<ContextMenuChild>.toComposables(
    modifier: Modifier = Modifier,
    isAnimationRunningState: State<Boolean>,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        forEach { child ->
            when (child) {
                is ContextMenuChild.ListItem -> {
                    var buttonRef: Element? by remember { mutableStateOf(null) }
                    val mouseEventState by rememberMouseEventAsState(buttonRef)
                    val animatedContainerColor by animateColorAsState(
                        if (isAnimationRunningState.value) Color.Transparent
                        else when (mouseEventState) {
                            MouseEventState.Pressed -> Color.White.copy(Styles.Opacity.PRESSED)
                            MouseEventState.Hovered -> Color.White.copy(Styles.Opacity.HOVERED)
                            MouseEventState.Released -> Color.Transparent
                        }
                    )

                    Row(
                        ref = ref { ref -> buttonRef = ref },
                        modifier = Modifier
                            .clickable { child.onClick?.invoke() }
                            .background(animatedContainerColor.toKobwebColor())
                            .fillMaxWidth()
                            .padding(leftRight = CONTAINER_PADDING_HORIZONTAL.px)
                            .height(child.heightPx.px)
                            .userSelect(UserSelect.None),
                        horizontalArrangement = Arrangement.spacedBy(15.px),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        child.leadingContent?.let { leadingContent ->
                            AssetSvg(
                                id = "context_menu_leading_asset_${child.text}",
                                path = when (leadingContent) {
                                    is ContextMenuChild.ListItem.LeadingContent.Asset ->
                                        leadingContent.path

                                    is ContextMenuChild.ListItem.LeadingContent.Check ->
                                        if (leadingContent.isChecked) Assets.Paths.CHECK
                                        else ContextMenuChild.ListItem.EMPTY_SPACE
                                },
                                primaryColor = Styles.WHITE.copyf(
                                    alpha = if (leadingContent.halfOpacity) 0.5f else 1f
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1)
                                .display(DisplayStyle.Block)
                                .limitTextWithEllipsis()
                                .fontSize(if (child.showSmallText) 14.px else 16.px)
                        ) { Text(child.text) }

                        child.trailingContent?.let { trailingContent ->
                            val arrowId = remember { "context_menu_trailing_arrow" }
                            when (trailingContent) {
                                ContextMenuChild.ListItem.TrailingContent.Arrow -> AssetSvg(
                                    id = arrowId,
                                    path = Assets.Paths.ARROW_RIGHT,
                                    primaryColor = Styles.WHITE.copyf(alpha = 0.5f)
                                )

                                is ContextMenuChild.ListItem.TrailingContent.Switch -> Switch(
                                    isSelected = trailingContent.isSelected,
                                    onSelectionChange = trailingContent.onSelectionChange,
                                    reactToMouseEvents = false,
                                )

                                is ContextMenuChild.ListItem.TrailingContent.Text -> Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.px),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(modifier = Modifier.opacity(0.7).fontSize(14.px)) {
                                        Text(trailingContent.value)
                                    }

                                    if (trailingContent.showArrow) {
                                        AssetSvg(
                                            id = arrowId,
                                            path = Assets.Paths.ARROW_RIGHT,
                                            primaryColor = Styles.WHITE.copyf(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                ContextMenuChild.HorizontalDivider -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(child.heightPx.px)
                        .backgroundColor(Styles.WHITE.copyf(alpha = 0.11f))
                )

                ContextMenuChild.VerticalSpacer -> Box(modifier = Modifier.height(child.heightPx.px))
            }
        }
    }
}

sealed class ContextMenuChild(val heightPx: Int) {
    data class ListItem(
        val text: String,
        val showSmallText: Boolean = false,
        val leadingContent: LeadingContent? = null,
        val trailingContent: TrailingContent? = null,
        val isTopItem: Boolean = false,
        val onClick: (() -> Unit)? = null,
    ) : ContextMenuChild(heightPx = if (isTopItem) 64 else 44) {
        sealed class LeadingContent(val halfOpacity: Boolean = false) {
            data class Check(
                val isChecked: Boolean,
                val hasHalfOpacity: Boolean = false,
            ) : LeadingContent(halfOpacity = hasHalfOpacity)

            data class Asset(
                val path: String,
                val hasHalfOpacity: Boolean = false,
            ) : LeadingContent(halfOpacity = hasHalfOpacity)
        }

        sealed class TrailingContent {
            data object Arrow : TrailingContent()

            data class Text(val value: String, val showArrow: Boolean = true) : TrailingContent()

            data class Switch(
                val isSelected: Boolean,
                val onSelectionChange: (Boolean) -> Unit,
            ) : TrailingContent()
        }

        companion object {
            /**
             * Useful when you want the [ListItem] to consume the space of a potential icon
             */
            const val EMPTY_SPACE = ""
        }
    }

    data object HorizontalDivider : ContextMenuChild(heightPx = 1)
    data object VerticalSpacer : ContextMenuChild(heightPx = CONTAINER_PADDING_VERTICAL)
}

private const val CONTAINER_PADDING_HORIZONTAL = 24
private const val CONTAINER_PADDING_VERTICAL = 10
