package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.data.CollectionsDataProvider
import com.google.youtube.models.CollectionPageData
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.Wrap
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import org.jetbrains.compose.web.css.px

@Composable
fun CollectionPage(collectionId: String) {
    val collectionsDataProvider = remember { CollectionsDataProvider() }
    val data: CollectionPageData = remember(collectionsDataProvider) {
        collectionsDataProvider.getVideosForCollectionId(collectionId)
    }
    SpacedColumn(spacePx = 39, modifier = Modifier.fillMaxWidth()) {
        Wrap(modifier = Modifier.fillMaxWidth(), horizontalGapPx = 16) {
            SpacedRow(spacePx = 4, modifier = Modifier.weight(1)) {
                TextBox(text = "Collection:", size = 20, color = Styles.WHITE.copyf(alpha = 0.5f))
                TextBox(text = data.name, size = 20)
            }
            AssetSvgButton(
                id = "manage_collection_button",
                isDense = true,
                onClick = {},
                text = "Manage Collection",
            )
        }
        MainVideosGrid(videos = data.videos, minWidth = 356.px)
    }
}
