package com.google.youtube.components.widgets.context_menu

import com.google.youtube.components.widgets.ContextMenuChild
import com.google.youtube.utils.Asset

val ContextMenuPageVideoOptions = listOf(
    ContextMenuChild.ListItem(
        text = "More Options",
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Asset(path = Asset.Path.FILTER, hasHalfOpacity = true),
        trailingContent = ContextMenuChild.ListItem.TrailingContent.Arrow,
        isTopItem = true,
        onClick = {}
    ),
    ContextMenuChild.HorizontalDivider,
    ContextMenuChild.VerticalSpacer,
    ContextMenuChild.ListItem(
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Asset(
                path = Asset.Path.SLEEP_TIMER,
                hasHalfOpacity = true
            ),
        text = "Sleep Timer",
        trailingContent = ContextMenuChild.ListItem.TrailingContent.Text(value = "OFF"),
    ),
    ContextMenuChild.ListItem(
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Asset(path = Asset.Path.CC, hasHalfOpacity = true),
        text = "Subtitles",
        trailingContent = ContextMenuChild.ListItem.TrailingContent.Text(value = "OFF"),
    ),
    ContextMenuChild.ListItem(
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Asset(path = Asset.Path.SPEED, hasHalfOpacity = true),
        text = "Playback speed",
        trailingContent = ContextMenuChild.ListItem.TrailingContent.Text(value = "1x"),
    ),
    ContextMenuChild.VerticalSpacer,
)
