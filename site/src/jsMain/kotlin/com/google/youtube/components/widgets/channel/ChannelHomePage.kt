package com.google.youtube.components.widgets.channel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.RowScrollButtons
import com.google.youtube.components.widgets.VideoThumbnailCard
import com.google.youtube.models.ChannelListItemData
import com.google.youtube.utils.AnimatedVisibility
import com.google.youtube.utils.Asset
import com.google.youtube.utils.HorizontalScrollState
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.bindScrollState
import com.google.youtube.utils.hideScrollBar
import com.google.youtube.utils.noShrink
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.thenIf
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element

@Composable
fun ChannelHomePage() {
    var isMissedPostsContainerVisible by remember { mutableStateOf(true) }

    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            isVisible = isMissedPostsContainerVisible,
            modifier = Modifier.fillMaxWidth(),
        ) {
            ChannelListItemsContainer(
                modifier = Modifier.margin(bottom = PARENT_CONTAINER_ITEM_GAP),
                title = "In Case You Missed",
                showBorder = true,
                onClose = { isMissedPostsContainerVisible = false },
                items = remember {
                    listOf(SAMPLE_POST_DATA, *Array(8) { SAMPLE_THUMBNAIL_DATA })
                },
            )
        }

        ChannelListItemsContainer(
            modifier = Modifier.margin(bottom = PARENT_CONTAINER_ITEM_GAP),
            title = "Popular Videos",
            items = remember { List(8) { SAMPLE_THUMBNAIL_DATA } },
        )

        ChannelListItemsContainer(
            title = "Latest Posts",
            showLargePostCards = true,
            items = remember { List(8) { SAMPLE_POST_DATA } },
        )
    }
}

@Composable
private fun ChannelListItemsContainer(
    title: String,
    items: List<ChannelListItemData>,
    modifier: Modifier = Modifier,
    showLargePostCards: Boolean = false,
    showBorder: Boolean = false,
    onClose: (() -> Unit)? = null,
) {
    var scrollableRowRef by remember { mutableStateOf<Element?>(null) }
    val horizontalScrollState = remember {
        mutableStateOf(HorizontalScrollState.ReachedStart)
    }

    SpacedColumn(
        spacePx = 24,
        modifier = Modifier
            .fillMaxWidth()
            .thenIf(showBorder) {
                Modifier
                    .border(1.px, LineStyle.Solid, Styles.DIVIDER)
                    .borderRadius(20.px)
                    .padding(24.px)
            }
            .then(modifier),
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextBox(
                modifier = Modifier.weight(1),
                text = title,
                family = Styles.Fonts.ROBOTO_CONDENSED,
                weight = FontWeight.Medium,
                size = 24,
            )
            onClose?.let { callback ->
                AssetImageButton(asset = Asset.Icon.CLOSE, onClick = callback)
            }
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            SpacedRow(
                ref = ref { e -> scrollableRowRef = e },
                spacePx = 20,
                modifier = Modifier
                    .bindScrollState(horizontalScrollState)
                    .fillMaxWidth()
                    .height(ITEM_MAX_HEIGHT)
                    .hideScrollBar()
                    .overflow { x(Overflow.Scroll) }
                    .scrollBehavior(ScrollBehavior.Smooth),
            ) {
                items.forEach { data ->
                    when (data) {
                        is ChannelListItemData.Post -> OutlinedPost(
                            data = data,
                            modifier = Modifier
                                .width(
                                    if (showLargePostCards) ITEM_WIDTH_LARGE_PX.px
                                    else ITEM_WIDTH_PX.px
                                )
                                .fillMaxHeight(),
                            onLike = {},
                            onDislike = {},
                            onComment = {},
                            onShare = {},
                        )

                        is ChannelListItemData.Thumbnail -> VideoThumbnailCard(
                            modifier = Modifier.width(ITEM_WIDTH_PX.px).noShrink(),
                            details = data.toThumbnailDetails(),
                        )
                    }
                }
            }

            RowScrollButtons(
                containerPadding = 0.px,
                elementToControl = scrollableRowRef,
                gradientColor = Styles.SURFACE,
                horizontalScrollState = horizontalScrollState,
                scrollPixels = ITEM_WIDTH_PX,
            )
        }
    }
}

private const val ITEM_WIDTH_PX = 354.0
private const val ITEM_WIDTH_LARGE_PX = 487.67
private val ITEM_MAX_HEIGHT = 326.px
private val SAMPLE_THUMBNAIL_DATA = ChannelListItemData.Thumbnail(
    // TODO: Use real data
    id = "wxhdymaizuppmq",
    channelAsset = Asset.Icon.USER_AVATAR,
    channelName = "Juxtopposed",
    daysSinceUploaded = "1 day",
    isChannelVerified = true,
    subscribersCount = "295K",
    thumbnailAsset = Asset.Thumbnails.THUMBNAIL_1,
    videoDuration = "12:07",
    videoTitle = "I Redesigned the ENTIRE YouTube UI from Scratch",
    viewCount = "120K",
)
private val SAMPLE_POST_DATA = ChannelListItemData.Post(
    // TODO: Use real data
    id = "wxhdymaizuppmq",
    channelAsset = Asset.Icon.USER_AVATAR,
    channelName = "Juxtopposed",
    daysSinceUploaded = "1 day",
    isChannelVerified = true,
    subscribersCount = "295K",
    commentCount = "1.1K",
    dislikeCount = "12",
    likeCount = "2.9K",
    message = "it’s finally time for youtube. what are your biggest issues with it?",
)
private val PARENT_CONTAINER_ITEM_GAP = 50.px
