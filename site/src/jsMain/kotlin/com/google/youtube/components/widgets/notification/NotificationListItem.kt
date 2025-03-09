package com.google.youtube.components.widgets.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.utils.Asset
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.LocalNotificationPanelManager
import com.google.youtube.utils.Route
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.clickable
import com.google.youtube.utils.limitTextWithEllipsis
import com.google.youtube.utils.noShrink
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.lineHeight
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element

@Composable
fun NotificationListItem(data: NotificationListItemData, modifier: Modifier = Modifier) {
    val notificationPanelManager = LocalNotificationPanelManager.current
    val navigator = LocalNavigator.current

    var rowRef by remember { mutableStateOf<Element?>(null) }

    SpacedRow(
        ref = ref { e -> rowRef = e },
        spacePx = 20,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                notificationPanelManager.close()
                when (data.videoType) {
                    is NotificationListItemVideoType.Video -> {
                        navigator.pushRoute(Route.Video(data.videoType.id))
                    }

                    is NotificationListItemVideoType.Short -> {
                        navigator.pushRoute(Route.Short(data.videoType.id))
                    }
                }
            }
            .noShrink()
            .padding(left = 19.px, right = 16.px)
            .then(modifier),
        centerContentVertically = false,
    ) {
        Image(modifier = Modifier.clip(Circle()).size(56.px), src = data.sharerChannelAsset)
        when (data.notificationType) {
            is NotificationListItemType.Upload -> {
                SpacedColumn(spacePx = 15, modifier = Modifier.weight(1)) {
                    AnnotatedText(
                        redText = data.sharerChannelUsername,
                        normalText = " uploaded: ${data.title}",
                    )

                    TextBox(
                        text = "${data.notificationType.timeSinceUpload} ago",
                        size = 14,
                        color = Styles.WHITE.copyf(alpha = 0.6f),
                    )

                    CTAButtons(id = data.id, onWatchLaterClick = {}, onShareClick = {})
                }

                Image(
                    modifier = Modifier.borderRadius(4.px).size(width = 84.px, height = 47.px),
                    src = data.thumbnailAsset,
                )

                AssetImageButton(Asset.Icon.MORE, modifier = Modifier.opacity(0.5)) {}
            }

            is NotificationListItemType.Share -> {
                SpacedColumn(8, modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        AssetImageButton(Asset.Icon.SHARE, modifier = Modifier.opacity(0.5))

                        AnnotatedText(
                            redText = data.sharerChannelUsername,
                            normalText = " shared with you:",
                        )

                        Spacer()

                        AssetImageButton(Asset.Icon.MORE, modifier = Modifier.opacity(0.5)) {}
                    }

                    Row(
                        modifier = Modifier
                            .background(Styles.WHITE.copyf(alpha = 0.07f))
                            .borderRadius(10.px)
                            .fillMaxWidth()
                            .padding(15.px),
                    ) {
                        SpacedColumn(20, modifier = Modifier.weight(1)) {
                            SpacedColumn(10) {
                                TextBox(data.title)
                                TextBox(
                                    text = data.notificationType.sharedVideoChannelUsername,
                                    size = 14,
                                    color = Styles.WHITE.copyf(alpha = 0.8f),
                                )
                            }

                            CTAButtons(id = data.id, onWatchLaterClick = {}, onShareClick = {})
                        }

                        Image(
                            modifier = Modifier.clip(Rect(4.px)).size(53.px, 97.px),
                            src = data.thumbnailAsset,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CTAButtons(id: String, onWatchLaterClick: () -> Unit, onShareClick: () -> Unit) {
    SpacedRow(10) {
        AssetSvgButton(
            id = "notification_${id}_water_later_btn",
            startIconPath = Asset.Path.WATCH_LATER,
            text = "Watch later",
            isDense = true,
            onClick = onWatchLaterClick,
        )

        AssetSvgButton(
            id = "notification_${id}_share_btn",
            startIconPath = Asset.Path.SHARE,
            text = "Share",
            isDense = true,
            onClick = onShareClick,
        )
    }
}

@Composable
private fun AnnotatedText(redText: String, normalText: String) {
    Span({
        style {
            fontSize(16.px)
            lineHeight(25.px)
            limitTextWithEllipsis(3)
        }
    }) {
        Span({ style { color(Styles.RED_NOTIFICATION_TITLE_ACCENT) } }) {
            Text(redText)
        }
        Text(normalText)
    }
}

@Immutable
data class NotificationListItemData(
    val id: String,
    val sharerChannelAsset: String,
    val sharerChannelUsername: String,
    val title: String,
    val notificationType: NotificationListItemType,
    val videoType: NotificationListItemVideoType,
    val thumbnailAsset: String,
)

sealed class NotificationListItemType {
    data class Upload(val timeSinceUpload: String) : NotificationListItemType()

    data class Share(val sharedVideoChannelUsername: String) : NotificationListItemType()
}

sealed class NotificationListItemVideoType(open val id: String) {
    data class Video(override val id: String) : NotificationListItemVideoType(id = id)
    data class Short(override val id: String) : NotificationListItemVideoType(id = id)
}
