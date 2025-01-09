package com.google.youtube.components.sections

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.google.youtube.components.widgets.AssetSvg
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Styles
import com.google.youtube.utils.onMouseEvent
import com.google.youtube.utils.removeMouseEventListeners
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.dom.disposableRef
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.role
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.components.layout.VerticalDivider
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Composable
fun NavRail(
    modifier: Modifier = Modifier,
    selectedParentChildIndicesState: MutableState<Pair<Int?, Int?>>,
    isExpandedState: State<Boolean>,
    items: List<NavRailItem> = SampleNavRailItems,
) {
    Column(modifier = modifier) {
        items.forEachIndexed { parentIndex, parentItem ->
            val isDropDownOpen = remember { mutableStateOf(false) }

            Column(modifier = Modifier.fillMaxWidth()) {
                NavRailListItem(
                    item = parentItem,
                    isSelectedState = remember {
                        derivedStateOf {
                            selectedParentChildIndicesState.value.first == parentIndex &&
                                    selectedParentChildIndicesState.value.second == null
                        }
                    },
                    isHorizontallyExpandedState = isExpandedState,
                    isDropDownOpen = isDropDownOpen,
                    hasChildren = parentItem.children?.isNotEmpty() == true,
                    isParentItem = true,
                    onClick = { selectedParentChildIndicesState.value = parentIndex to null }
                )

                parentItem.children?.let { children ->
                    AnimatedVisibility(
                        isVisible = isDropDownOpen.value && isExpandedState.value,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            children.forEachIndexed { childIndex, childItem ->
                                NavRailListItem(
                                    item = childItem,
                                    isSelectedState = remember {
                                        derivedStateOf {
                                            selectedParentChildIndicesState.value.first == parentIndex &&
                                                    selectedParentChildIndicesState.value.second == childIndex
                                        }
                                    },
                                    isHorizontallyExpandedState = isExpandedState,
                                    hasChildren = false,
                                    isParentItem = false,
                                    onClick = {
                                        selectedParentChildIndicesState.value =
                                            parentIndex to childIndex
                                    }
                                )
                            }
                        }
                    }
                }

                if (parentItem.hasBottomDivider) {
                    HorizontalDivider(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(100.percent - 20.px)
                            .backgroundColor(Styles.WHITE)
                            .opacity(0.1f)
                    )
                }
            }
        }
    }
}

@Immutable
data class NavRailItem(
    val label: String,
    val iconType: NavRailListItemIconType? = null,
    val count: Int = 0,
    val children: List<NavRailItem>? = null,
    val hasBottomDivider: Boolean = false,
)

sealed class NavRailListItemIconType {
    @Immutable
    data class ToggleableIcons(
        val inactiveIconPath: String,
        val activeIconPath: String,
    ) : NavRailListItemIconType()

    @Immutable
    data class Image(val ref: String) : NavRailListItemIconType()
}

@Composable
private fun NavRailListItem(
    item: NavRailItem,
    isSelectedState: State<Boolean>,
    isHorizontallyExpandedState: State<Boolean>,
    isDropDownOpen: MutableState<Boolean>? = null,
    hasChildren: Boolean,
    isParentItem: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val uniqueId = remember(item.label) {
        "${item.label.replace(' ', '_')}_nav_rail_button"
    }
    var bgColor by remember { mutableStateOf<Color?>(null) }
    var dropDownBGColor by remember { mutableStateOf(Color.Transparent) }

    // Animated States
    val animatedBGColor by animateColorAsState(
        bgColor
            ?: if (isSelectedState.value) Styles.BACKGROUND_SELECTED.toComposeColor()
            else Color.Transparent
    )
    val animatedDropDownBGColor by animateColorAsState(dropDownBGColor)
    val animatedIconOpacity by animateFloatAsState(if (isSelectedState.value) 1f else 0f)
    val animatedDropDownRotation by animateFloatAsState(
        if (isDropDownOpen?.value == true) 180f else 0f
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            ref = disposableRef { element ->
                val mouseCallbacks = element.onMouseEvent(
                    onHoveredAndPressed = { bgColor = Styles.PRESS_HIGHLIGHT.toComposeColor() },
                    onHovered = { bgColor = Styles.HOVER_HIGHLIGHT.toComposeColor() },
                    onReleased = { bgColor = null },
                )
                onDispose { element.removeMouseEventListeners(mouseCallbacks) }
            },
            modifier = Modifier
                .weight(1f)
                .background(animatedBGColor.toKobwebColor())
                .color(Styles.OFF_WHITE)
                .cursor(Cursor.Pointer)
                .clip(Rect(10.px))
                .padding(12.px)
                .onClick { onClick() }
                .role("button")
                .userSelect(UserSelect.None),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icon / Image
            Box {
                when (item.iconType) {
                    is NavRailListItemIconType.Image -> Image(
                        modifier = Modifier
                            .margin(right = if (isParentItem) 0.px else 20.px)
                            .clip(Circle()),
                        src = item.iconType.ref,
                        width = 24,
                        height = 24,
                    )

                    is NavRailListItemIconType.ToggleableIcons -> {
                        val primaryColor by animateColorAsState(
                            if (isSelectedState.value) Styles.RED.toComposeColor()
                            else Styles.WHITE.toComposeColor()
                        )
                        val secondaryColor by animateColorAsState(
                            if (isSelectedState.value) Styles.PINK.toComposeColor()
                            else Styles.WHITE.toComposeColor()
                        )

                        AssetSvg(
                            id = uniqueId,
                            path = item.iconType.inactiveIconPath,
                            primaryColor = primaryColor.toKobwebColor(),
                            secondaryColor = secondaryColor.toKobwebColor(),
                        ) {
                            opacity(1 - animatedIconOpacity)
                        }
                        AssetSvg(
                            id = uniqueId + "_SELECTED",
                            path = item.iconType.activeIconPath,
                            primaryColor = primaryColor.toKobwebColor(),
                            secondaryColor = secondaryColor.toKobwebColor(),
                        ) {
                            opacity(animatedIconOpacity)
                        }
                    }

                    null -> Unit
                }
            }

            // Label and Count Badge
            AnimatedVisibility(
                modifier = Modifier.weight(1),
                isVisible = isHorizontallyExpandedState.value,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .margin(left = if (isParentItem) 20.px else 0.px)
                        .fontSize(if (isParentItem) 16.px else 15.px)
                        .fontWeight(if (isParentItem) FontWeight.Medium else FontWeight.Normal)
                        .overflow(Overflow.Hidden)
                        .whiteSpace(WhiteSpace.NoWrap),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Label
                    Box(modifier = Modifier.margin(right = 8.px).weight(1)) { Text(item.label) }

                    // Count badge
                    if (item.count > 0) {
                        Box(
                            modifier = Modifier
                                .padding(leftRight = 8.px, topBottom = 3.6.px)
                                .background(Styles.SUBSCRIPTIONS_COUNT_BADGE_CONTAINER)
                                .clip(Circle())
                        ) {
                            Text(item.count.toString())
                        }
                    }
                }
            }
        }

        if (isHorizontallyExpandedState.value && hasChildren) {
            VerticalDivider(
                modifier = Modifier
                    .height(28.px)
                    .margin(leftRight = 6.px)
                    .backgroundColor(Styles.WHITE)
                    .opacity(0.1f),
            )

            // Drop Down button
            Box(
                ref = disposableRef { element ->
                    val mouseCallbacks = element.onMouseEvent(
                        onHoveredAndPressed = {
                            dropDownBGColor = Styles.PRESS_HIGHLIGHT.toComposeColor()
                        },
                        onHovered = { dropDownBGColor = Styles.HOVER_HIGHLIGHT.toComposeColor() },
                        onReleased = { dropDownBGColor = Color.Transparent }
                    )
                    onDispose { element.removeMouseEventListeners(mouseCallbacks) }
                },
                modifier = Modifier
                    .background(animatedDropDownBGColor.toKobwebColor())
                    .clip(Rect(10.px))
                    .size(46.px)
                    .padding(10.px)
                    .onClick { isDropDownOpen?.let { state -> state.value = !state.value } }
                    .cursor(Cursor.Pointer)
            ) {
                Image(
                    modifier = Modifier
                        .rotate(animatedDropDownRotation.deg)
                        .userSelect(UserSelect.None),
                    src = Assets.Icons.ARROW_DOWN
                )
            }
        }
    }
}

private val SampleNavRailItems = listOf(
    NavRailItem(
        label = "Home",
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Assets.Paths.HOME,
            activeIconPath = Assets.Paths.HOME_SELECTED
        )
    ),
    NavRailItem(
        label = "Explore",
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Assets.Paths.EXPLORE,
            activeIconPath = Assets.Paths.EXPLORE_SELECTED
        )
    ),
    NavRailItem(
        label = "Shorts",
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Assets.Paths.SHORTS,
            activeIconPath = Assets.Paths.SHORTS_SELECTED
        )
    ),
    NavRailItem(
        label = "TV Mode",
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Assets.Paths.TV,
            activeIconPath = Assets.Paths.TV_SELECTED
        ),
        hasBottomDivider = true,
    ),
    NavRailItem(
        label = "History",
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Assets.Paths.HISTORY,
            activeIconPath = Assets.Paths.HISTORY_SELECTED
        )
    ),
    NavRailItem(
        label = "Watch Later",
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Assets.Paths.WATCH_LATER,
            activeIconPath = Assets.Paths.WATCH_LATER_SELECTED
        )
    ),
    NavRailItem(
        label = "Liked Videos",
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Assets.Paths.LIKED_VIDEOS,
            activeIconPath = Assets.Paths.LIKED_VIDEOS_SELECTED
        )
    ),
    NavRailItem(
        label = "Playlists",
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Assets.Paths.PLAYLISTS,
            activeIconPath = Assets.Paths.PLAYLISTS_SELECTED,
        ),
        hasBottomDivider = true,
        children = listOf(
            NavRailItem(label = "Cool Stuff"),
            NavRailItem(label = "Redesigns"),
            NavRailItem(label = "Artistic")
        ),
    ),
    NavRailItem(
        label = "Collections",
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Assets.Paths.COLLECTIONS,
            activeIconPath = Assets.Paths.COLLECTIONS_SELECTED,
        ),
        children = listOf(
            NavRailItem(label = "Tech Reviews and Unboxings"),
            NavRailItem(label = "DIY & Crafting"),
            NavRailItem(label = "Gaming"),
            NavRailItem(label = "Cooking & Recipes")
        ),
    ),
    NavRailItem(
        label = "Subscriptions",
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Assets.Paths.SUBSCRIPTIONS,
            activeIconPath = Assets.Paths.SUBSCRIPTIONS_SELECTED,
        ),
        hasBottomDivider = true,
        children = listOf(
            NavRailItem(
                label = "Lofi Girl",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_LOFI_GIRL),
                count = 2
            ),
            NavRailItem(
                label = "Ninja",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_NINJA),
                count = 3
            ),
            NavRailItem(
                label = "TechAltar",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_TECH_ALTAIR)
            ),
            NavRailItem(
                label = "The Human Spider",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_THE_HUMAN_SPIDER)
            ),
            NavRailItem(
                label = "FaceDev",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_FACE_DEV)
            ),
            NavRailItem(
                label = "jacksepticeye",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_JACKSCEPTICEYE),
                count = 1
            ),
            NavRailItem(
                label = "jacksfilms",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_JACKSFILMS)
            ),
            NavRailItem(
                label = "Screen Junkies",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_SCREEN_JUNKIES),
                count = 8
            ),
            NavRailItem(
                label = "Papa Meat",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_PAPA_MEAT),
                count = 1
            ),
            NavRailItem(
                label = "Steam",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_STEAM)
            ),
            NavRailItem(
                label = "The Critical Drinker",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_THE_CRITICAL_DRINKER)
            ),
            NavRailItem(
                label = "Hyperplexed",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_HYPERPLEXED),
                count = 5
            ),
            NavRailItem(
                label = "The Coding Sloth",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_THE_CODING_SLOTH)
            ),
            NavRailItem(
                label = "BOG",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_BOG)
            ),
            NavRailItem(
                label = "Cyberpunk 2077",
                iconType = NavRailListItemIconType.Image(ref = Assets.Avatars.AVATAR_CYBERPUNK_2077),
                count = 7
            ),
        ),
    ),
)
