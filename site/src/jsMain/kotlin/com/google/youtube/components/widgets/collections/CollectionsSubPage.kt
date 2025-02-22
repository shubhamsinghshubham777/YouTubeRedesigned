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
import com.google.youtube.components.widgets.VerticalDivider
import com.google.youtube.components.widgets.Wrap
import com.google.youtube.components.widgets.context_menu.RoundedSearchTextField
import com.google.youtube.components.widgets.context_menu.TextField
import com.google.youtube.models.CollectionChannelItem
import com.google.youtube.models.CollectionData
import com.google.youtube.pages.ScrollableSpacedRow
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Assets
import com.google.youtube.utils.PaddingValues
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberIsSmallBreakpoint
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.fontWeight
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.silk.components.graphics.Image
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
    val sampleData = remember {
        List(3) {
            CollectionData(
                name = "Gaming",
                channelItems = List(8) { index ->
                    CollectionChannelItem(
                        channelId = index.toString(),
                        avatarAsset = Assets.Icons.USER_AVATAR,
                        channelName = "Ninja",
                        isVerified = true,
                        subscribersCount = "23.8M",
                        isSubscribed = true,
                    )
                }
            )
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(top = 40.px)) {
        Filters()
        SpacedColumn(spacePx = 35, modifier = Modifier.fillMaxWidth().padding(topBottom = 47.px)) {
            sampleData.forEachIndexed { index, data ->
                var isContentVisible by remember { mutableStateOf(index == 0) }
                val animatedArrowRotation by animateFloatAsState(if (isContentVisible) 180f else 0f)
                val nameState = remember { mutableStateOf(data.name) }
                val isEditable = remember { mutableStateOf(false) }
                var textFieldRef by remember { mutableStateOf<HTMLElement?>(null) }
                val coroutineScope = rememberCoroutineScope()

                Column(modifier = Modifier.fillMaxWidth()) {
                    Wrap(modifier = Modifier.fillMaxWidth(), verticalGapPx = 8) {
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
                                asset = if (isEditable.value) Assets.Icons.CHECK
                                else Assets.Icons.EDIT,
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
                                startIconPath = Assets.Paths.ADD_SOLO,
                                text = "Add Channel",
                            )
                            AssetSvgButton(
                                id = "view_collection_button_for_${data.name}",
                                isDense = true,
                                onClick = {},
                                text = "View Collection",
                            )
                        }
                        AssetImageButton(
                            asset = Assets.Icons.ARROW_DOWN,
                            modifier = Modifier
                                .margin(left = 16.px)
                                .rotate(animatedArrowRotation.deg),
                            onClick = { isContentVisible = !isContentVisible },
                        )
                    }

                    AnimatedVisibility(
                        isVisible = isContentVisible,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        ScrollableSpacedRow(
                            spacePx = 15,
                            showScrollButtons = true,
                            modifier = Modifier.padding(top = 16.px),
                        ) {
                            data.channelItems.forEach { item ->
                                SpacedColumn(
                                    spacePx = 20,
                                    modifier = Modifier.noShrink().padding(leftRight = 10.px),
                                    centerContentHorizontally = true,
                                ) {
                                    SpacedColumn(spacePx = 16, centerContentHorizontally = true) {
                                        Image(src = item.avatarAsset, width = 46, height = 46)
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            SpacedRow(8) {
                                                TextBox(
                                                    text = item.channelName,
                                                    size = 18,
                                                    maxLines = 1,
                                                    lineHeight = 28.3,
                                                    weight = FontWeight.Medium,
                                                )
                                                Image(
                                                    src = Assets.Icons.VERIFIED_BADGE,
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
                                    // TODO: Replace with real subscribe button
                                    AssetSvgButton(
                                        id = "subscribe_button",
                                        startIconPath = Assets.Paths.NOTIFS_SELECTED,
                                        text = "Subscribed",
                                        onClick = {},
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
private fun Filters() {
    val isSmallBreakpoint by rememberIsSmallBreakpoint()
    val searchQueryState = remember { mutableStateOf("") }
    var isGridLayoutSelected by remember { mutableStateOf(true) }

    Wrap(horizontalGapPx = 16, modifier = Modifier.fillMaxWidth()) {
        AssetSvgButton(
            endIconPath = Assets.Paths.ARROW_DOWN,
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
            startIconPath = Assets.Paths.GRID,
        )
        AssetSvgButton(
            id = "layout_type_list_button",
            isDense = true,
            isSelected = !isGridLayoutSelected,
            onClick = { isGridLayoutSelected = false },
            type = AssetSvgButtonType.SelectableChip,
            startIconPath = Assets.Paths.LIST,
        )
        if (!isSmallBreakpoint) Spacer()
        AssetSvgButton(
            id = "new_collection_button",
            isDense = true,
            isSelected = false,
            onClick = {},
            startIconPath = Assets.Paths.COLLECTIONS,
            text = "New Collection",
        )
        RoundedSearchTextField(textState = searchQueryState, hintText = "Search subscriptions")
    }
}
