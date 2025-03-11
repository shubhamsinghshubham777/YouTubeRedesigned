package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.youtube.data.ChannelDataProvider
import com.google.youtube.utils.Asset
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.Route
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
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.times
import org.jetbrains.compose.web.dom.Text

@Composable
fun ChannelBriefPopupPage(channelId: String) {
    val navigator = LocalNavigator.current
    val channelDataProvider = remember { ChannelDataProvider() }
    val data = remember(channelDataProvider, channelId) {
        channelDataProvider.getChannelBriefForId(channelId)
    }

    Column(
        modifier = Modifier.margin(topBottom = containerPaddingVertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.px),
    ) {
        // Channel Details
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(leftRight = containerPaddingHorizontal)
                .minWidth(370.px),
        ) {
            Image(
                modifier = Modifier.clip(Circle()),
                src = data.assetRef,
                width = imageSizePx,
                height = imageSizePx,
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
                    ) { Text(data.name) }
                    if (data.isVerified) Image(Asset.Icon.VERIFIED_BADGE)
                }
                Box { Text("@${data.id}") }
                Box(
                    modifier = Modifier
                        .color(Styles.VIDEO_CARD_SECONDARY_TEXT)
                        .limitTextWithEllipsis()
                ) {
                    Text("Joined ${data.joinedSince} ago • ${data.subscribersCount} subscribers")
                }
            }

            AssetImageButton(Asset.Icon.MORE) {}
        }

        // Action Buttons
        Row(
            modifier = Modifier.margin(
                top = containerPaddingVertical,
                leftRight = containerPaddingHorizontal,
            ),
            horizontalArrangement = Arrangement.spacedBy(12.px),
        ) {
            SubscribeButton(showPopup = false)

            AssetSvgButton(
                id = "view_channel_button",
                onClick = { navigator.pushRoute(Route.Page(id = channelId)) },
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
            ) { Text("${data.commentsCount} comments") }
        }
    }
}

private val containerPaddingHorizontal = 16.px
private val containerPaddingVertical = 12.px
private const val imageSizePx = 72
