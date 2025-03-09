package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.CategoryTab
import com.google.youtube.components.widgets.RowScrollButtons
import com.google.youtube.components.widgets.VideoThumbnailCard
import com.google.youtube.data.FeedProvider
import com.google.youtube.models.ExploreGridCategoryWithVideos
import com.google.youtube.models.ExploreGridDetails
import com.google.youtube.utils.Asset
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.HorizontalScrollState
import com.google.youtube.utils.Styles
import com.google.youtube.utils.bindScrollState
import com.google.youtube.utils.gridGap
import com.google.youtube.utils.hideScrollBar
import com.google.youtube.utils.limitTextWithEllipsis
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.gridAutoColumns
import com.varabyte.kobweb.compose.ui.modifiers.gridAutoFlow
import com.varabyte.kobweb.compose.ui.modifiers.gridTemplateRows
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.GridAutoFlow
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.INSTANT
import org.w3c.dom.ScrollToOptions

@Composable
fun ExplorePage(modifier: Modifier = Modifier) {
    val feedProvider = remember { FeedProvider() }

    val grids = remember(feedProvider) {
        val trendingNowFeed = feedProvider.getTrendingNowFeed()
        val musicFeed = feedProvider.getNewMusicFeed()

        listOf(
            ExploreGridDetails(
                asset = Asset.Icon.TRENDING_SELECTED,
                title = "Trending",
                categoriesWithVideos = listOf(
                    ExploreGridCategoryWithVideos("Now", trendingNowFeed),
                    ExploreGridCategoryWithVideos("Music", trendingNowFeed.shuffled()),
                    ExploreGridCategoryWithVideos("Gaming", trendingNowFeed.shuffled()),
                    ExploreGridCategoryWithVideos("Movies", trendingNowFeed.shuffled()),
                    ExploreGridCategoryWithVideos("Shorts", trendingNowFeed.shuffled()),
                ),
            ),
            ExploreGridDetails(
                asset = Asset.Icon.MUSIC_SELECTED,
                title = "Music",
                categoriesWithVideos = listOf(
                    ExploreGridCategoryWithVideos("New", musicFeed),
                    ExploreGridCategoryWithVideos("Playlists", musicFeed.shuffled()),
                    ExploreGridCategoryWithVideos("Top Charts", musicFeed.shuffled()),
                ),
            ),
            ExploreGridDetails(
                asset = Asset.Icon.GAMES_SELECTED, title = "Gaming",
                categoriesWithVideos = listOf(
                    ExploreGridCategoryWithVideos(videos = feedProvider.getGamingFeed()),
                ),
            ),
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth().then(modifier),
        verticalArrangement = Arrangement.spacedBy(CONTAINER_PADDING),
    ) {
        grids.forEach { details -> ExploreGridSection(details) }
    }
}

@Composable
private fun ExploreGridSection(details: ExploreGridDetails) {
    var selectedTab by remember {
        mutableStateOf(details.categoriesWithVideos.firstOrNull()?.label)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.px, LineStyle.Solid, Styles.DIVIDER)
            .borderRadius(24.px)
            .padding(topBottom = CONTAINER_PADDING),
        verticalArrangement = Arrangement.spacedBy(27.px)
    ) {
        // Header
        Row(
            modifier = Modifier.padding(leftRight = CONTAINER_PADDING).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(CONTAINER_PADDING),
        ) {
            Image(
                modifier = Modifier
                    .backgroundImage(Styles.Gradient.RED_TO_PINK)
                    .clip(Circle())
                    .padding(10.px),
                src = details.asset,
                width = 44,
                height = 44
            )

            Box(
                modifier = Modifier
                    .fontSize(28.px)
                    .fontWeight(FontWeight.Medium)
                    .weight(1)
                    .limitTextWithEllipsis(1)
            ) { Text(details.title) }

            AssetSvgButton(
                id = "view_all_button_${details.title}",
                onClick = {},
                containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
            ) {
                Text("View all")
            }
        }

        // Tabs
        val categories = details.categoriesWithVideos.mapNotNull { e -> e.label }
        if (categories.isNotEmpty()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .padding(leftRight = 16.px)
                        .overflow { x(Overflow.Scroll) }
                        .hideScrollBar(),
                    horizontalArrangement = Arrangement.spacedBy(23.px)
                ) {
                    categories.forEach { category ->
                        CategoryTab(
                            label = category,
                            isSelected = category == selectedTab,
                            onClick = { selectedTab = category },
                        )
                    }
                }

                // Divider
                Box(modifier = Modifier.background(Styles.DIVIDER).fillMaxWidth().height(1.px))
            }
        }

        // Grid
        var gridElement by remember { mutableStateOf<Element?>(null) }
        Crossfade(
            targetState = selectedTab,
            onStateChange = {
                gridElement?.scrollTo(
                    ScrollToOptions(left = 0.0, behavior = org.w3c.dom.ScrollBehavior.INSTANT)
                )
            },
        ) { animatedCategory ->
            val videos = details.categoriesWithVideos
                .find { categoryWithVideos -> categoryWithVideos.label == animatedCategory }
                ?.videos
            val horizontalScrollState = remember {
                mutableStateOf(HorizontalScrollState.ReachedStart)
            }

            Box {
                Div(
                    Modifier
                        .fillMaxWidth()
                        .scrollBehavior(ScrollBehavior.Smooth)
                        .bindScrollState(horizontalScrollState)
                        .hideScrollBar()
                        .display(DisplayStyle.Grid)
                        .overflow { x(Overflow.Auto) }
                        .padding(leftRight = CONTAINER_PADDING)
                        .gridAutoColumns { size(GRID_THUMBNAIL_CARD_SIZE.width.px) }
                        .gridAutoFlow(GridAutoFlow.Column)
                        .gridTemplateRows {
                            repeat(if ((videos?.size ?: 0) <= 10) 1 else 2) { size(1.fr) }
                        }
                        .gridGap(GRID_ITEMS_GAP.px)
                        .toAttrs { ref { element -> gridElement = element; onDispose {} } }
                ) {
                    videos?.forEach { details ->
                        VideoThumbnailCard(details = details, size = GRID_THUMBNAIL_CARD_SIZE)
                    }
                }

                // Manual scroll buttons
                RowScrollButtons(
                    elementToControl = gridElement,
                    horizontalScrollState = horizontalScrollState,
                    containerPadding = CONTAINER_PADDING,
                    scrollPixels = (GRID_THUMBNAIL_CARD_SIZE.width + GRID_ITEMS_GAP).toDouble(),
                    centerVertically = true,
                    gradientColor = Styles.SURFACE,
                )
            }
        }
    }
}

private val CONTAINER_PADDING = 24.px
private val GRID_THUMBNAIL_CARD_SIZE = IntSize(width = 344, height = 192)
private const val GRID_ITEMS_GAP = 20
