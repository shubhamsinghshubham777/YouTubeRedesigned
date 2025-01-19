package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Assets
import com.google.youtube.utils.HorizontalScrollState
import com.google.youtube.utils.Styles
import com.google.youtube.utils.bindScrollState
import com.google.youtube.utils.hideScrollBar
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.flexShrink
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element

@Composable
fun FilterRow(showPersonalisedFeedDialogState: MutableState<Boolean>) {
    var rowRef by remember { mutableStateOf<Element?>(null) }
    val horizontalScrollState = remember { mutableStateOf(HorizontalScrollState.ReachedStart) }
    var selectedFilterIndex by remember { mutableIntStateOf(0) }

    Box(contentAlignment = Alignment.Center) {
        Row(
            ref = ref { element -> rowRef = element },
            modifier = Modifier
                .overflow(Overflow.Scroll)
                .hideScrollBar()
                .scrollBehavior(ScrollBehavior.Smooth)
                .bindScrollState(horizontalScrollState),
            horizontalArrangement = Arrangement.spacedBy(12.px),
        ) {
            // Personalised Feed Button
            AssetSvgButton(
                id = "filter_preferences_button",
                type = AssetSvgButtonType.SelectableChip,
                onClick = { showPersonalisedFeedDialogState.value = true },
            ) {
                Image(src = Assets.Icons.MAGIC_FEED)
            }

            // Filter Chips
            filters.forEachIndexed { index, filter ->
                val isSelected = remember(index) { index == selectedFilterIndex }
                AssetSvgButton(
                    id = filter.name.replace(' ', '_'),
                    type = AssetSvgButtonType.SelectableChip,
                    text = filter.name,
                    startIconPath = filter.iconPath,
                    containerColor = if (!isSelected) filter.backgroundColor else null,
                    iconPrimaryColor = if (!isSelected) filter.iconPrimaryColor else null,
                    iconSecondaryColor = if (!isSelected) filter.iconSecondaryColor else null,
                    isSelected = index == selectedFilterIndex,
                    onClick = { selectedFilterIndex = index },
                )
            }

            // Spacer
            Box(modifier = Modifier.width(12.px).flexShrink(0))
        }

        repeat(2) { index ->
            val startItem = index == 0
            AnimatedVisibility(
                isVisible = if (startItem) horizontalScrollState.value != HorizontalScrollState.ReachedStart
                else horizontalScrollState.value != HorizontalScrollState.ReachedEnd,
                modifier = Modifier
                    .align(if (startItem) Alignment.CenterStart else Alignment.CenterEnd)
                    .zIndex(1)
            ) {
                Box(
                    modifier = Modifier
                        .backgroundImage(
                            linearGradient(
                                if (startItem) LinearGradient.Direction.ToRight
                                else LinearGradient.Direction.ToLeft
                            ) {
                                add(Styles.SURFACE)
                                setMidpoint(75.percent)
                                add(Colors.Transparent)
                            }
                        )
                        .padding(right = if (startItem) 0.px else 8.px)
                        .width(61.px),
                    contentAlignment = if (startItem) Alignment.CenterStart
                    else Alignment.CenterEnd,
                ) {
                    AssetImageButton(
                        asset = if (startItem) Assets.Icons.ARROW_LEFT
                        else Assets.Icons.ARROW_RIGHT,
                        onClick = {
                            rowRef?.scrollBy(x = if (startItem) -200.0 else 200.0, y = 0.0)
                        },
                    )
                }
            }
        }
    }
}

private class FilterInfo(
    val name: String,
    val iconPath: String? = null,
    val backgroundColor: Color? = null,
    val iconPrimaryColor: Color? = null,
    val iconSecondaryColor: Color? = null,
)

private const val SPECIAL_FILTER_NAME = "New Creators"

private val filters = listOf(
    FilterInfo(name = "All"),
    FilterInfo(name = "Subscriptions"),
    FilterInfo(name = "Posts"),
    FilterInfo(name = "Music"),
    FilterInfo(name = "Tech"),
    FilterInfo(name = "Design"),
    FilterInfo(name = "Live"),
    FilterInfo(name = "Playlists"),
    FilterInfo(name = "Cats"),
    FilterInfo(name = "Electronics"),
    FilterInfo(
        name = SPECIAL_FILTER_NAME,
        iconPath = Assets.Paths.DYNAMIC,
        backgroundColor = Styles.BACKGROUND_SELECTED,
        iconPrimaryColor = Styles.RED,
        iconSecondaryColor = Styles.PINK,
    ),
    FilterInfo(name = "Art"),
    FilterInfo(name = "Tech News"),
    FilterInfo(name = "Lofi beats"),
    FilterInfo(name = "UI/UX"),
)
