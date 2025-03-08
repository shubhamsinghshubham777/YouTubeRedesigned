package com.google.youtube.components.widgets.context

import com.google.youtube.components.widgets.ContextMenuChild
import com.google.youtube.utils.Asset

val ContextMenuPageSubscription = listOf(
    ContextMenuChild.ListItem(
        text = "Posts",
        leadingContent = ContextMenuChild.ListItem.LeadingContent.Check(isChecked = false),
        onClick = {}
    ),
    ContextMenuChild.ListItem(
        text = "Videos",
        leadingContent = ContextMenuChild.ListItem.LeadingContent.Check(isChecked = true),
    ),
    ContextMenuChild.ListItem(
        text = "Live",
        leadingContent = ContextMenuChild.ListItem.LeadingContent.Check(isChecked = true),
    ),
    ContextMenuChild.VerticalSpacer,
    ContextMenuChild.HorizontalDivider,
    ContextMenuChild.VerticalSpacer,
    ContextMenuChild.ListItem(
        text = "Collection",
        leadingContent = ContextMenuChild.ListItem.LeadingContent.Asset(Asset.Path.COLLECTIONS),
        trailingContent = ContextMenuChild.ListItem.TrailingContent.Arrow,
    ),
    ContextMenuChild.ListItem(
        text = "Unsubscribe",
        leadingContent = ContextMenuChild.ListItem.LeadingContent.Asset(Asset.Path.UNSUBSCRIBE),
    ),
)
