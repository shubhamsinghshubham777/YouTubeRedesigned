package com.google.youtube.components.widgets.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.IconLabel
import com.google.youtube.components.widgets.SegmentedButtonPair
import com.google.youtube.components.widgets.SubscribeButton
import com.google.youtube.components.widgets.UnderlinedToggleText
import com.google.youtube.components.widgets.comments.CommentsSection
import com.google.youtube.data.VideoPlayerDataProvider
import com.google.youtube.pages.SegmentedContentType
import com.google.youtube.utils.Asset
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.Route
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.clickable
import com.google.youtube.utils.rememberIsLargeBreakpoint
import com.google.youtube.utils.rememberMouseEventAsState
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.WhiteSpace
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.columnGap
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.rowGap
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.modifiers.whiteSpace
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element

@Composable
fun PlayerAndComments(
    modifier: Modifier = Modifier,
    videoId: String,
    selectedSegment: MutableState<SegmentedContentType>,
    isTheaterModeOn: MutableState<Boolean>,
    isSegmentedContentVisible: MutableState<Boolean>,
    segmentedContent: (@Composable () -> Unit)?,
) {
    val navigator = LocalNavigator.current
    val isLargeBreakpoint by rememberIsLargeBreakpoint()

    var isDescriptionBoxExpanded by remember { mutableStateOf(false) }
    var descriptionToggleRef by remember { mutableStateOf<Element?>(null) }
    val descriptionToggleMouseEventState by rememberMouseEventAsState(descriptionToggleRef)

    // Data States
    val videoPlayerDataProvider = remember { VideoPlayerDataProvider() }
    val videoDetails = remember(videoPlayerDataProvider, videoId) {
        videoPlayerDataProvider.getVideoDetailsForId(videoId)
    }

    Column(modifier = modifier) {
        VideoPlayer(
            videoId = videoId,
            isTheaterModeOn = isTheaterModeOn,
            selectedSegment = selectedSegment,
        )

        Box(modifier = Modifier.height(13.px))

        TextBox(
            maxLines = 2,
            size = 20,
            text = videoDetails.title,
            weight = FontWeight.SemiBold,
        )
        Box(modifier = Modifier.height(12.5.px))

        // Video Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .display(DisplayStyle.Flex)
                .flexWrap(FlexWrap.Wrap)
                .columnGap(20.px)
                .rowGap(20.px),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SpacedRow(20) {
                var channelRowRef by remember { mutableStateOf<Element?>(null) }
                val isChannelRowHovered =
                    rememberMouseEventAsState(channelRowRef).value == MouseEventState.Hovered

                SpacedRow(
                    ref = ref { e -> channelRowRef = e },
                    spacePx = 15,
                    modifier = Modifier.clickable {
                        navigator.pushRoute(Route.Page(id = videoDetails.channelId))
                    }
                ) {
                    Image(
                        modifier = Modifier.size(46.px).clip(Circle()),
                        src = videoDetails.channelAsset,
                    )
                    SpacedColumn(1.29) {
                        SpacedRow(8) {
                            TextBox(
                                modifier = Modifier.thenIf(isChannelRowHovered) {
                                    Modifier.textDecorationLine(TextDecorationLine.Underline)
                                },
                                text = videoDetails.channelName,
                                size = 18,
                                lineHeight = 28.3,
                                weight = FontWeight.Medium,
                            )
                            Image(src = Asset.Icon.VERIFIED_BADGE)
                        }
                        TextBox(
                            text = "${videoDetails.subscribersCount} subscribers",
                            size = 14,
                            color = Styles.VIDEO_CARD_SECONDARY_TEXT,
                        )
                    }
                }
                SubscribeButton()
            }
            SpacedRow(24) {
                IconLabel(iconAsset = Asset.Icon.EYE, label = videoDetails.viewCount)
                IconLabel(iconAsset = Asset.Icon.DATE, label = videoDetails.uploadDate)
            }
            SpacedRow(
                spacePx = 10,
                modifier = Modifier
                    .display(DisplayStyle.Flex)
                    .flexWrap(FlexWrap.Wrap)
                    .rowGap(16.px)
            ) {
                SegmentedButtonPair(
                    assetPathLeft = Asset.Path.LIKED,
                    assetPathRight = Asset.Path.DISLIKE,
                    containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
                    isLeftLabelBold = true,
                    labelLeft = videoDetails.likeCount,
                    labelRight = videoDetails.dislikeCount,
                    onClickLeft = {},
                    onClickRight = {},
                )
                AssetImageButton(
                    asset = Asset.Icon.SHARE,
                    containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
                ) {}
                AssetImageButton(
                    asset = Asset.Icon.ADD_TO_PLAYLIST,
                    containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
                ) {}
                AssetImageButton(
                    asset = Asset.Icon.WATCH_LATER,
                    containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
                ) {}
                AssetImageButton(
                    asset = Asset.Icon.MORE_HORIZONTAL,
                    containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
                ) {}
                if (!isLargeBreakpoint) {
                    AssetImageButton(
                        asset = Asset.Icon.ARROW_LEFT,
                        containerColor = Styles.ELEVATED_BUTTON_CONTAINER
                    ) { isSegmentedContentVisible.value = !isSegmentedContentVisible.value }
                }
            }
        }

        Box(modifier = Modifier.height(20.5.px))

        // Description
        Box(
            modifier = Modifier
                .border(1.px, LineStyle.Solid, Styles.WHITE.copyf(alpha = 0.1f))
                .borderRadius(15.43.px)
                .fillMaxWidth()
                .padding(leftRight = 24.px, topBottom = 20.px)
                .whiteSpace(WhiteSpace.PreLine)
        ) {
            SpacedColumn(16) {
                TextBox(
                    text = videoDetails.description,
                    maxLines = if (isDescriptionBoxExpanded) null else 3,
                )
                UnderlinedToggleText(
                    isSelected = isDescriptionBoxExpanded,
                    mouseEventState = descriptionToggleMouseEventState,
                    onClick = { isDescriptionBoxExpanded = !isDescriptionBoxExpanded },
                    ref = ref { e -> descriptionToggleRef = e },
                )
            }
        }

        Box(modifier = Modifier.height(31.px))

        // Comments
        SpacedRow(
            spacePx = 16,
            modifier = Modifier.fillMaxWidth(),
            centerContentVertically = false,
        ) {
            CommentsSection(modifier = Modifier.weight(1), videoId = videoId)
            segmentedContent?.invoke()
        }
    }
}
