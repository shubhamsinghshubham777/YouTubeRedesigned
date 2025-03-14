package com.google.youtube.components.widgets.comments

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.SegmentedButtonPair
import com.google.youtube.components.widgets.UnderlinedToggleText
import com.google.youtube.models.VideoCommentData
import com.google.youtube.utils.Asset
import com.google.youtube.utils.FadeInOut
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.clickable
import com.google.youtube.utils.hideScrollBar
import com.google.youtube.utils.isSmallerThan
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberBreakpointAsState
import com.google.youtube.utils.rememberElementHeightAsState
import com.google.youtube.utils.rememberMouseEventAsState
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.boxShadow
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.rotate
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.translate
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.deg
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element

@Composable
fun CommentItem(data: VideoCommentData) {
    var commentAndRepliesElement by remember { mutableStateOf<Element?>(null) }
    val containerHeight by rememberElementHeightAsState(commentAndRepliesElement)
    val animatedContainerHeight by animateFloatAsState(containerHeight.toFloat())
    var areRepliesCollapsed by remember { mutableStateOf(true) }
    val animatedArrowBtnRotation by animateFloatAsState(if (areRepliesCollapsed) 180f else 0f)

    SpacedRow(spacePx = 12, modifier = Modifier.fillMaxWidth(), centerContentVertically = false) {
        // User Avatar & Expand/Collapse Arrow
        Column(
            modifier = Modifier.height(animatedContainerHeight.px),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Image(modifier = Modifier.size(43.px), src = data.userAssetRef)
            if (data.replies.isNotEmpty()) {
                // Vertical Divider
                Box(
                    modifier = Modifier
                        .background(Styles.DIVIDER)
                        .noShrink()
                        .weight(1)
                        .width(2.px)
                )

                Box(
                    modifier = Modifier
                        .background(Styles.REPLY_TOGGLE_CONTAINER)
                        .borderRadius(20.px)
                        .boxShadow(
                            offsetY = 4.px,
                            blurRadius = 4.px,
                            color = Styles.BLACK.copyf(alpha = 0.25f),
                        )
                        .padding(leftRight = 9.px, topBottom = 4.px)
                        .clickable { areRepliesCollapsed = !areRepliesCollapsed },
                ) {
                    Image(
                        modifier = Modifier.rotate(animatedArrowBtnRotation.deg),
                        src = Asset.Icon.DOUBLE_ARROW_UP,
                    )
                }
            }
        }

        Column(
            ref = ref { e -> commentAndRepliesElement = e },
            modifier = Modifier.weight(1),
        ) {
            MessageAndControls(
                data = data,
                onRepliesClick = { areRepliesCollapsed = !areRepliesCollapsed },
            )
            FadeInOut(isVisible = !areRepliesCollapsed, modifier = Modifier.fillMaxWidth()) {
                SpacedColumn(
                    spacePx = 24,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (data.replies.isNotEmpty()) 24.px else 0.px),
                ) {
                    data.replies.forEach { childData -> CommentItem(childData) }
                }
            }
        }
    }
}

@Composable
private fun MessageAndControls(data: VideoCommentData, onRepliesClick: () -> Unit) {
    val breakpoint by rememberBreakpointAsState()
    val isSmallBreakpoint by remember { derivedStateOf { breakpoint.isSmallerThan(Breakpoint.SM) } }

    var isMessageCollapsed by remember { mutableStateOf(true) }
    var messageToggleTextRef by remember { mutableStateOf<Element?>(null) }
    val messageToggleTextMouseEventState by rememberMouseEventAsState(messageToggleTextRef)

    val animatedSegmentedButtonsSpacing by animateFloatAsState(if (isSmallBreakpoint) 4f else 20f)

    SpacedColumn(spacePx = 14, modifier = Modifier.fillMaxWidth()) {
        SpacedColumn(spacePx = 10, modifier = Modifier.fillMaxWidth()) {
            SpacedRow(spacePx = 10, modifier = Modifier.fillMaxWidth()) {
                TextBox(
                    maxLines = 1,
                    size = 14,
                    text = data.username,
                    weight = FontWeight.Medium,
                )
                TextBox(
                    color = Styles.VIDEO_CARD_SECONDARY_TEXT,
                    maxLines = 1,
                    size = 12,
                    text = "${data.durationSinceUploaded} ago",
                    modifier = Modifier.weight(1),
                )
                AssetImageButton(Asset.Icon.MORE) {}
            }
            SpacedColumn(8) {
                TextBox(
                    maxLines = if (isMessageCollapsed) 2 else null,
                    text = data.message
                )
                if (data.message.length > 200) {
                    UnderlinedToggleText(
                        isSelected = !isMessageCollapsed,
                        mouseEventState = messageToggleTextMouseEventState,
                        onClick = { isMessageCollapsed = !isMessageCollapsed },
                        ref = ref { e -> messageToggleTextRef = e },
                    )
                }
            }
        }

        // Controls
        SpacedRow(
            spacePx = animatedSegmentedButtonsSpacing,
            modifier = Modifier
                .fillMaxWidth()
                .overflow(overflowX = Overflow.Scroll, overflowY = Overflow.Hidden)
                .hideScrollBar(),
        ) {
            SegmentedButtonPair(
                assetPathLeft = Asset.Path.LIKED,
                assetPathRight = Asset.Path.DISLIKE,
                containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
                isDense = true,
                labelLeft = data.likeCount,
                labelRight = data.dislikeCount,
                onClickLeft = {},
                onClickRight = {},
            )
            SegmentedButtonPair(
                assetColorLeft = Styles.RED_LIGHT,
                assetPathLeft = if (data.replies.isNotEmpty()) Asset.Path.COMMENTS else null,
                containerColor = Styles.ELEVATED_BUTTON_CONTAINER,
                isDense = true,
                isRightLabelBold = true,
                labelLeft = if (data.replies.isNotEmpty()) data.replies.size.toString() else null,
                labelRight = "Reply",
                onClickLeft = onRepliesClick,
                onClickRight = {},
            )
            if (data.isHearted) {
                Box(modifier = Modifier.noShrink(), contentAlignment = Alignment.BottomEnd) {
                    Image(
                        modifier = Modifier.clip(Circle()).size(22.px),
                        src = Asset.Icon.USER_AVATAR,
                    )
                    Image(
                        modifier = Modifier
                            .translate(tx = 6.5.px, ty = 6.px)
                            .size(width = 13.px, height = 12.px),
                        src = Asset.Icon.HEART,
                    )
                }
            }
        }
    }
}
