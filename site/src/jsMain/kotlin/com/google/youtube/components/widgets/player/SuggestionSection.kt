package com.google.youtube.components.widgets.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.utils.Assets
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.animatedColor
import com.google.youtube.utils.clickable
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberMouseEventAsState
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element

@Composable
fun SuggestionSection(author: String, videos: List<VideoThumbnailDetails>) {
    SpacedColumn(spacePx = 14, modifier = Modifier.fillMaxWidth()) {
        TextBox(
            modifier = Modifier.margin(bottom = 2.px),
            text = "From $author",
            size = 14,
        )
        videos.forEach { video ->
            var rowRef by remember { mutableStateOf<Element?>(null) }
            val animatedContainerColor by rememberMouseEventAsState(rowRef).animatedColor()
            SpacedRow(
                ref = ref { e -> rowRef = e },
                spacePx = 8,
                modifier = Modifier
                    .borderRadius(8.px)
                    .fillMaxWidth()
                    .background(animatedContainerColor.toKobwebColor())
                    .clickable {},
            ) {
                Box(
                    modifier = Modifier
                        .clip(Rect(8.px))
                        .noShrink()
                        .size(width = 168.px, height = 94.px),
                    contentAlignment = Alignment.BottomEnd,
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize().objectFit(ObjectFit.Cover),
                        src = video.thumbnailAsset
                    )
                    TextBox(
                        modifier = Modifier
                            .background(Styles.BLACK.copyf(alpha = 0.6f))
                            .clip(Rect(4.px))
                            .margin(4.px)
                            .padding(leftRight = 4.px, topBottom = 2.px),
                        text = video.duration,
                    )
                }
                SpacedColumn(4) {
                    TextBox(
                        color = Styles.VIDEO_CARD_PRIMARY_TEXT,
                        lineHeight = 20,
                        maxLines = 2,
                        size = 14,
                        text = video.title,
                        weight = FontWeight.Medium,
                    )
                    Column {
                        SpacedRow(4) {
                            TextBox(
                                color = Styles.VIDEO_CARD_SECONDARY_TEXT,
                                lineHeight = 18,
                                size = 12,
                                text = video.channelName,
                            )
                            if (video.isVerified) {
                                Image(
                                    modifier = Modifier.size(12.px),
                                    src = Assets.Icons.VERIFIED_BADGE,
                                )
                            }
                        }
                        TextBox(
                            color = Styles.VIDEO_CARD_SECONDARY_TEXT,
                            lineHeight = 18,
                            size = 12,
                            text = "${video.views} views • ${video.daysSinceUploaded} ago",
                        )
                    }
                }
            }
        }
    }
}
