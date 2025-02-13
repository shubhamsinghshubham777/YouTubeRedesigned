package com.google.youtube.components.layouts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.components.sections.NavRail
import com.google.youtube.components.sections.TopBar
import com.google.youtube.components.sections.TopBarDefaults
import com.google.youtube.components.widgets.PersonalisedFeedDialog
import com.google.youtube.pages.ExplorePage
import com.google.youtube.pages.HistoryPage
import com.google.youtube.pages.HomePage
import com.google.youtube.pages.SearchPage
import com.google.youtube.pages.ShortDetails
import com.google.youtube.pages.ShortsGrid
import com.google.youtube.pages.TVModePage
import com.google.youtube.pages.VideoPlayerPage
import com.google.youtube.utils.Constants
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.Dialog
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.Route
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.hideScrollBar
import com.google.youtube.utils.isGreaterThan
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.minWidth
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.top
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import kotlinx.browser.window
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun MainLayout() {
    val navigator = LocalNavigator.current
    val currentRoute by navigator.currentRouteState
    val breakpoint = rememberBreakpoint()
    val isLargeBreakpoint by remember(breakpoint) {
        derivedStateOf { breakpoint isGreaterThan Breakpoint.SM }
    }
    val horizontalPaddingState = animateFloatAsState(if (isLargeBreakpoint) 24f else 12f)
    val isNavRailExpandedState = remember { mutableStateOf(false) }
    val navRailWidthPx by animateFloatAsState(if (isNavRailExpandedState.value) 250f else 50f)
    // TODO: Create a composition local for this
    val showPersonalisedFeedDialogState = remember { mutableStateOf(false) }

    // Auto close NavRail on item selection for smaller devices
    LaunchedEffect(currentRoute) {
        if (!isLargeBreakpoint) isNavRailExpandedState.value = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(
                modifier = Modifier
                    .background(Styles.SURFACE)
                    .padding(leftRight = horizontalPaddingState.value.px)
                    .position(Position.Sticky)
                    .top(0.px)
                    .zIndex(1),
                onDrawerButtonClick = {
                    isNavRailExpandedState.value = !isNavRailExpandedState.value
                },
                onLogoClick = { navigator.pushRoute(Route.Home) },
                onSearch = { query -> navigator.pushRoute(Route.Search(query)) },
            )
            Row(modifier = Modifier.fillMaxSize()) {
                NavRail(
                    modifier = Modifier
                        .height(100.vh - TopBarDefaults.HEIGHT)
                        .position(Position.Sticky)
                        .top(TopBarDefaults.HEIGHT)
                        .width(navRailWidthPx.px)
                        .margin(leftRight = horizontalPaddingState.value.px)
                        .overflow(overflowX = Overflow.Hidden, overflowY = Overflow.Scroll)
                        .hideScrollBar(),
                    isExpandedState = isNavRailExpandedState,
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(100.percent - navRailWidthPx.px)
                        .fillMaxHeight()
                        .overflow(overflowX = Overflow.Scroll, overflowY = Overflow.Hidden)
                        .hideScrollBar()
                ) {
                    Crossfade(
                        targetState = currentRoute,
                        modifier = Modifier
                            .fillMaxWidth(100.percent - horizontalPaddingState.value.px)
                            .fillMaxHeight()
                            .minWidth(Constants.MOBILE_MAX_AVAILABLE_WIDTH.px)
                            .padding(top = Constants.CONTENT_PADDING),
                        onStateChange = { window.scrollTo(0.0, 0.0) },
                        animateTranslationY = false,
                    ) { animatedRoute ->
                        when (animatedRoute) {
                            is Route.Collection -> TextBox("Collection (${animatedRoute.id})")
                            Route.Collections -> TextBox("All Collections")
                            Route.Explore -> ExplorePage(
                                Modifier.padding(bottom = Constants.CONTENT_PADDING)
                            )

                            Route.History -> HistoryPage()
                            Route.Home -> HomePage(
                                showPersonalisedFeedDialogState = showPersonalisedFeedDialogState,
                            )

                            Route.LikedVideos -> TextBox("Liked Videos")
                            is Route.Page -> TextBox("Channel (${animatedRoute.id})")
                            is Route.Playlist -> TextBox("Playlist (${animatedRoute.id})")
                            Route.Playlists -> TextBox("All Playlists")
                            is Route.Short -> ShortDetails(
                                id = animatedRoute.id,
                                onBackPressed = navigator::pop,
                            )

                            Route.Shorts -> ShortsGrid(showPersonalisedFeedDialogState)
                            Route.Subscriptions -> TextBox("Subscriptions")
                            Route.TVMode -> TVModePage()
                            is Route.Video -> VideoPlayerPage(videoId = animatedRoute.id)
                            Route.WatchLater -> TextBox("Watch Later")
                            is Route.Search -> SearchPage(query = animatedRoute.query)
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
            PersonalisedFeedDialog(
                onClose = { showPersonalisedFeedDialogState.value = false }
            )
        }
    }
}
