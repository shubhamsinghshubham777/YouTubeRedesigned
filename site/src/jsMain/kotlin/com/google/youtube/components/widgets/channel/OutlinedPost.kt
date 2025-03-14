package com.google.youtube.components.widgets.channel

import androidx.compose.runtime.Composable
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.SegmentedButtonPair
import com.google.youtube.models.ChannelListItemData
import com.google.youtube.utils.Asset
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.Wrap
import com.google.youtube.utils.noShrink
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px

@Composable
fun OutlinedPost(
    data: ChannelListItemData.Post,
    modifier: Modifier = Modifier,
    onComment: () -> Unit,
    onDislike: () -> Unit,
    onLike: () -> Unit,
    onShare: () -> Unit,
    showImageOnBottom: Boolean = false,
) {
    Column(
        modifier = Modifier
            .border(1.px, LineStyle.Solid, Styles.DIVIDER)
            .borderRadius(16.px)
            .fillMaxSize()
            .noShrink()
            .padding(16.px)
            .then(modifier),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            TextBox(
                lineHeight = 25,
                modifier = Modifier.weight(1),
                size = 18,
                text = data.message,
                weight = FontWeight.Medium,
                color = Styles.OFF_WHITE,
            )
            SpacedRow(spacePx = 8, centerContentVertically = false) {
                if (!showImageOnBottom) {
                    data.postAsset?.let { asset ->
                        Image(
                            modifier = Modifier
                                .clip(Rect(4.px))
                                .margin(left = 8.px)
                                .objectFit(ObjectFit.Cover),
                            src = asset,
                            width = 134,
                            height = 134,
                        )
                    }
                }
                if (data.isPinned) Image(src = Asset.Icon.PIN_SELECTED, width = 24, height = 24)
                AssetImageButton(Asset.Icon.MORE) {}
            }
        }

        if (showImageOnBottom) {
            data.postAsset?.let { asset ->
                Image(
                    modifier = Modifier
                        .clip(Rect(4.px))
                        .fillMaxWidth(50.percent)
                        .margin(topBottom = 32.px)
                        .objectFit(ObjectFit.Cover),
                    src = asset,
                )
            }
        }

        SpacedColumn(spacePx = 16) {
            TextBox(
                text = "${data.daysSinceUploaded} ago",
                color = Styles.VIDEO_CARD_SECONDARY_TEXT,
                lineHeight = 26.5,
            )
            Wrap(8) {
                SegmentedButtonPair(
                    isDense = true,
                    assetPathLeft = Asset.Path.LIKED,
                    assetPathRight = Asset.Path.DISLIKE,
                    labelLeft = data.likeCount,
                    labelRight = data.dislikeCount,
                    onClickLeft = onLike,
                    onClickRight = onDislike,
                    containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
                )
                SegmentedButtonPair(
                    isDense = true,
                    assetPathLeft = Asset.Path.COMMENTS,
                    assetPathRight = Asset.Path.SHARE,
                    labelLeft = data.commentCount,
                    onClickLeft = onComment,
                    onClickRight = onShare,
                    containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
                )
            }
        }
    }
}
