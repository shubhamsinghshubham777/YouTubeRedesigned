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
import com.varabyte.kobweb.compose.css.PointerEvents
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.functions.LinearGradient
import com.varabyte.kobweb.compose.css.functions.linearGradient
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.fontWeight
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.pointerEvents
import com.varabyte.kobweb.compose.ui.modifiers.position
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.plus
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element

@Composable
fun MissedVideosContainer() {
    var rowRef by remember { mutableStateOf<Element?>(null) }
    val containerPadding = remember { 30.px }
    var showContainer by remember { mutableStateOf(true) }
    val horizontalScrollState = remember { mutableStateOf(HorizontalScrollState.ReachedStart) }

    AnimatedVisibility(
        isVisible = showContainer,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Styles.MISSED_VIDEOS_CONTAINER)
                .clip(Rect(20.px))
                .padding(topBottom = containerPadding)
                .margin(bottom = 40.px),
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
                            views = "150K",
                            daysSinceUploaded = "4 months ago",
                            duration = "12:07",
                            size = IntSize(
                                width = VideoThumbnailCardDefaults.WIDTH,
                                height = VideoThumbnailCardDefaults.HEIGHT,
                            ),
                        )
                    }
                }

                //  Buttons
                repeat(2) { index ->
                    val startItem = index == 0
                    AnimatedVisibility(
                        isVisible = if (startItem) horizontalScrollState.value != HorizontalScrollState.ReachedStart
                        else horizontalScrollState.value != HorizontalScrollState.ReachedEnd,
                        modifier = Modifier
                            .position(Position.Absolute)
                            .align(if (startItem) Alignment.CenterStart else Alignment.CenterEnd)
                            .zIndex(1),
                    ) {
                        Box(
                            modifier = Modifier
                                .backgroundImage(
                                    linearGradient(
                                        dir = if (startItem) LinearGradient.Direction.ToRight
                                        else LinearGradient.Direction.ToLeft
                                    ) {
                                        add(Styles.MISSED_VIDEOS_CONTAINER)
                                        add(Colors.Transparent)
                                    }
                                )
                                .width(56.px)
                                .height(rowRef?.clientHeight?.px?.plus(containerPadding) ?: 0.px)
                                .pointerEvents(PointerEvents.None),
                            contentAlignment = if (startItem) Alignment.TopStart else Alignment.TopEnd
                        ) {
                            AssetImageButton(
                                if (startItem) Assets.Icons.ARROW_LEFT else Assets.Icons.ARROW_RIGHT,
                                modifier = Modifier
                                    .margin(leftRight = 22.px, top = 96.px)
                                    .background(Styles.MISSED_VIDEOS_ARROW_CONTAINER)
                                    .pointerEvents(PointerEvents.Auto)
                            ) {
                                rowRef?.scrollBy(
                                    x = VideoThumbnailCardDefaults.WIDTH
                                        .plus(VIDEO_THUMBNAIL_CARDS_GAP)
                                        .times(if (startItem) -1 else 1),
                                    y = 0.0
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private const val VIDEO_THUMBNAIL_CARDS_GAP: Double = 20.0
