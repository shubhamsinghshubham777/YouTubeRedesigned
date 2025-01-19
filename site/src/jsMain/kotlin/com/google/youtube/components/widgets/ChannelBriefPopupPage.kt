package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Styles
import com.google.youtube.utils.limitTextWithEllipsis
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.times
import org.jetbrains.compose.web.dom.Text

@Composable
fun ChannelBriefPopupPage(channelId: String) {
    Column(
        modifier = Modifier.margin(topBottom = containerPaddingVertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.px)
    ) {
        // Image-Name-ID-Duration-Subscribers
        Row(modifier = Modifier.fillMaxWidth(100.percent - containerPaddingHorizontal)) {
            Image(
                src = Assets.Avatars.AVATAR_JACKSEPTICEYE,
                width = imageSizePx,
                height = imageSizePx
            )

            Column(
                modifier = Modifier
                    .margin(leftRight = containerPaddingHorizontal)
                    .weight(1)
                    .fontSize(Styles.FontSize.SMALL),
                verticalArrangement = Arrangement.spacedBy(8.px),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.px),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .fontSize(18.px)
                            .fontWeight(FontWeight.Medium)
                            .color(Styles.VIDEO_CARD_PRIMARY_TEXT),
                    ) { Text("jackscepticeye") }
                    Image(Assets.Icons.VERIFIED_BADGE)
                }
                Box { Text("@jacksepticeye") }
                Box(
                    modifier = Modifier
                        .color(Styles.VIDEO_CARD_SECONDARY_TEXT)
                        .limitTextWithEllipsis()
                ) {
                    Text("Joined 17 years ago • 30.8M subscribers")
                }
            }

            AssetImageButton(Assets.Icons.MORE) {}
        }

        // Action Buttons
        Row(
            modifier = Modifier.margin(
                top = containerPaddingVertical,
                leftRight = containerPaddingHorizontal
            ),
            horizontalArrangement = Arrangement.spacedBy(8.px)
        ) {
            AssetSvgButton(
                id = "subscribe_button",
                onClick = {},
                startIconPath = Assets.Paths.NOTIFS_SELECTED,
                endIconPath = Assets.Paths.ARROW_DOWN,
            ) {
                Text("Subscribed")
            }

            AssetSvgButton(
                id = "view_channel_button",
                onClick = {},
                startIconPath = "",
                endIconPath = "",
            ) {
                Text("View Channel")
            }
        }

        // Divider
        HorizontalDivider(Modifier.fillMaxWidth().backgroundColor(Styles.WHITE).opacity(0.1))

        // Comment Count
        Column(
            modifier = Modifier.fillMaxWidth(100.percent - (2 * containerPaddingHorizontal)),
            verticalArrangement = Arrangement.spacedBy(8.px),
        ) {
            Box(
                modifier = Modifier
                    .fontSize(18.px)
                    .fontWeight(FontWeight.Medium)
            ) { Text("On this channel") }

            Box(
                modifier = Modifier
                    .fontSize(Styles.FontSize.SMALL)
                    .color(Styles.VIDEO_CARD_SECONDARY_TEXT),
            ) { Text("100+ comments") }
        }
    }
}

private val containerPaddingHorizontal = 16.px
private val containerPaddingVertical = 12.px
private const val imageSizePx = 72
