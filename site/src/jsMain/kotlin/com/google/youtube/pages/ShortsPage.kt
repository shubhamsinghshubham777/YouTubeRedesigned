package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.google.youtube.components.widgets.FilterRow
import com.google.youtube.components.widgets.ShortThumbnailCard
import com.google.youtube.components.widgets.ShortThumbnailCardDefaults
import com.google.youtube.utils.Assets
import com.google.youtube.utils.BasicGrid
import com.google.youtube.utils.GridGap
import com.varabyte.kobweb.compose.css.JustifyItems
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px

@Composable
fun ShortsPage(
    showPersonalisedFeedDialogState: MutableState<Boolean>,
    horizontalPaddingState: State<Float>,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        FilterRow(showPersonalisedFeedDialogState = showPersonalisedFeedDialogState)
        BasicGrid(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 27.px,
                    right = horizontalPaddingState.value.px,
                    bottom = 27.px,
                ),
            gridGap = GridGap(x = 16.px, y = 40.px),
            columnBuilder = {
                minmax(ShortThumbnailCardDefaults.SIZE.width.px, 1.fr)
            },
            justifyItems = JustifyItems.Center,
        ) {
            repeat(20) {
                ShortThumbnailCard(
                    thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                    channelName = "DailyDoseOfInternet",
                    title = "Put this cat in jail",
                    views = "10M",
                    daysSinceUploaded = "3weeks"
                )
            }
        }
    }
}
