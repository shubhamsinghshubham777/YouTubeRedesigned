package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntSize
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Assets
import com.google.youtube.utils.HorizontalScrollState
import com.google.youtube.utils.Styles
import com.google.youtube.utils.bindScrollState
import com.google.youtube.utils.hideScrollBar
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element

@Composable
fun MissedVideosContainer(modifier: Modifier = Modifier) {
    var rowRef by remember { mutableStateOf<Element?>(null) }
    val containerPadding = remember { 30.px }
    var showContainer by remember { mutableStateOf(true) }
    val horizontalScrollState = remember { mutableStateOf(HorizontalScrollState.ReachedStart) }

    AnimatedVisibility(
        isVisible = showContainer,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(Styles.MISSED_VIDEOS_CONTAINER)
                .clip(Rect(20.px))
                .padding(topBottom = containerPadding),
            verticalArrangement = Arrangement.spacedBy(containerPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(leftRight = containerPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .fontFamily(Styles.Fonts.ROBOTO_CONDENSED)
                        .fontSize(24.px)
                        .fontWeight(FontWeight.Medium)
                ) { Text("In Case You Missed") }

                AssetImageButton(Assets.Icons.CLOSE) { showContainer = false }
            }

            Box {
                Row(
                    ref = ref { elementRef -> rowRef = elementRef },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(leftRight = containerPadding)
                        .overflow { x(Overflow.Scroll) }
                        .bindScrollState(horizontalScrollState)
                        .hideScrollBar()
                        .scrollBehavior(ScrollBehavior.Smooth),
                    horizontalArrangement = Arrangement.spacedBy(VIDEO_THUMBNAIL_CARDS_GAP.px),
                ) {
                    repeat(10) {
                        VideoThumbnailCard(
                            thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                            channelAsset = Assets.Icons.USER_AVATAR,
                            title = "How Websites Learned to Fit Everywhere",
                            channelName = "Juxtopposed",
                            isVerified = true,
                            views = "150K",
                            daysSinceUploaded = "4 months",
                            duration = "12:07",
                            size = IntSize(
                                width = VideoThumbnailCardDefaults.WIDTH,
                                height = VideoThumbnailCardDefaults.HEIGHT,
                            ),
                        )
                    }
                }

                // Manual scroll buttons
                RowScrollButtons(
                    elementToControl = rowRef,
                    horizontalScrollState = horizontalScrollState,
                    containerPadding = containerPadding,
                    scrollPixels = VideoThumbnailCardDefaults.WIDTH.plus(VIDEO_THUMBNAIL_CARDS_GAP),
                )
            }
        }
    }
}

private const val VIDEO_THUMBNAIL_CARDS_GAP: Double = 20.0
