package com.google.youtube.components.layouts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.components.sections.NavRail
import com.google.youtube.components.sections.TopBar
import com.google.youtube.pages.HomePage
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.Dialog
import com.google.youtube.utils.Styles
import com.google.youtube.utils.isGreaterThan
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text

@Composable
fun MainLayout() {
    val breakpoint = rememberBreakpoint()
    val isLargeBreakpoint by remember(breakpoint) {
        derivedStateOf { breakpoint isGreaterThan Breakpoint.SM }
    }
    val horizontalPaddingPx by animateFloatAsState(if (isLargeBreakpoint) 24f else 12f)
    val selectedParentChildIndicesState = remember {
        mutableStateOf<Pair<Int?, Int?>>(0 to null)
    }
    val isNavRailExpandedState = remember(isLargeBreakpoint) { mutableStateOf(isLargeBreakpoint) }
    val navRailWidthPx by animateFloatAsState(if (isNavRailExpandedState.value) 250f else 50f)
    val showPersonalisedFeedDialogState = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(
                modifier = Modifier.padding(leftRight = horizontalPaddingPx.px),
                onDrawerButtonClick = {
                    isNavRailExpandedState.value = !isNavRailExpandedState.value
                }
            )
            Row(modifier = Modifier.fillMaxSize()) {
                NavRail(
                    modifier = Modifier
                        .width(navRailWidthPx.px)
                        .margin(leftRight = horizontalPaddingPx.px),
                    selectedParentChildIndicesState = selectedParentChildIndicesState,
                    isExpandedState = isNavRailExpandedState,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(100.percent - navRailWidthPx.px - horizontalPaddingPx.px)
                        .fillMaxHeight()
                        .overflow(Overflow.Scroll)
                ) {
                    Box(modifier = Modifier.fillMaxSize().minWidth(320.px)) {
                        Crossfade(selectedParentChildIndicesState.value) { animatedState ->
                            when (animatedState.first) {
                                0 -> HomePage(
                                    showPersonalisedFeedDialogState = showPersonalisedFeedDialogState
                                )

                                1 -> Text("Explore")
                                2 -> Text("Shorts")
                                3 -> Text("TV Mode")
                                4 -> Text("History")
                                5 -> Text("Watch Later")
                                6 -> Text("Liked Videos")

                                7 -> when (animatedState.second) {
                                    0 -> Text("Cool Stuff")
                                    1 -> Text("Redesigns")
                                    2 -> Text("Artistic")
                                    else -> Text("Playlists")
                                }

                                8 -> when (animatedState.second) {
                                    0 -> Text("Tech Reviews and Unboxings")
                                    1 -> Text("DIY & Crafting")
                                    2 -> Text("Gaming")
                                    3 -> Text("Cooking & Recipes")
                                    else -> Text("Collections")
                                }

                                else -> Text(
                                    "Subscriptions" + (animatedState.second?.toString() ?: "")
                                )
                            }
                        }
                    }
                }
            }
        }

        Dialog(
            modifier = Modifier.zIndex(1),
            isDisplayed = showPersonalisedFeedDialogState.value,
            onDismissed = { showPersonalisedFeedDialogState.value = false }
        ) {
            Box(
                modifier = Modifier
                    .background(Styles.SURFACE_ELEVATED)
                    .clip(Rect(12.px))
                    .padding(32.px),
                contentAlignment = Alignment.Center,
            ) { Text("Personalised Feed Dialog [WIP]") }
        }
    }
}
