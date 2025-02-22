package com.google.youtube.components.widgets.subscriptions

import androidx.compose.runtime.Composable
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.SegmentedButtonPair
import com.google.youtube.models.ChannelListItemData
import com.google.youtube.utils.Assets
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.noShrink
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px

@Composable
fun MessagePostCard(data: ChannelListItemData.Post) {
    Column(
        modifier = Modifier
            .border(1.px, LineStyle.Solid, Styles.DIVIDER)
            .borderRadius(16.px)
            .noShrink()
            .padding(16.px)
            .size(356.px, 289.px),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        SpacedRow(
            centerContentVertically = false,
            modifier = Modifier.height(160.px),
            spacePx = 8,
        ) {
            TextBox(
                color = Styles.OFF_WHITE,
                lineHeight = 25,
                modifier = Modifier.weight(1),
                size = 18,
                text = data.message,
                weight = FontWeight.Medium,
            )
            AssetImageButton(Assets.Icons.MORE) {}
        }
        TextBox(
            color = Styles.VIDEO_CARD_SECONDARY_TEXT,
            lineHeight = 26.5,
            text = "${data.daysAgo} ago",
        )
        SpacedRow(8) {
            SegmentedButtonPair(
                isDense = true,
                assetPathLeft = Assets.Paths.LIKED,
                labelLeft = data.likeCount,
                assetPathRight = Assets.Paths.DISLIKE,
                labelRight = data.dislikeCount,
                onClickLeft = {},
                onClickRight = {},
                containerColor = Styles.SURFACE_ELEVATED,
            )
            SegmentedButtonPair(
                isDense = true,
                assetPathLeft = Assets.Paths.COMMENTS,
                labelLeft = data.commentCount,
                assetPathRight = Assets.Paths.SHARE,
                onClickLeft = {},
                onClickRight = {},
                containerColor = Styles.SURFACE_ELEVATED,
                assetRightStroked = true,
            )
        }
    }
}
