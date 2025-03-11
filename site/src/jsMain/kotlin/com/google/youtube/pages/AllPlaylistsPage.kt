package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.components.widgets.PlaylistListItem
import com.google.youtube.components.widgets.context.RoundedSearchTextField
import com.google.youtube.data.PlaylistDataProvider
import com.google.youtube.utils.Asset
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.Wrap
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
    val playlistDataProvider = remember { PlaylistDataProvider() }
    val playlists = remember(playlistDataProvider) { playlistDataProvider.getAllPlaylists() }

    SpacedColumn(spacePx = 28, modifier = Modifier.fillMaxWidth().padding(bottom = 28.px)) {
        TopBar(isGridModeSelected, searchQueryState)
        playlists.forEach { data -> PlaylistListItem(data) }
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
                endIconPath = Asset.Path.ARROW_DOWN,
                onClick = {},
            )
        }
        AssetSvgButton(
            id = "grid_mode_button",
            type = AssetSvgButtonType.SelectableChip,
            startIconPath = Asset.Path.GRID,
            isSelected = isGridModeSelected.value,
            onClick = { isGridModeSelected.value = true },
        )
        AssetSvgButton(
            id = "list_mode_button",
            type = AssetSvgButtonType.SelectableChip,
            startIconPath = Asset.Path.LIST,
            isSelected = !isGridModeSelected.value,
            onClick = { isGridModeSelected.value = false },
        )
        RoundedSearchTextField(
            textState = searchQueryState,
            hintText = "Search playlists"
        ) { marginLeft(8.px) }
    }
}
