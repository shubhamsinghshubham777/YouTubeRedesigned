package com.google.youtube.pages

import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.CategoryTab
import com.google.youtube.components.widgets.RowScrollButtons
import com.google.youtube.components.widgets.VerticalDivider
import com.google.youtube.components.widgets.collections.CollectionsSubPage
import com.google.youtube.components.widgets.subscriptions.SubscriptionsSubPage
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.HorizontalScrollState
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.Route
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.bindScrollState
import com.google.youtube.utils.hideScrollBar
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element

@Composable
fun SubscriptionsPage(isSubscriptionsCategorySelected: Boolean) {
    val updatedIsSubscriptionsCategorySelected =
        updateTransition(isSubscriptionsCategorySelected).currentState

    Column(modifier = Modifier.fillMaxWidth()) {
        Tabs(updatedIsSubscriptionsCategorySelected)
        // Divider
        Box(modifier = Modifier.background(Styles.DIVIDER).fillMaxWidth().height(1.px))
        Crossfade(
            targetState = updatedIsSubscriptionsCategorySelected,
            modifier = Modifier.fillMaxWidth(),
        ) { animatedIsSubscriptionsCategorySelected ->
            if (animatedIsSubscriptionsCategorySelected) SubscriptionsSubPage()
            else CollectionsSubPage()
        }
    }
}

// TODO: Use this throughout the codebase
@Composable
fun ScrollableSpacedRow(
    spacePx: Int = 15,
    modifier: Modifier = Modifier,
    showScrollButtons: Boolean = false,
    content: @Composable RowScope.() -> Unit,
) {
    var rowRef by remember { mutableStateOf<Element?>(null) }
    val horizontalScrollState = remember { mutableStateOf(HorizontalScrollState.ReachedStart) }
    Box(modifier = Modifier.fillMaxWidth().then(modifier)) {
        SpacedRow(
            ref = ref { e -> rowRef = e },
            spacePx = spacePx,
            centerContentVertically = false,
            modifier = Modifier
                .bindScrollState(horizontalScrollState)
                .fillMaxWidth()
                .hideScrollBar()
                .padding(top = 24.px)
                .overflow { x(Overflow.Scroll) }
                .scrollBehavior(ScrollBehavior.Smooth),
            content = content,
        )
        if (showScrollButtons) {
            RowScrollButtons(
                elementToControl = rowRef,
                horizontalScrollState = horizontalScrollState,
                containerPadding = 0.px,
                scrollPixels = 356.0,
                gradientColor = Styles.SURFACE,
            )
        }
    }
}

@Composable
private fun Tabs(isSubscriptionsCategorySelected: Boolean) {
    val navigator = LocalNavigator.current
    SpacedRow(
        spacePx = 16,
        modifier = Modifier.fillMaxWidth().overflow { x(Overflow.Scroll) }.hideScrollBar(),
    ) {
        CategoryTab(
            isDense = false,
            isSelected = isSubscriptionsCategorySelected,
            label = "Subscriptions",
            onClick = { navigator.pushRoute(Route.Subscriptions) },
            secondaryLabel = "126",
        )
        // Divider
        VerticalDivider()
        CategoryTab(
            isDense = false,
            isSelected = !isSubscriptionsCategorySelected,
            label = "Collections",
            onClick = { navigator.pushRoute(Route.Collections) },
            secondaryLabel = "3",
        )
    }
}
