package com.google.youtube.components.widgets.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import com.google.youtube.components.widgets.SegmentedButton
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.pages.SegmentedContentType
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.rememberIsLargeBreakpoint
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@Composable
fun SegmentedContent(
    state: MutableState<SegmentedContentType>,
    modifier: Modifier = Modifier,
) {
    val isLargeBreakpoint by rememberIsLargeBreakpoint()
    Column(modifier = Modifier.fillMaxSize().then(modifier)) {
        SegmentedButton(
            segments = SegmentedContentType.entries.map { e -> e.label },
            selectedIndex = state.value.ordinal,
            onSegmentClick = { index ->
                state.value = SegmentedContentType.entries.elementAt(index)
            },
        )
        Crossfade(
            targetState = state.value,
            modifier = Modifier.fillMaxWidth().weight(1),
        ) { animatedIndex ->
            when (animatedIndex) {
                SegmentedContentType.Suggestions -> SpacedColumn(
                    spacePx = 24,
                    modifier = Modifier.padding(topBottom = 24.px)
                ) {
                    repeat(3) {
                        SuggestionSection(
                            author = "Juxtopossed",
                            videos = List(3) {
                                VideoThumbnailDetails(
                                    thumbnailAsset = Assets.Thumbnails.THUMBNAIL_1,
                                    channelAsset = Assets.Avatars.AVATAR_JACKSEPTICEYE,
                                    title = "I Redesigned the ENTIRE Spotify UI from Scratch",
                                    channelName = "Juxtopposed",
                                    isVerified = true,
                                    views = "1.7M",
                                    daysSinceUploaded = "5 months",
                                    duration = "10:24",
                                )
                            }
                        )
                    }
                }

                SegmentedContentType.Transcripts -> Box(
                    modifier = Modifier.fillMaxWidth().margin(top = 24.px),
                    contentAlignment = Alignment.Center,
                ) {
                    TextBox(text = "Your transcripts will show here")
                }

                SegmentedContentType.LiveChat -> LiveChatSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (isLargeBreakpoint) 84.vh else 65.vh)
                        .margin(top = 8.px, bottom = 22.px)
                )
            }
        }
    }
}
