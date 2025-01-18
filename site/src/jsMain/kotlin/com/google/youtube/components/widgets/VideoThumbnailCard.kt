package com.google.youtube.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import com.google.youtube.utils.Assets
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.Styles
import com.google.youtube.utils.rememberMouseEventAsState
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.Shape
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element

@Composable
fun VideoThumbnailCard(
    thumbnailAsset: String,
    channelAsset: String,
    title: String,
    views: String,
    daysSinceUploaded: String,
    duration: String,
    shape: Shape = Rect(15.9.px),
    modifier: Modifier = Modifier,
    size: IntSize? = null,
) {
    var elementRef by remember { mutableStateOf<Element?>(null) }
    val mouseEvent by rememberMouseEventAsState(elementRef)
    val containerColor by animateColorAsState(
        when (mouseEvent) {
            MouseEventState.Pressed -> Color.White.copy(alpha = Styles.Opacity.HOVERED)
            else -> Color.Transparent
        }
    )
    Column(
        ref = ref { e -> elementRef = e },
        modifier = Modifier
            .background(containerColor.toKobwebColor())
            .clip(shape)
            .userSelect(UserSelect.None)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(15.px)
    ) {
        Box(
            modifier = Modifier
                .then(
                    size?.let { safeSize ->
                        Modifier.size(width = safeSize.width.px, height = safeSize.height.px)
                    } ?: Modifier.fillMaxSize()
                )
                .clip(shape),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Image(
                modifier = Modifier.thenIf(size == null) { Modifier.fillMaxSize() },
                src = thumbnailAsset,
                width = size?.width,
                height = size?.height,
            )
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
    const val WIDTH: Int = 354
    const val HEIGHT: Int = 198
}
