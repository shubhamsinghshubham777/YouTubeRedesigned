package com.google.youtube.pages

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.utils.Assets
import com.google.youtube.utils.MouseEventState
import com.google.youtube.utils.PaddingValues
import com.google.youtube.utils.clickable
import com.google.youtube.utils.hideScrollBar
import com.google.youtube.utils.isGreaterThan
import com.google.youtube.utils.limitTextWithEllipsis
import com.google.youtube.utils.noShrink
import com.google.youtube.utils.rememberMouseEventAsState
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
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.objectFit
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.textDecorationLine
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.Element

@Composable
fun TVModePage(
    horizontalPaddingState: State<Float>,
    borderRadius: CSSLengthOrPercentageNumericValue = 14.px,
) {
    val breakpoint = rememberBreakpoint()
    val animatedContentPaddingFactor by animateFloatAsState(
        if (breakpoint.isGreaterThan(Breakpoint.SM)) 1f else 0.5f
    )
    val contentPadding by remember {
        derivedStateOf {
            PaddingValues(leftRight = 25.px, topBottom = 20.px) * animatedContentPaddingFactor
        }
    }
    var titleRef by remember { mutableStateOf<Element?>(null) }
    val mouseEventStateForTitle by rememberMouseEventAsState(titleRef)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(95.percent)
            .padding(right = horizontalPaddingState.value.px)
    ) {
        Image(
            modifier = Modifier.borderRadius(borderRadius).fillMaxSize().objectFit(ObjectFit.Cover),
            src = Assets.Thumbnails.THUMBNAIL_1,
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
                    ref = ref { element -> titleRef = element },
                    modifier = Modifier
                        .limitTextWithEllipsis()
                        .thenIf(mouseEventStateForTitle != MouseEventState.Released) {
                            Modifier.textDecorationLine(TextDecorationLine.Underline)
                        }
                        .clickable {}
                ) {
                    Text("I Redesigned the ENTIRE YouTube UI from Scratch")
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(28.px),
                    src = Assets.Avatars.AVATAR_JACKSEPTICEYE,
                )

                Box(modifier = Modifier.margin(left = 16.px, right = 8.px)) {
                    Text("Juxtopposed")
                }

                Image(Assets.Icons.VERIFIED_BADGE)
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
                AssetImageButton(Assets.Icons.LIST_OF_TOPICS) {}
                AssetImageButton(Assets.Icons.ARROW_LEFT) {}
                Box { Text("Topic 01 - Entertainment") }
                AssetImageButton(Assets.Icons.ARROW_RIGHT) {}
                AssetImageButton(Assets.Icons.VOLUME) {}
            }

            Spacer()

            Row(
                modifier = Modifier.noShrink(),
                horizontalArrangement = Arrangement.spacedBy(8.px),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AssetImageButton(Assets.Icons.CC) {}
                AssetImageButton(Assets.Icons.QUALITY_4K) {}
                AssetImageButton(Assets.Icons.SETTINGS) {}
                AssetImageButton(Assets.Icons.PIP) {}
                AssetImageButton(Assets.Icons.THEATER) {}
                AssetImageButton(Assets.Icons.FULLSCREEN) {}
            }
        }
    }
}
