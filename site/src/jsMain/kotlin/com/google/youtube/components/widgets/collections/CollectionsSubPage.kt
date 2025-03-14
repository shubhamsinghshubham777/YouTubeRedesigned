package com.google.youtube.components.widgets.collections

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.components.widgets.SubscribeButton
import com.google.youtube.components.widgets.VerticalDivider
import com.google.youtube.components.widgets.context.RoundedSearchTextField
import com.google.youtube.components.widgets.context.TextField
import com.google.youtube.data.CollectionsDataProvider
import com.google.youtube.pages.ScrollableSpacedRow
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Asset
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.PaddingValues
import com.google.youtube.utils.Route
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.Wrap
import com.google.youtube.utils.clickable
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberIsSmallBreakpoint
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.fontWeight
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.overlay.PopupPlacement
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLElement

@Composable
fun CollectionsSubPage() {
    val navigator = LocalNavigator.current
    val searchQueryState = remember { mutableStateOf("") }
    val collectionsDataProvider = remember { CollectionsDataProvider() }
    val collectionsData = remember { collectionsDataProvider.getCollections() }

    Column(modifier = Modifier.fillMaxWidth().padding(top = 40.px)) {
        Filters(searchQueryState)
        SpacedColumn(spacePx = 35, modifier = Modifier.fillMaxWidth().padding(topBottom = 47.px)) {
            collectionsData.forEachIndexed { parentIndex, data ->
                var isRowExpanded by remember { mutableStateOf(true) }
                val animatedArrowRotation by animateFloatAsState(if (isRowExpanded) 180f else 0f)
                val nameState = remember { mutableStateOf(data.name) }
                val isEditable = remember { mutableStateOf(false) }
                var textFieldRef by remember { mutableStateOf<HTMLElement?>(null) }
                val coroutineScope = rememberCoroutineScope()

                Column(modifier = Modifier.fillMaxWidth()) {
                    Wrap(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isRowExpanded = !isRowExpanded },
                        verticalGapPx = 8,
                    ) {
                        SpacedRow(4) {
                            if (isEditable.value) {
                                TextField(
                                    onElementAvailable = { e -> textFieldRef = e },
                                    textState = nameState,
                                    contentPadding = PaddingValues(0.px),
                                    showBorder = false,
                                    onEnterKeyPressed = {
                                        coroutineScope.launch {
                                            toggleTextField(isEditable, nameState, textFieldRef)
                                        }
                                    },
                                    hintText = "Collection name",
                                ) {
                                    fontSize(18.px)
                                    fontWeight(FontWeight.Medium)
                                    height(20.px)
                                }
                            } else {
                                TextBox(
                                    text = nameState.value,
                                    size = 18,
                                    weight = FontWeight.Medium
                                )
                            }
                            AssetImageButton(
                                asset = if (isEditable.value) Asset.Icon.CHECK
                                else Asset.Icon.EDIT,
                                modifier = Modifier.opacity(0.6f),
                                onClick = {
                                    coroutineScope.launch {
                                        toggleTextField(isEditable, nameState, textFieldRef)
                                    }
                                },
                            )
                        }
                        Spacer()
                        SpacedRow(8) {
                            AssetSvgButton(
                                id = "add_channel_button_for_${data.name}",
                                isDense = true,
                                onClick = {},
                                startIconPath = Asset.Path.ADD_SOLO,
                                text = "Add Channel",
                            )
                            AssetSvgButton(
                                id = "view_collection_button_for_${data.name}",
                                isDense = true,
                                onClick = { navigator.pushRoute(Route.Collection(id = data.id)) },
                                text = "View Collection",
                            )
                        }
                        AssetImageButton(
                            asset = Asset.Icon.ARROW_DOWN,
                            modifier = Modifier
                                .margin(left = 16.px)
                                .rotate(animatedArrowRotation.deg),
                        )
                    }

                    AnimatedVisibility(
                        isVisible = isRowExpanded,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ScrollableSpacedRow(
                            spacePx = 15,
                            showScrollButtons = true,
                            modifier = Modifier.padding(top = 16.px),
                        ) {
                            data.channelItems
                                .filter { item ->
                                    item.channelName.contains(
                                        searchQueryState.value,
                                        ignoreCase = true,
                                    )
                                }
                                .forEach { item ->
                                    var isChannelNameHovered by remember { mutableStateOf(false) }

                                    SpacedColumn(
                                        spacePx = 20,
                                        modifier = Modifier.noShrink().padding(leftRight = 10.px),
                                        centerContentHorizontally = true,
                                    ) {
                                        SpacedColumn(
                                            spacePx = 16,
                                            centerContentHorizontally = true,
                                            modifier = Modifier
                                                .clickable {
                                                    navigator.pushRoute(Route.Page(id = item.channelId))
                                                }
                                                .onMouseEnter { isChannelNameHovered = true }
                                                .onMouseLeave { isChannelNameHovered = false }
                                        ) {
                                            Image(
                                                modifier = Modifier.clip(Circle()),
                                                src = item.avatarAsset,
                                                width = 46,
                                                height = 46,
                                            )
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                SpacedRow(8) {
                                                    TextBox(
                                                        modifier = Modifier
                                                            .thenIf(isChannelNameHovered) {
                                                                Modifier.textDecorationLine(
                                                                    TextDecorationLine.Underline
                                                                )
                                                            },
                                                        text = item.channelName,
                                                        size = 18,
                                                        maxLines = 1,
                                                        lineHeight = 28.3,
                                                        weight = FontWeight.Medium,
                                                    )
                                                    Image(
                                                        src = Asset.Icon.VERIFIED_BADGE,
                                                        width = 15,
                                                        height = 15,
                                                    )
                                                }
                                                TextBox(
                                                    text = "${item.subscribersCount} subscribers",
                                                    lineHeight = 23.1,
                                                    color = Styles.VIDEO_CARD_SECONDARY_TEXT,
                                                )
                                            }
                                        }
                                        SubscribeButton(
                                            initialIsSubscribed = true,
                                            popupPlacement = if (parentIndex == collectionsData.lastIndex) {
                                                PopupPlacement.TopLeft
                                            } else {
                                                PopupPlacement.BottomLeft
                                            },
                                        )
                                    }
                                }
                        }
                    }
                }
            }
        }
    }
}

private suspend fun toggleTextField(
    isEditable: MutableState<Boolean>,
    nameState: MutableState<String>,
    textFieldRef: HTMLElement?,
) = withContext(Dispatchers.Main) {
    if (nameState.value.isNotBlank()) {
        isEditable.value = !isEditable.value
        if (isEditable.value) {
            launch {
                delay(10)
                textFieldRef?.focus()
            }
        }
    }
}

@Composable
private fun Filters(searchQueryState: MutableState<String>) {
    val isSmallBreakpoint by rememberIsSmallBreakpoint()
    var isGridLayoutSelected by remember { mutableStateOf(true) }

    Wrap(horizontalGapPx = 16, modifier = Modifier.fillMaxWidth()) {
        AssetSvgButton(
            endIconPath = Asset.Path.ARROW_DOWN,
            id = "sort_by_button",
            isDense = true,
            onClick = {},
            secondaryText = "Sort by:",
            text = "Relevance",
            type = AssetSvgButtonType.SelectableChip,
        )
        VerticalDivider()
        AssetSvgButton(
            id = "layout_type_grid_button",
            isDense = true,
            isSelected = isGridLayoutSelected,
            onClick = { isGridLayoutSelected = true },
            type = AssetSvgButtonType.SelectableChip,
            startIconPath = Asset.Path.GRID,
        )
        AssetSvgButton(
            id = "layout_type_list_button",
            isDense = true,
            isSelected = !isGridLayoutSelected,
            onClick = { isGridLayoutSelected = false },
            type = AssetSvgButtonType.SelectableChip,
            startIconPath = Asset.Path.LIST,
        )
        if (!isSmallBreakpoint) Spacer()
        AssetSvgButton(
            id = "new_collection_button",
            isDense = true,
            isSelected = false,
            onClick = {},
            startIconPath = Asset.Path.COLLECTIONS,
            text = "New Collection",
        )
        RoundedSearchTextField(textState = searchQueryState, hintText = "Search subscriptions")
    }
}
