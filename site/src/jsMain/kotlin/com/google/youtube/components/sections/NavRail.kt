package com.google.youtube.components.sections

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.google.youtube.components.widgets.AssetSvg
import com.google.youtube.components.widgets.ChannelBriefPopupPage
import com.google.youtube.components.widgets.Popup
import com.google.youtube.models.NavRailItemData
import com.google.youtube.models.NavRailListItemIconType
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Asset
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.Route
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.Styles
import com.google.youtube.utils.clickable
import com.google.youtube.utils.limitTextWithEllipsis
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.onMouseEvent
import com.google.youtube.utils.removeMouseEventListeners
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.dom.ElementRefScope
import com.varabyte.kobweb.compose.dom.disposableRef
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundColor
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.color
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.role
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.layout.HorizontalDivider
import com.varabyte.kobweb.silk.components.layout.VerticalDivider
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.opacity
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement

@Composable
fun NavRail(
    modifier: Modifier = Modifier,
    isExpandedState: State<Boolean>,
    items: List<NavRailItemData> = SampleNavRailItems,
) {
    val navigator = LocalNavigator.current
    val currentRoute by navigator.currentRouteState
    var hoveredElementRef by remember { mutableStateOf<Element?>(null) }
    var hoveredRoute by remember { mutableStateOf<Route?>(null) }
    val isPopupContentHoveredState = remember { mutableStateOf(false) }

    SpacedColumn(spacePx = ITEM_SPACING_PX, modifier = modifier) {
        items.forEach { parentItem ->
            val isDropDownOpen = remember { mutableStateOf(false) }

            Column(modifier = Modifier.fillMaxWidth()) {
                NavRailListItem(
                    item = parentItem,
                    isSelectedState = remember {
                        derivedStateOf { parentItem.route == currentRoute }
                    },
                    isHorizontallyExpandedState = isExpandedState,
                    isDropDownOpen = isDropDownOpen,
                    hasChildren = parentItem.children?.isNotEmpty() == true,
                    isParentItem = true,
                    onClick = { navigator.pushRoute(parentItem.route) }
                )

                parentItem.children?.let { children ->
                    AnimatedVisibility(
                        isVisible = isDropDownOpen.value && isExpandedState.value,
                        modifier = Modifier.fillMaxWidth(),
                    ) { isAnimating ->
                        SpacedColumn(
                            spacePx = ITEM_SPACING_PX,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(topBottom = ITEM_SPACING_PX.px),
                        ) {
                            children.forEach { childItem ->
                                var elementRef: Element? by remember { mutableStateOf(null) }
                                NavRailListItem(
                                    ref = ref { r -> elementRef = r },
                                    modifier = Modifier
                                        .onMouseEnter {
                                            if (parentItem.route == Route.Subscriptions && !isAnimating.value) {
                                                hoveredElementRef = elementRef
                                                hoveredRoute = childItem.route
                                            }
                                        },
                                    item = childItem,
                                    isSelectedState = remember {
                                        derivedStateOf { childItem.route == currentRoute }
                                    },
                                    isHorizontallyExpandedState = isExpandedState,
                                    hasChildren = false,
                                    isParentItem = false,
                                    onClick = { navigator.pushRoute(childItem.route) }
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

    Popup(
        modifier = Modifier
            .background(Styles.SURFACE_ELEVATED)
            .clip(Rect(12.px))
            .margin(left = 8.px),
        anchor = hoveredElementRef,
        targetState = hoveredRoute,
        isContentHovered = isPopupContentHoveredState,
    ) { animatedHoveredRoute ->
        if (animatedHoveredRoute is Route.Page) {
            ChannelBriefPopupPage(channelId = animatedHoveredRoute.id)
        }
    }
}

@Composable
private fun NavRailListItem(
    ref: ElementRefScope<HTMLElement>? = null,
    item: NavRailItemData,
    isSelectedState: State<Boolean>,
    isHorizontallyExpandedState: State<Boolean>,
    isDropDownOpen: MutableState<Boolean>? = null,
    hasChildren: Boolean,
    isParentItem: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val uniqueId = remember(item.label) { "${item.label}_nav_rail_button" }
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
        ref = ref,
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
                .background(animatedBGColor.toKobwebColor())
                .borderRadius(10.px)
                .clickable(onClick = onClick)
                .clip(Rect(10.px))
                .color(Styles.OFF_WHITE)
                .padding(12.px)
                .role("button")
                .userSelect(UserSelect.None)
                .weight(1f),
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

                    else -> Unit
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
                        .margin(left = if (isParentItem) 40.px else 0.px)
                        .fontSize(if (isParentItem) 16.px else 15.px)
                        .fontWeight(if (isParentItem) FontWeight.Medium else FontWeight.Normal),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Label
                    Box(
                        modifier = Modifier
                            .display(DisplayStyle.Block)
                            .weight(1)
                            .padding(right = 24.px)
                            .limitTextWithEllipsis()
                    ) { Text(item.label) }

                    // Count badge
                    if (item.count > 0) {
                        Box(
                            modifier = Modifier
                                .padding(leftRight = 8.px, topBottom = 3.6.px)
                                .background(Styles.SUBSCRIPTIONS_COUNT_BADGE_CONTAINER)
                                .clip(Circle())
                                .noShrink()
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
                    .borderRadius(10.px)
                    .clickable { isDropDownOpen?.let { state -> state.value = !state.value } }
                    .clip(Rect(10.px))
                    .padding(10.px)
                    .size(46.px)
            ) {
                Image(
                    modifier = Modifier
                        .rotate(animatedDropDownRotation.deg)
                        .userSelect(UserSelect.None),
                    src = Asset.Icon.ARROW_DOWN
                )
            }
        }
    }
}

private val SampleNavRailItems = listOf(
    NavRailItemData(
        label = "Home",
        route = Route.Home,
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Asset.Path.HOME,
            activeIconPath = Asset.Path.HOME_SELECTED
        )
    ),
    NavRailItemData(
        label = "Explore",
        route = Route.Explore,
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Asset.Path.EXPLORE,
            activeIconPath = Asset.Path.EXPLORE_SELECTED
        )
    ),
    NavRailItemData(
        label = "Shorts",
        route = Route.Shorts,
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Asset.Path.SHORTS,
            activeIconPath = Asset.Path.SHORTS_SELECTED
        )
    ),
    NavRailItemData(
        label = "TV Mode",
        route = Route.TVMode,
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Asset.Path.TV,
            activeIconPath = Asset.Path.TV_SELECTED
        ),
        hasBottomDivider = true,
    ),
    NavRailItemData(
        label = "History",
        route = Route.History,
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Asset.Path.HISTORY,
            activeIconPath = Asset.Path.HISTORY_SELECTED
        )
    ),
    NavRailItemData(
        label = "Watch Later",
        route = Route.WatchLater,
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Asset.Path.WATCH_LATER,
            activeIconPath = Asset.Path.WATCH_LATER_SELECTED
        )
    ),
    NavRailItemData(
        label = "Liked Videos",
        route = Route.LikedVideos,
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Asset.Path.LIKED_VIDEOS,
            activeIconPath = Asset.Path.LIKED_VIDEOS_SELECTED
        )
    ),
    NavRailItemData(
        label = "Playlists",
        route = Route.Playlists,
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Asset.Path.PLAYLISTS,
            activeIconPath = Asset.Path.PLAYLISTS_SELECTED,
        ),
        hasBottomDivider = true,
        children = listOf(
            NavRailItemData(label = "Cool Stuff", route = Route.Playlist(id = "cool_stuff")),
            NavRailItemData(label = "Redesigns", route = Route.Playlist(id = "redesigns")),
            NavRailItemData(label = "Artistic", route = Route.Playlist(id = "artistic"))
        ),
    ),
    NavRailItemData(
        label = "Collections",
        route = Route.Collections,
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Asset.Path.COLLECTIONS,
            activeIconPath = Asset.Path.COLLECTIONS_SELECTED,
        ),
        children = listOf(
            NavRailItemData(
                label = "Tech Reviews and Unboxings",
                route = Route.Collection(id = "tech_reviews_and_unboxings"),
            ),
            NavRailItemData(
                label = "DIY & Crafting",
                route = Route.Collection(id = "diy_and_crafting"),
            ),
            NavRailItemData(label = "Gaming", route = Route.Collection(id = "gaming")),
            NavRailItemData(
                label = "Cooking & Recipes",
                route = Route.Collection(id = "cooking_and_recipes"),
            )
        ),
    ),
    NavRailItemData(
        label = "Subscriptions",
        route = Route.Subscriptions,
        iconType = NavRailListItemIconType.ToggleableIcons(
            inactiveIconPath = Asset.Path.SUBSCRIPTIONS,
            activeIconPath = Asset.Path.SUBSCRIPTIONS_SELECTED,
        ),
        hasBottomDivider = true,
        children = listOf(
            NavRailItemData(
                label = "Lofi Girl",
                route = Route.Page(id = "lofigirl"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.LOFI_GIRL),
                count = 2,
            ),
            NavRailItemData(
                label = "Ninja",
                route = Route.Page(id = "ninja"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.NINJA),
                count = 3,
            ),
            NavRailItemData(
                label = "TechAltar",
                route = Route.Page(id = "techaltar"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.TECH_ALTAR)
            ),
            NavRailItemData(
                label = "The Human Spider",
                route = Route.Page(id = "thehumanspider"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.THE_HUMAN_SPIDER)
            ),
            NavRailItemData(
                label = "FaceDev",
                route = Route.Page(id = "facedev"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.FACE_DEV)
            ),
            NavRailItemData(
                label = "jacksepticeye",
                route = Route.Page(id = "jacksepticeye"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.JACKSEPTICEYE),
                count = 1,
            ),
            NavRailItemData(
                label = "jacksfilms",
                route = Route.Page(id = "jacksfilms"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.JACKSFILMS)
            ),
            NavRailItemData(
                label = "Screen Junkies",
                route = Route.Page(id = "screenjunkies"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.SCREEN_JUNKIES),
                count = 8,
            ),
            NavRailItemData(
                label = "Papa Meat",
                route = Route.Page(id = "papameat"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.PAPA_MEAT),
                count = 1,
            ),
            NavRailItemData(
                label = "Steam",
                route = Route.Page(id = "steam"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.STEAM)
            ),
            NavRailItemData(
                label = "The Critical Drinker",
                route = Route.Page(id = "thecriticaldrinker"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.THE_CRITICAL_DRINKER)
            ),
            NavRailItemData(
                label = "Hyperplexed",
                route = Route.Page(id = "hyperplexed"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.HYPERPLEXED),
                count = 5,
            ),
            NavRailItemData(
                label = "The Coding Sloth",
                route = Route.Page(id = "thecodingsloth"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.THE_CODING_SLOTH)
            ),
            NavRailItemData(
                label = "BOG",
                route = Route.Page(id = "bog"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.BOG)
            ),
            NavRailItemData(
                label = "Cyberpunk 2077",
                route = Route.Page(id = "cyberpunk2077"),
                iconType = NavRailListItemIconType.Image(ref = Asset.Channel.CYBERPUNK_2077),
                count = 7,
            ),
        ),
    ),
)

private const val ITEM_SPACING_PX = 2
