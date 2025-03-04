package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.CategoryTab
import com.google.youtube.components.widgets.HorizontalDivider
import com.google.youtube.components.widgets.Wrap
import com.google.youtube.components.widgets.channel.ChannelAboutPage
import com.google.youtube.components.widgets.channel.ChannelChannelsPage
import com.google.youtube.components.widgets.channel.ChannelHomePage
import com.google.youtube.components.widgets.channel.ChannelPlaylistsPage
import com.google.youtube.components.widgets.channel.ChannelPostsPage
import com.google.youtube.components.widgets.channel.ChannelShortsPage
import com.google.youtube.components.widgets.channel.ChannelVideosPage
import com.google.youtube.utils.Assets
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.rememberIsSmallBreakpoint
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.cursor
import com.varabyte.kobweb.compose.css.textDecorationLine
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.RowScope
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.maxWidth
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text

@Composable
fun ChannelPage(id: String) {
    val selectedTab = remember { mutableStateOf(Tab.Home) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header
        Box(modifier = Modifier.fillMaxWidth()) {
            // Banner
            Image(
                modifier = Modifier.fillMaxWidth().clip(Rect(22.px)).objectFit(ObjectFit.Cover),
                src = Assets.Banners.BANNER_1,
                height = 241,
            )

            Wrap(
                modifier = Modifier.fillMaxWidth().padding(top = 176.px, left = 27.px),
                verticalGapPx = 16,
                centerVertically = false,
            ) {
                // Thumbnail Image
                Image(
                    modifier = Modifier
                        .border(1.px, LineStyle.Solid, Styles.PAGE_THUMBNAIL_BORDER)
                        .borderRadius(80.px)
                        .clip(Circle()),
                    src = Assets.Icons.USER_AVATAR,
                    width = 160,
                    height = 160,
                )
                ChannelDetails()
                SocialLinks()
            }
        }

        // Content
        Tabs(selectedTab = selectedTab)
        HorizontalDivider(Styles.DIVIDER)
        Crossfade(
            targetState = selectedTab.value,
            modifier = Modifier.fillMaxWidth().padding(topBottom = 28.px)
        ) { animatedSelectedTabLabel ->
            when (animatedSelectedTabLabel) {
                Tab.Home -> ChannelHomePage()
                Tab.Videos -> ChannelVideosPage()
                Tab.Shorts -> ChannelShortsPage()
                Tab.Posts -> ChannelPostsPage()
                Tab.Playlists -> ChannelPlaylistsPage()
                Tab.Channels -> ChannelChannelsPage()
                Tab.About -> ChannelAboutPage()
            }
        }
    }
}

@Composable
private fun Tabs(selectedTab: MutableState<Tab>) {
    Wrap(
        horizontalGapPx = 23,
        verticalGapPx = 0,
        modifier = Modifier.fillMaxWidth().margin(top = 37.px),
    ) {
        Tab.entries.forEach { tab ->
            CategoryTab(
                label = tab.name,
                isSelected = selectedTab.value == tab,
                onClick = { selectedTab.value = tab },
            )
        }
        Spacer()
        AssetImageButton(Assets.Icons.SEARCH_DIM) {}
    }
}

@Composable
private fun RowScope.ChannelDetails() {
    val isSmallBreakpoint by rememberIsSmallBreakpoint()
    val isChannelDescHovered = remember { mutableStateOf(false) }
    val isChannelDescExpanded = remember { mutableStateOf(false) }

    SpacedColumn(
        spacePx = 18,
        modifier = Modifier.Companion.align(Alignment.Bottom)
            .weight(1)
            .padding(left = 20.px)
            .zIndex(1),
    ) {
        SpacedRow(8) {
            TextBox(text = "Juxtopposed", size = 28, lineHeight = 28.4)
            Image(src = Assets.Icons.VERIFIED_BADGE, width = 16, height = 16)
        }
        Span({
            style {
                color(Styles.VIDEO_CARD_SECONDARY_TEXT)
                if (!isSmallBreakpoint) maxWidth(37.vw)
            }
        }) {
            Text("@juxtopposed • ")
            Span({ style { color(Styles.WHITE) } }) { Text("295K ") }
            Text("subscribers • ")
            Span({ style { color(Styles.WHITE) } }) { Text("31 ") }
            Text("videos • ")
            Span({ style { color(Styles.WHITE) } }) {
                Text(
                    with("subscribe for more cats (and design content, of course, along with exciting new UI/UX trends!)") {
                        if (isChannelDescExpanded.value) this
                        else this.substring(0, 55)
                    } + "${if (!isChannelDescExpanded.value) " ..." else ""} "
                )
            }
            Span({
                style {
                    if (isChannelDescHovered.value) {
                        cursor(Cursor.Pointer)
                        textDecorationLine(TextDecorationLine.Underline)
                    }
                }
                onMouseEnter { isChannelDescHovered.value = true }
                onMouseLeave { isChannelDescHovered.value = false }
                onClick {
                    isChannelDescExpanded.value = !isChannelDescExpanded.value
                }
            }) {
                Text(if (isChannelDescExpanded.value) "less" else "more")
            }
        }
    }
}

@Composable
private fun SocialLinks() {
    val isSmallBreakpoint by rememberIsSmallBreakpoint()

    Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(16.px)) {
        SpacedRow(spacePx = 18, modifier = Modifier.margin(21.px).zIndex(1)) {
            repeat(4) { Image(src = Assets.Icons.USER_AVATAR, width = 24, height = 24) }
            Box(
                modifier = Modifier
                    .border(1.px, LineStyle.Solid, Styles.WHITE.copyf(alpha = 0.24f))
                    .borderRadius(100.px)
                    .clip(Circle())
                    .size(24.px),
                contentAlignment = Alignment.Center,
            ) {
                TextBox(
                    modifier = Modifier.margin(right = 1.px),
                    text = "+4",
                    color = Styles.WHITE.copyf(alpha = 0.7f),
                    size = 13
                )
            }
        }

        // TODO: Replace with dedicated subscribe button (and use it in the whole codebase)
        Box(
            modifier = Modifier.thenIf(isSmallBreakpoint) { Modifier.align(Alignment.Start) }
        ) {
            AssetSvgButton(
                id = "subscribe_button",
                onClick = {},
                startIconPath = Assets.Paths.NOTIFS,
                text = "Subscribed",
            )
        }
    }
}

private enum class Tab {
    Home, Videos, Shorts, Posts, Playlists, Channels, About
}
