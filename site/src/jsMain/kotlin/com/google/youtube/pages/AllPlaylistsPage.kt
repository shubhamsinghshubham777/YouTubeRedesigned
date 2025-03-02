package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.components.widgets.PlaylistListItem
import com.google.youtube.components.widgets.Wrap
import com.google.youtube.components.widgets.context_menu.RoundedSearchTextField
import com.google.youtube.models.PlaylistItemData
import com.google.youtube.utils.Assets
import com.google.youtube.utils.SpacedColumn
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.px

@Composable
fun AllPlaylistsPage() {
    val isGridModeSelected = remember { mutableStateOf(false) }
    val searchQueryState = remember { mutableStateOf("") }

    SpacedColumn(spacePx = 28, modifier = Modifier.fillMaxWidth().padding(bottom = 28.px)) {
        TopBar(isGridModeSelected, searchQueryState)
        remember {
            List(10) { index ->
                PlaylistItemData(
                    id = "collection_$index",
                    name = "Redesigns $index",
                    channelName = "Juxtopposed",
                    thumbnailImageRef = Assets.Thumbnails.THUMBNAIL_1,
                    channelImageRef = Assets.Icons.USER_AVATAR,
                    isChannelVerified = true,
                    subscriberCount = "288K",
                    viewsCount = "50",
                    videosCount = 15,
                    totalDuration = "2:51:23",
                )
            }
        }.forEach { data ->
            PlaylistListItem(data)
        }
    }
}

@Composable
private fun TopBar(
    isGridModeSelected: MutableState<Boolean>,
    searchQueryState: MutableState<String>,
) {
    Wrap(modifier = Modifier.fillMaxWidth(), horizontalGapPx = 8) {
        Box(modifier = Modifier.weight(1)) {
            AssetSvgButton(
                id = "sort_by_button",
                type = AssetSvgButtonType.SelectableChip,
                secondaryText = "Sort by:",
                text = "Relevance",
                isDense = true,
                endIconPath = Assets.Paths.ARROW_DOWN,
                onClick = {},
            )
        }
        AssetSvgButton(
            id = "grid_mode_button",
            type = AssetSvgButtonType.SelectableChip,
            startIconPath = Assets.Paths.GRID,
            isSelected = isGridModeSelected.value,
            onClick = { isGridModeSelected.value = true },
        )
        AssetSvgButton(
            id = "list_mode_button",
            type = AssetSvgButtonType.SelectableChip,
            startIconPath = Assets.Paths.LIST,
            isSelected = !isGridModeSelected.value,
            onClick = { isGridModeSelected.value = false },
        )
        RoundedSearchTextField(
            textState = searchQueryState,
            hintText = "Search playlists"
        ) { marginLeft(8.px) }
    }
}
