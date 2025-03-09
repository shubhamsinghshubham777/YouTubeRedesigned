package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.youtube.components.widgets.notification.NotificationListItem
import com.google.youtube.data.NotificationDataProvider
import com.google.youtube.utils.Asset
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vw

@Composable
fun NotificationPanel() {
    val notificationDataProvider = remember { NotificationDataProvider() }
    val notifications = remember(notificationDataProvider) {
        notificationDataProvider.getNotificationsData()
    }

    Column(
        modifier = Modifier
            .background(Styles.SURFACE_ELEVATED)
            .clip(Rect(20.px))
            .size(width = 539.px, height = 683.px)
            .maxWidth(90.vw),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(leftRight = 19.px, topBottom = 14.px),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextBox(text = "Notifications", size = 18, weight = FontWeight.Medium)
            AssetImageButton(asset = Asset.Icon.SETTINGS, modifier = Modifier.opacity(0.5)) {}
        }

        HorizontalDivider()

        SpacedColumn(
            spacePx = 35,
            modifier = Modifier
                .fillMaxWidth()
                .overflow { y(Overflow.Scroll) }
                .padding(topBottom = 20.px),
        ) {
            notifications.forEach { notificationData ->
                NotificationListItem(data = notificationData)
            }
        }
    }
}
