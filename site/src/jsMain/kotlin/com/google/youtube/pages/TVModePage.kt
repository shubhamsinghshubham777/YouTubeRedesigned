package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.sections.TopBarDefaults
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.utils.Asset
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.PaddingValues
import com.google.youtube.utils.Route
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.clickable
import com.google.youtube.utils.hideScrollBar
import com.google.youtube.utils.limitTextWithEllipsis
import com.google.youtube.utils.noShrink
import com.varabyte.kobweb.compose.css.CSSLengthOrPercentageNumericValue
import com.varabyte.kobweb.compose.css.ObjectFit
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.functions.radialGradient
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.onMouseEnter
import com.varabyte.kobweb.compose.ui.modifiers.onMouseLeave
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.minus
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element
import org.w3c.dom.events.Event

@Composable
fun TVModePage(
    borderRadius: CSSLengthOrPercentageNumericValue = 14.px,
    contentPadding: PaddingValues = PaddingValues(leftRight = 25.px, topBottom = 20.px),
) {
    val navigator = LocalNavigator.current

    var parentContainerRef by remember { mutableStateOf<Element?>(null) }

    var isTitleHovered by remember { mutableStateOf(false) }
    var isChannelNameHovered by remember { mutableStateOf(false) }

    var isCCEnabled by remember { mutableStateOf(false) }
    var isFullScreenEnabled by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener: (Event) -> Unit = { isFullScreenEnabled = document.fullscreen }
        document.addEventListener("fullscreenchange", listener)
        document.addEventListener("mozfullscreenchange", listener)
        document.addEventListener("MSFullscreenChange", listener)
        document.addEventListener("webkitfullscreenchange", listener)
        onDispose {
            document.removeEventListener("fullscreenchange", listener)
            document.removeEventListener("mozfullscreenchange", listener)
            document.removeEventListener("MSFullscreenChange", listener)
            document.removeEventListener("webkitfullscreenchange", listener)
        }
    }

    Box(
        ref = ref { e -> parentContainerRef = e },
        modifier = Modifier
            .fillMaxWidth()
            .minHeight(500.px)
            .height(100.vh - TopBarDefaults.HEIGHT - (if (isFullScreenEnabled) 0.px else 12.px))
            .thenIf(!isFullScreenEnabled) { Modifier.padding(bottom = 12.px) }
    ) {
        Image(
            modifier = Modifier.borderRadius(borderRadius).fillMaxSize().objectFit(ObjectFit.Cover),
            src = Asset.Thumbnail.TV.TV_MODE_THUMBNAIL,
        )

        Box(
            modifier = Modifier
                .backgroundImage(
                    radialGradient {
                        add(Colors.Transparent)
                        setMidpoint(80.percent)
                        add(Colors.Black)
                    }
                )
                .borderRadius(borderRadius)
                .fillMaxSize()
        )

        Column(
            modifier = Modifier.padding(
                left = contentPadding.left,
                top = contentPadding.top,
                right = contentPadding.right,
            ),
            verticalArrangement = Arrangement.spacedBy(16.px)
        ) {
            Row(
                modifier = Modifier.fontSize(20.px),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.px),
            ) {
                Box(modifier = Modifier.opacity(0.5f).noShrink()) {
                    Text("Now Playing:")
                }

                Box(
                    modifier = Modifier
                        .clickable { navigator.pushRoute(Route.Video(id = "m9kd28VOTLK")) }
                        .limitTextWithEllipsis()
                        .onMouseEnter { isTitleHovered = true }
                        .onMouseLeave { isTitleHovered = false }
                        .thenIf(isTitleHovered) {
                            Modifier.textDecorationLine(TextDecorationLine.Underline)
                        },
                ) {
                    Text("I Redesigned the ENTIRE YouTube UI from Scratch")
                }
            }

            SpacedRow(
                spacePx = 15,
                modifier = Modifier
                    .onMouseEnter { isChannelNameHovered = true }
                    .onMouseLeave { isChannelNameHovered = false }
                    .clickable { navigator.pushRoute(Route.Page(id = "juxtopposed")) },
            ) {
                Image(modifier = Modifier.size(28.px), src = Asset.Channel.JUXTOPPOSED)
                SpacedRow(8) {
                    TextBox(
                        modifier = Modifier.thenIf(isChannelNameHovered) {
                            Modifier.textDecorationLine(TextDecorationLine.Underline)
                        },
                        text = "Juxtopposed"
                    )
                    Image(Asset.Icon.VERIFIED_BADGE)
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(
                    left = contentPadding.left,
                    right = contentPadding.right,
                    bottom = contentPadding.bottom
                )
                .overflow { x(Overflow.Scroll) }
                .hideScrollBar(),
            horizontalArrangement = Arrangement.spacedBy(8.px),
        ) {
            Row(
                modifier = Modifier.noShrink(),
                horizontalArrangement = Arrangement.spacedBy(8.px),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AssetImageButton(Asset.Icon.LIST_OF_TOPICS) {}
                AssetImageButton(Asset.Icon.ARROW_LEFT) {}
                Box { Text("Topic 01 - Entertainment") }
                AssetImageButton(Asset.Icon.ARROW_RIGHT) {}
                AssetImageButton(Asset.Icon.VOLUME) {}
            }

            Spacer()

            Row(
                modifier = Modifier.noShrink(),
                horizontalArrangement = Arrangement.spacedBy(8.px),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AssetImageButton(if (isCCEnabled) Asset.Icon.CC_SELECTED else Asset.Icon.CC) {
                    isCCEnabled = !isCCEnabled
                }
                AssetImageButton(Asset.Icon.QUALITY_4K) {}
                AssetImageButton(Asset.Icon.SETTINGS) {}
                AssetImageButton(Asset.Icon.PIP) {}
                AssetImageButton(Asset.Icon.THEATER) {}
                AssetImageButton(
                    if (isFullScreenEnabled) Asset.Icon.FULLSCREEN_SELECTED
                    else Asset.Icon.FULLSCREEN
                ) {
                    if (isFullScreenEnabled) {
                        window.document.exitFullscreen()
                    } else {
                        parentContainerRef?.requestFullscreen()
                    }
                    isFullScreenEnabled = !isFullScreenEnabled
                }
            }
        }
    }
}
