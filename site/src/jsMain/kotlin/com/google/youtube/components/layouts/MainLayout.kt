package com.google.youtube.components.layouts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.components.sections.NavRail
import com.google.youtube.components.sections.TopBar
import com.google.youtube.utils.isGreaterThan
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.px

@Composable
fun MainLayout() {
    val breakpoint = rememberBreakpoint()
    val isLargeBreakpoint by remember(breakpoint) {
        derivedStateOf { breakpoint isGreaterThan Breakpoint.SM }
    }
    val horizontalPaddingPx by animateFloatAsState(if (isLargeBreakpoint) 24f else 12f)
    val selectedParentChildIndicesState = remember {
        mutableStateOf<Pair<Int?, Int?>>(null to null)
    }
    val isNavRailExpandedState = remember(isLargeBreakpoint) { mutableStateOf(isLargeBreakpoint) }
    val navRailWidthPx by animateFloatAsState(if (isNavRailExpandedState.value) 250f else 50f)

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(
            modifier = Modifier.padding(leftRight = horizontalPaddingPx.px),
            onDrawerButtonClick = { isNavRailExpandedState.value = !isNavRailExpandedState.value }
        )
        Row(modifier = Modifier.fillMaxSize()) {
            NavRail(
                modifier = Modifier
                    .width(navRailWidthPx.px)
                    .margin(leftRight = horizontalPaddingPx.px),
                selectedParentChildIndicesState = selectedParentChildIndicesState,
                isExpandedState = isNavRailExpandedState,
            )
            Box(modifier = Modifier.weight(1).fillMaxHeight()) {
            }
        }
    }
}
