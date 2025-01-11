package com.google.youtube.components.widgets.context_menu

import com.google.youtube.utils.Assets
import com.google.youtube.components.widgets.ContextMenuChild

val ContextMenuPageSubscription = listOf(
    ContextMenuChild.ListItem(
        text = "Posts",
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Check(isChecked = false),
        onClick = {}
    ),
    ContextMenuChild.ListItem(
        text = "Videos",
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Check(isChecked = true),
    ),
    ContextMenuChild.ListItem(
        text = "Live",
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Check(isChecked = true),
    ),
    ContextMenuChild.VerticalSpacer,
    ContextMenuChild.HorizontalDivider,
    ContextMenuChild.VerticalSpacer,
    ContextMenuChild.ListItem(
        text = "Collection",
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Asset(Assets.Paths.COLLECTIONS),
        trailingContent = ContextMenuChild.ListItem
            .TrailingContent
            .Arrow,
    ),
    ContextMenuChild.ListItem(
        text = "Unsubscribe",
        leadingContent = ContextMenuChild.ListItem
            .LeadingContent
            .Asset(Assets.Paths.UNSUBSCRIBE),
    ),
)
