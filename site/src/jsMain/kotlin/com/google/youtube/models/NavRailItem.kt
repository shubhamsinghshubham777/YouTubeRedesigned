package com.google.youtube.models

import androidx.compose.runtime.Immutable

@Immutable
data class NavRailItem(
    val label: String,
    val iconType: NavRailListItemIconType? = null,
    val count: Int = 0,
    val children: List<NavRailItem>? = null,
    val hasBottomDivider: Boolean = false,
) {
    enum class ParentElement(val label: String) {
        HOME("Home"),
        EXPLORE("Explore"),
        SHORTS("Shorts"),
        TV_MODE("TV Mode"),
        HISTORY("History"),
        WATCH_LATER("Watch Later"),
        LIKED_VIDEOS("Liked Videos"),
        PLAYLISTS("Playlists"),
        COLLECTIONS("Collections"),
        SUBSCRIPTIONS("Subscriptions");

        companion object {
            fun find(value: String): ParentElement? {
                return entries.firstOrNull { it.label == value }
            }
        }
    }
}
