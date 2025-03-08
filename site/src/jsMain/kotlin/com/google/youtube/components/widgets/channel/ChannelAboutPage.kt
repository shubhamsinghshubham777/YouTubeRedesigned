package com.google.youtube.components.widgets.channel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.utils.Asset
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.rememberIsSmallBreakpoint
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.cursor
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.maxWidth
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.thenIfNotNull
import com.varabyte.kobweb.silk.components.graphics.Image
import kotlinx.browser.window
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.vw

@Composable
fun ChannelAboutPage() {
    val isSmallBreakpoint by rememberIsSmallBreakpoint()

    if (!isSmallBreakpoint) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.vw),
        ) {
            DescriptionAndLinks(Modifier.maxWidth(45.percent))
            ChannelDetails()
        }
    } else {
        SpacedColumn(spacePx = 52, modifier = Modifier.fillMaxWidth()) {
            DescriptionAndLinks(Modifier.fillMaxWidth())
            ChannelDetails(Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun DescriptionAndLinks(modifier: Modifier = Modifier) {
    SpacedColumn(spacePx = 52, modifier = modifier) {
        // Description
        SpacedColumn(30) {
            TitleText("Description")
            TextBox(
                text = "subscribe for more cats (and design content, of course)\u2028\u2028this " +
                        "is my only channel on YouTube. if you’d like to contact me, use the " +
                        "contact email in channel details.",
                lineHeight = 25,
            )
        }

        // Links
        SpacedColumn(30) {
            TitleText("Links")
            Link(
                imageSrc = Asset.Social.TWITTER,
                label = "x dot com //tweet me something",
                url = "x.com/juxtopposed",
            )
            Link(
                imageSrc = Asset.Social.JUXTOPPOSED,
                label = "personal site //links n stuff",
                url = "juxtopposed.com",
            )
            Link(
                imageSrc = Asset.Social.REALTIME_COLORS,
                label = "Realtime Colors //color design",
                url = "realtimecolors.com",
            )
            Link(
                imageSrc = Asset.Social.FIGMA,
                label = "figma //design files",
                url = "figma.com/@juxtopposed",
            )
            Link(
                imageSrc = Asset.Social.CODEPEN,
                label = "codepen //check my code",
                url = "codepen.io/Juxtopposed",
            )
        }
    }
}

@Composable
private fun Link(imageSrc: String, label: String, url: String) {
    var isUrlHovered by remember { mutableStateOf(false) }

    SpacedRow(29) {
        Image(src = imageSrc, width = 24, height = 24)
        SpacedColumn(6) {
            TextBox(label)
            TextBox(
                modifier = Modifier
                    .onMouseEnter { isUrlHovered = true }
                    .onMouseLeave { isUrlHovered = false }
                    .onClick { window.open("https://$url") }
                    .thenIf(isUrlHovered) {
                        Modifier
                            .cursor(Cursor.Pointer)
                            .textDecorationLine(TextDecorationLine.Underline)
                    },
                text = url,
                color = Styles.LINK_BLUE
            )
        }
    }
}

@Composable
private fun ChannelDetails(modifier: Modifier = Modifier) {
    var isEmailHidden by remember { mutableStateOf(true) }

    SpacedColumn(spacePx = 30, modifier = modifier) {
        TitleText("Channel Details")
        SpacedColumn(20) {
            ListItem(
                asset = Asset.Icon.MAIL,
                label = if (isEmailHidden) "Click to view" else "juxtopposed@gmail.com",
                onClick = { isEmailHidden = !isEmailHidden },
            )
            ListItem(asset = Asset.Icon.EYE, label = "10,631,163 views")
            ListItem(asset = Asset.Icon.INFO, label = "Joined August 25, 2022")
            ListItem(asset = Asset.Icon.BROWSER, label = "United States")
            ListItem(
                asset = Asset.Icon.LINK,
                label = "www.youtube.com/@juxtopposed",
                onClick = { window.open("https://www.youtube.com/@juxtopposed") },
            )
            AssetSvgButton(
                id = "share_channel_button",
                startIconPath = Asset.Path.SHARE,
                text = "Share Channel",
                onClick = {},
            )
        }
    }
}

@Composable
private fun ListItem(asset: String, label: String, onClick: (() -> Unit)? = null) {
    var isUrlHovered by remember { mutableStateOf(false) }

    SpacedRow(16) {
        Image(modifier = Modifier.opacity(0.5), src = asset, width = 24, height = 24)
        TextBox(
            modifier = Modifier
                .thenIfNotNull(onClick) { safeOnClick ->
                    Modifier
                        .onMouseEnter { isUrlHovered = true }
                        .onMouseLeave { isUrlHovered = false }
                        .onClick { safeOnClick() }
                        .thenIf(isUrlHovered) {
                            Modifier
                                .cursor(Cursor.Pointer)
                                .textDecorationLine(TextDecorationLine.Underline)
                        }
                },
            text = label
        )
    }
}

@Composable
private fun TitleText(text: String) {
    TextBox(text = text, weight = FontWeight.SemiBold, size = 18)
}
