package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Styles
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexShrink
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.styleModifier
import com.varabyte.kobweb.silk.components.graphics.Image
import org.jetbrains.compose.web.css.px

@Composable
fun HomePage(showPersonalisedFeedDialogState: MutableState<Boolean>) {
    var selectedFilterIndex by remember { mutableIntStateOf(0) }
    Column {
        Row(
            modifier = Modifier
                .margin(top = 12.px)
                .fillMaxWidth()
                .overflow(Overflow.Scroll)
                .styleModifier { property("scrollbar-width", "none") },
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
