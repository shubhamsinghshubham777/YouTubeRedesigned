package com.google.youtube.components.widgets.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.context_menu.TextField
import com.google.youtube.models.LiveChatItemData
import com.google.youtube.models.colorFromUserId
import com.google.youtube.utils.Assets
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.css.UserSelect
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.columnGap
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.userSelect
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.flexGrow
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.w3c.dom.Element

@Composable
fun LiveChatSection(modifier: Modifier = Modifier) {
    var scrollableColumnRef by remember { mutableStateOf<Element?>(null) }
    val chatMessageState = remember { mutableStateOf("") }
    val sampleLiveChat = remember {
        mutableStateListOf(
            LiveChatItemData(userId = "youtube-enjoyer", message = "omg"),
            LiveChatItemData(userId = "bob", message = "lets gooooo"),
            LiveChatItemData(userId = "youtube-enjoyer", message = "we are so back"),
            LiveChatItemData(userId = "youtube-enjoyer", message = "have we ever been this back"),
            LiveChatItemData(userId = "videowatcher", message = "right on time"),
            LiveChatItemData(userId = "therizzler", message = "lets goooo"),
            LiveChatItemData(userId = "gamer123", message = "@therizzler hey"),
            LiveChatItemData(
                userId = "imhonestlyoutofusernameideas",
                message = "wow im right on time for this video. can’t wait to watch it.",
            ),
            LiveChatItemData(
                userId = "gamer123",
                message = "bro can’t wait to watch the video but didn’t take the time to choose " +
                        "a proper username lol",
            ),
            LiveChatItemData(
                userId = "youtube-enjoyer",
                message = "*opens youtube to enjoy videos* *watches this video* *watches " +
                        "this video* there. now i can’t enjoy youtube >:("
            ),
            LiveChatItemData(
                userId = "bob",
                message = "@youtube-enjoyer change your name to youtube-non-enjoyer",
            ),
            LiveChatItemData(userId = "youtube-enjoyer", message = "@bob i might actually do that"),
            LiveChatItemData(userId = "bob", message = "@youtube-enjoyer awesome"),
            LiveChatItemData(userId = "therizzler", message = "@gamer123 hi"),
            LiveChatItemData(userId = "videowatcher", message = "w"),
            LiveChatItemData(userId = "youtube-enjoyer", message = "w’s in the chat"),
            LiveChatItemData(userId = "gamer123", message = "hell yeahhh"),
            LiveChatItemData(userId = "gamer123", message = "im watching this while having lunch"),
        )
    }

    LaunchedEffect(sampleLiveChat.size) {
        scrollableColumnRef?.apply {
            scrollTo(x = 0.0, y = (scrollHeight - clientHeight).toDouble())
        }
    }

    Column(
        modifier = Modifier
            .background(Styles.LIVE_CHAT_CONTAINER)
            .border(
                LiveChatSectionDefaults.BORDER_THICKNESS,
                LineStyle.Solid,
                Styles.LIVE_CHAT_CONTAINER_BORDER
            )
            .borderRadius(LiveChatSectionDefaults.BORDER_RADIUS)
            .then(modifier),
    ) {
        // Top Section
        SpacedRow(
            spacePx = 10,
            modifier = Modifier
                .background(Styles.SURFACE)
                .borderRadius(
                    topLeft = LiveChatSectionDefaults.BORDER_RADIUS,
                    topRight = LiveChatSectionDefaults.BORDER_RADIUS
                )
                .fillMaxWidth()
                .noShrink()
                .overflow { x(Overflow.Scroll) }
                .padding(12.px),
        ) {
            sampleLiveChatTopItems.forEach { data -> LiveChatTopItem(data = data) }
        }
        // Bottom Border for the row above
        Box(
            modifier = Modifier
                .background(Styles.LIVE_CHAT_CONTAINER_BORDER)
                .fillMaxWidth()
                .height(LiveChatSectionDefaults.BORDER_THICKNESS)
        )

        // Scrollable Chat Section
        SpacedColumn(
            ref = ref { e -> scrollableColumnRef = e },
            spacePx = 8,
            modifier = Modifier
                .overflow { y(Overflow.Scroll) }
                .scrollBehavior(ScrollBehavior.Smooth)
                .weight(1)
                .padding(left = 20.px, top = 12.px, right = 24.px, bottom = 16.px),
        ) {
            sampleLiveChat.forEach { chat ->
                AnnotatedTextRow {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TextBox(
                            color = chat.color.toKobwebColor(),
                            lineHeight = 24,
                            size = 14,
                            text = chat.userId,
                            weight = FontWeight.Medium,
                        )
                        TextBox(
                            color = Styles.LIVE_CHAT_USERNAME_DIVIDER,
                            lineHeight = 24,
                            size = 14,
                            text = ":",
                            weight = FontWeight.Medium,
                        )
                    }
                    chat.message.split(' ').forEach { segment ->
                        TextBox(
                            color = if (segment.startsWith('@')) {
                                colorFromUserId(segment.substringAfter('@')).toKobwebColor()
                            } else Styles.WHITE,
                            lineHeight = 24,
                            size = 14,
                            text = segment,
                        )
                    }
                }
            }
        }

        // Top Border for the row below
        Box(
            modifier = Modifier
                .background(Styles.LIVE_CHAT_CONTAINER_BORDER)
                .fillMaxWidth()
                .height(LiveChatSectionDefaults.BORDER_THICKNESS)
        )

        SpacedRow(
            spacePx = 8,
            modifier = Modifier
                .fillMaxWidth()
                .background(Styles.SURFACE)
                .borderRadius(
                    bottomLeft = LiveChatSectionDefaults.BORDER_RADIUS,
                    bottomRight = LiveChatSectionDefaults.BORDER_RADIUS
                )
                .padding(leftRight = 8.px),
        ) {
            TextField(
                hintText = "Type to chat...",
                showBorder = false,
                textState = chatMessageState,
                onEnterKeyPressed = { sendMessage(sampleLiveChat, chatMessageState) },
            ) {
                width(0.px)
                flexGrow(1)
            }
            if (chatMessageState.value.isNotBlank()) {
                AssetImageButton(
                    asset = Assets.Icons.ARROW_RIGHT,
                    containerColor = Styles.PINK_DARKENED,
                    onClick = { sendMessage(sampleLiveChat, chatMessageState) },
                )
            }
            AssetImageButton(
                asset = Assets.Icons.SMILEY,
                modifier = Modifier.opacity(LiveChatSectionDefaults.CTA_OPACITY),
            ) {}
            AssetImageButton(
                asset = Assets.Icons.MORE,
                modifier = Modifier.opacity(LiveChatSectionDefaults.CTA_OPACITY),
            ) {}
        }
    }
}

private fun sendMessage(
    sampleLiveChat: SnapshotStateList<LiveChatItemData>,
    chatMessageState: MutableState<String>,
) {
    sampleLiveChat.add(LiveChatItemData(userId = "current-user", message = chatMessageState.value))
    chatMessageState.value = ""
}

@Composable
private fun AnnotatedTextRow(
    columnGapPx: Int = 4,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .columnGap(columnGapPx.px)
            .display(DisplayStyle.Flex)
            .flexWrap(FlexWrap.Wrap)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

private object LiveChatSectionDefaults {
    val BORDER_THICKNESS = 1.px
    val BORDER_RADIUS = 20.px
    const val CTA_OPACITY = 0.6f
}

@Composable
private fun LiveChatTopItem(data: LiveChatItemData) {
    SpacedRow(
        spacePx = 8,
        modifier = Modifier
            .background(data.color.copy(alpha = 0.25f).toKobwebColor())
            .borderRadius(44.px)
            .noShrink()
            .padding(left = 4.px, top = 4.px, right = 10.px, bottom = 4.px)
            .userSelect(UserSelect.None),
    ) {
        Box(
            modifier = Modifier
                .background(data.color.toKobwebColor())
                .clip(Circle())
                .size(24.px),
            contentAlignment = Alignment.Center,
        ) {
            TextBox(
                color = Styles.BLACK,
                size = 11,
                text = (data.userInitial ?: '?').toString(),
                weight = FontWeight.Medium,
            )
        }
        TextBox(
            lineHeight = 24,
            size = 14,
            text = data.message,
            weight = FontWeight.Medium,
        )
    }
}

private val sampleLiveChatTopItems = listOf(
    LiveChatItemData(userId = "0", userInitial = 'S', message = "$4.99"),
    LiveChatItemData(userId = "1", userInitial = 'S', message = "2 months"),
    LiveChatItemData(userId = "2", userInitial = 'S', message = "1 year"),
    LiveChatItemData(userId = "3", userInitial = 'S', message = "$50"),
)
