package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Styles
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.flexShrink
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onScroll
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

@Composable
fun HomePage(showPersonalisedFeedDialogState: MutableState<Boolean>) {
    var selectedFilterIndex by remember { mutableIntStateOf(0) }

    // Filter Row states
    var filterRowElement: Element? by remember { mutableStateOf(null) }
    var filterRowScrollState by remember { mutableStateOf(HorizontalScrollState.ReachedStart) }

    Column {
        // Filter Row
        Box(modifier = Modifier.margin(top = 12.px), contentAlignment = Alignment.Center) {
            Row(
                ref = ref { element -> filterRowElement = element },
                modifier = Modifier
                    .overflow(Overflow.Scroll)
                    .styleModifier { property("scrollbar-width", "none") }
                    .scrollBehavior(ScrollBehavior.Smooth)
                    .onScroll { event ->
                        val target = event.target as HTMLElement
                        when {
                            target.scrollLeft == 0.0 &&
                                    filterRowScrollState != HorizontalScrollState.ReachedStart ->
                                filterRowScrollState = HorizontalScrollState.ReachedStart

                            target.scrollWidth - (target.scrollLeft + target.clientWidth) <= 0.5 ->
                                filterRowScrollState = HorizontalScrollState.ReachedEnd

                            filterRowScrollState != HorizontalScrollState.Scrolling ->
                                filterRowScrollState = HorizontalScrollState.Scrolling
                        }
                    },
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
                    val isSelected = remember(index, selectedFilterIndex) {
                        index == selectedFilterIndex
                    }

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
                    isVisible = if (startItem) filterRowScrollState != HorizontalScrollState.ReachedStart
                    else filterRowScrollState != HorizontalScrollState.ReachedEnd,
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
                                filterRowElement?.scrollBy(
                                    x = if (startItem) -200.0 else 200.0,
                                    y = 0.0
                                )
                            },
                        )
                    }
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

private enum class HorizontalScrollState { ReachedStart, Scrolling, ReachedEnd }
