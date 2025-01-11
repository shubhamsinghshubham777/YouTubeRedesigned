package com.google.youtube.components.widgets.context_menu

import com.google.youtube.utils.Assets
import com.google.youtube.components.widgets.ContextMenuChild

val ContextMenuPageVideoOptions = listOf(
    ContextMenuChild.ListItem(
        text = "More Options",
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Asset(
                path = Assets.Paths.FILTER,
                hasHalfOpacity = true
            ),
        trailingContent = ContextMenuChild.ListItem
            .TrailingContent
            .Arrow,
        isTopItem = true,
        onClick = {}
    ),
    ContextMenuChild.HorizontalDivider,
    ContextMenuChild.VerticalSpacer,
    ContextMenuChild.ListItem(
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Asset(
                path = Assets.Paths.SLEEP_TIMER,
                hasHalfOpacity = true
            ),
        text = "Sleep Timer",
        trailingContent = ContextMenuChild.ListItem
            .TrailingContent
            .Text(value = "OFF"),
    ),
    ContextMenuChild.ListItem(
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Asset(
                path = Assets.Paths.CC,
                hasHalfOpacity = true
            ),
        text = "Subtitles",
        trailingContent = ContextMenuChild.ListItem
            .TrailingContent
            .Text(value = "OFF"),
    ),
    ContextMenuChild.ListItem(
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Asset(
                path = Assets.Paths.SPEED,
                hasHalfOpacity = true
            ),
        text = "Playback speed",
        trailingContent = ContextMenuChild.ListItem
            .TrailingContent
            .Text(value = "1x"),
    ),
    ContextMenuChild.VerticalSpacer,
)
