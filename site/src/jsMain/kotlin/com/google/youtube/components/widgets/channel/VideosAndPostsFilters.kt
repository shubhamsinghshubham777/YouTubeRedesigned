package com.google.youtube.components.widgets.channel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.components.widgets.VerticalDivider
import com.google.youtube.utils.Wrap
import com.google.youtube.utils.Asset
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import org.jetbrains.compose.web.css.px

@Composable
fun VideosAndPostsFilters(
    filterState: MutableState<VideosAndPostsFilter>,
    layoutTypeState: MutableState<VideosAndPostsLayoutType>,
) {
    Wrap(8) {
        VideosAndPostsFilter.entries.forEach { filter ->
            AssetSvgButton(
                id = "${filter.name}_filter_button",
                isDense = true,
                isSelected = filterState.value == filter,
                onClick = { filterState.value = filter },
                text = filter.name,
                type = AssetSvgButtonType.SelectableChip,
            )
        }
        VerticalDivider(modifier = Modifier.margin(leftRight = 8.px).height(28.px))
        VideosAndPostsLayoutType.entries.forEach { layout ->
            AssetSvgButton(
                id = "${layout.name}_layout_type_button",
                isDense = true,
                isSelected = layoutTypeState.value == layout,
                onClick = { layoutTypeState.value = layout },
                startIconPath = when (layout) {
                    VideosAndPostsLayoutType.Grid -> Asset.Path.GRID
                    VideosAndPostsLayoutType.List -> Asset.Path.LIST
                },
                type = AssetSvgButtonType.SelectableChip,
            )
        }
    }
}

enum class VideosAndPostsFilter { Latest, Popular, Oldest }
enum class VideosAndPostsLayoutType { Grid, List }
