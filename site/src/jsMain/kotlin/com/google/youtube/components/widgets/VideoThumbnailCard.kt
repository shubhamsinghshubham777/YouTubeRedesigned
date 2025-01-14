package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Styles
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Composable
fun VideoThumbnailCard(
    thumbnailAsset: String,
    channelAsset: String,
    title: String,
    views: String,
    daysSinceUploaded: String,
    duration: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier.userSelect(UserSelect.None).then(modifier),
        verticalArrangement = Arrangement.spacedBy(15.px)
    ) {
        Box(
            modifier = Modifier
                .size(
                    width = VideoThumbnailCardDefaults.WIDTH.px,
                    height = VideoThumbnailCardDefaults.HEIGHT.px
                )
                .clip(Rect(15.9.px)),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Image(src = thumbnailAsset)
            Box(
                modifier = Modifier
                    .background(Styles.VIDEO_CARD_DURATION_CONTAINER)
                    .margin(10.px)
                    .padding(leftRight = 6.px, topBottom = 3.px)
                    .clip(Rect(6.px))
                    .fontWeight(FontWeight.Medium)
            ) { Text(duration) }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.px),
        ) {
            Image(src = channelAsset, modifier = Modifier.size(48.px))
            Column(
                modifier = Modifier
                    .weight(1)
                    .color(Styles.VIDEO_CARD_SECONDARY_TEXT),
                verticalArrangement = Arrangement.spacedBy(8.px),
            ) {
                Box(
                    modifier = Modifier
                        .fontSize(18.px)
                        .fontWeight(FontWeight.Medium)
                        .lineHeight(25.px)
                        .color(Styles.VIDEO_CARD_PRIMARY_TEXT)
                ) { Text(title) }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.px),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Juxtopposed")
                    Image(Assets.Icons.VERIFIED_BADGE)
                }
                Row { Text("$views views • $daysSinceUploaded") }
            }
            AssetImageButton(Assets.Icons.MORE) {}
        }
    }
}

object VideoThumbnailCardDefaults {
    const val WIDTH: Double = 354.0
    const val HEIGHT: Double = 198.0
}
