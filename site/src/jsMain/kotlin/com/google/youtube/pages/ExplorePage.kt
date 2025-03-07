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
import com.google.youtube.models.ExploreGridCategoryWithVideos
import com.google.youtube.models.ExploreGridDetails
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.utils.Assets
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
fun ExplorePage(
    modifier: Modifier = Modifier,
    grids: List<ExploreGridDetails> = fakeGridDetails,
) {
    Column(
        modifier = Modifier.fillMaxWidth().then(modifier),
        verticalArrangement = Arrangement.spacedBy(containerPadding),
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
            .padding(topBottom = containerPadding),
        verticalArrangement = Arrangement.spacedBy(27.px)
    ) {
        // Header
        Row(
            modifier = Modifier.padding(leftRight = containerPadding).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(containerPadding),
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
                        .padding(leftRight = containerPadding)
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
                    containerPadding = containerPadding,
                    scrollPixels = (GRID_THUMBNAIL_CARD_SIZE.width + GRID_ITEMS_GAP).toDouble(),
                    centerVertically = true,
                    gradientColor = Styles.SURFACE,
                )
            }
        }
    }
}

private val containerPadding = 24.px

private val fakeVideoThumbnailDetails: List<VideoThumbnailDetails> = List(20) { index ->
    VideoThumbnailDetails(
        id = index.toString(),
        thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
        channelAsset = Assets.Icons.USER_AVATAR,
        title = "How Websites Learned to Fit Everywhere",
        channelName = "Juxtopposed",
        isVerified = true,
        views = "150K",
        daysSinceUploaded = "4 months",
        duration = "12:07",
    )
}

private val fakeGridDetails: List<ExploreGridDetails> = listOf(
    ExploreGridDetails(
        asset = Assets.Icons.TRENDING_SELECTED,
        title = "Trending",
        categoriesWithVideos = listOf(
            ExploreGridCategoryWithVideos(label = "Now", videos = fakeVideoThumbnailDetails),
            ExploreGridCategoryWithVideos(label = "Music", videos = fakeVideoThumbnailDetails),
            ExploreGridCategoryWithVideos(label = "Gaming", videos = fakeVideoThumbnailDetails),
            ExploreGridCategoryWithVideos(label = "Movies", videos = fakeVideoThumbnailDetails),
            ExploreGridCategoryWithVideos(label = "Shorts", videos = fakeVideoThumbnailDetails),
        ),
    ),
    ExploreGridDetails(
        asset = Assets.Icons.MUSIC_SELECTED,
        title = "Music",
        categoriesWithVideos = listOf(
            ExploreGridCategoryWithVideos(
                label = "New",
                videos = fakeVideoThumbnailDetails.subList(0, 10)
            ),
            ExploreGridCategoryWithVideos(
                label = "Playlists",
                videos = fakeVideoThumbnailDetails.subList(0, 10)
            ),
            ExploreGridCategoryWithVideos(
                label = "Top Charts",
                videos = fakeVideoThumbnailDetails.subList(0, 10)
            ),
        ),
    ),
    ExploreGridDetails(
        asset = Assets.Icons.GAMES_SELECTED, title = "Gaming",
        categoriesWithVideos = listOf(
            ExploreGridCategoryWithVideos(videos = fakeVideoThumbnailDetails.subList(0, 10)),
        ),
    ),
)

private val GRID_THUMBNAIL_CARD_SIZE = IntSize(width = 344, height = 192)
private const val GRID_ITEMS_GAP = 20
