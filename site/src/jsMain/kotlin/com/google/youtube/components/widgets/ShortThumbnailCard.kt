package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntSize
import com.google.youtube.models.ShortThumbnailDetails
import com.google.youtube.utils.Assets
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.Route
import com.google.youtube.utils.Styles
import com.google.youtube.utils.limitTextWithEllipsis
import com.google.youtube.utils.noShrink
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Shape
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Composable
fun ShortThumbnailCard(
    details: ShortThumbnailDetails,
    shape: Shape = Styles.Shape.CARD,
    modifier: Modifier = Modifier,
    size: IntSize = ShortThumbnailCardDefaults.SIZE,
) {
    val navigator = LocalNavigator.current

    Column(
        modifier = Modifier
            .cursor(Cursor.Pointer)
            .maxWidth(size.width.px)
            .noShrink()
            .onClick { navigator.pushRoute(Route.Short(id = details.id)) }
            .userSelect(UserSelect.None)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(15.px)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Image(
                modifier = Modifier
                    .clip(shape)
                    .objectFit(ObjectFit.Cover)
                    .size(width = size.width.px, height = size.height.px),
                src = details.thumbnailAsset,
            )
            AssetImageButton(modifier = Modifier.margin(4.px), asset = Assets.Icons.MORE) {}
        }

        Column(Modifier.color(Styles.VIDEO_CARD_SECONDARY_TEXT)) {
            Box(
                modifier = Modifier
                    .color(Styles.VIDEO_CARD_PRIMARY_TEXT)
                    .fontSize(18.px)
                    .fontWeight(FontWeight.Medium)
                    .limitTextWithEllipsis()
            ) { Text(details.title) }

            Box(Modifier.margin(topBottom = 8.px).limitTextWithEllipsis()) {
                Text(details.channelName)
            }

            Box(Modifier.limitTextWithEllipsis()) {
                Text("${details.views} views • ${details.daysSinceUploaded} ago")
            }
        }
    }
}

object ShortThumbnailCardDefaults {
    val SIZE = IntSize(width = 231, height = 409)
}
