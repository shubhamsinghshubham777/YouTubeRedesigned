package com.google.youtube.models

import androidx.compose.runtime.Immutable
import com.google.youtube.utils.Route

@Immutable
data class NavRailItemData(
    val label: String,
    val route: Route,
    val iconType: NavRailListItemIconType? = null,
    val count: Int = 0,
    val children: List<NavRailItemData>? = null,
    val hasBottomDivider: Boolean = false,
)
