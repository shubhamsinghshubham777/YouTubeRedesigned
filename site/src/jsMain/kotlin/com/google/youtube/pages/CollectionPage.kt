package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.utils.Assets
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
    val data = remember { getCollectionData(collectionId) }
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
        MainVideosGrid(
            videos = remember {
                List(20) { index ->
                    VideoThumbnailDetails(
                        id = index.toString(),
                        thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                        channelAsset = Assets.Icons.USER_AVATAR,
                        title = "How Websites Learned to Fit Everywhere",
                        channelName = "Juxtopposed",
                        isVerified = true,
                        views = "150K",
                        daysSinceUploaded = "4 months",
                        duration = "12:07",
                    )
                }
            },
            minWidth = 356.px,
        )
    }
}

@Immutable
private data class CollectionPageData(val name: String, val videos: List<VideoThumbnailDetails>)

@Suppress("UNUSED_PARAMETER")
private fun getCollectionData(id: String) = CollectionPageData(
    name = "Gaming",
    videos = List(35) { index ->
        VideoThumbnailDetails(
            id = index.toString(),
            thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
            channelAsset = Assets.Icons.USER_AVATAR,
            title = "How Websites Learned to Fit Everywhere",
            channelName = "Juxtopposed",
            isVerified = true,
            views = "150K",
            daysSinceUploaded = "4 months",
            duration = "12:07",
        )
    }
)
