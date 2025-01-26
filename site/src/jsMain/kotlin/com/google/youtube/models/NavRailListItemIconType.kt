package com.google.youtube.models

import androidx.compose.runtime.Immutable

sealed class NavRailListItemIconType {
    @Immutable
    data class ToggleableIcons(
        val inactiveIconPath: String,
        val activeIconPath: String,
    ) : NavRailListItemIconType()

    @Immutable
    data class Image(val ref: String) : NavRailListItemIconType()
}
