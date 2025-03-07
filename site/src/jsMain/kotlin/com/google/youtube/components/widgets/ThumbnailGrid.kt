package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.IntSize
import com.google.youtube.models.VideoThumbnailDetails
import com.google.youtube.utils.BasicGrid
import com.google.youtube.utils.Constants
import com.google.youtube.utils.GridGap
import com.google.youtube.utils.Styles
import com.google.youtube.utils.isGreaterThan
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.backgroundImage
import com.varabyte.kobweb.compose.ui.modifiers.borderLeft
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.translateX
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.div
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.unaryMinus
import org.jetbrains.compose.web.dom.Text

@Composable
fun ThumbnailGrid(date: String, thumbnailDetails: List<VideoThumbnailDetails>) {
    val breakpoint = rememberBreakpoint()
    val thumbnailWidth = remember(breakpoint) {
        when {
            breakpoint.isGreaterThan(Breakpoint.SM) -> 341
            else -> Constants.MOBILE_MAX_AVAILABLE_WIDTH - CONTAINER_PADDING_LEFT
        }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(left = DOT_INDICATOR_SIZE / 3)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(21.px),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DotIndicator(
                modifier = Modifier.translateX(-DOT_INDICATOR_SIZE / 3),
                isActive = date.contains("Today")
            )
            Box { Text(date) }
        }

        BasicGrid(
            modifier = Modifier
                .borderLeft(3.px, LineStyle.Solid, Styles.DIVIDER)
                .fillMaxWidth()
                .padding(left = CONTAINER_PADDING_LEFT.px, top = 24.px, bottom = 72.px),
            gridGap = GridGap(x = 19.px),
            columnBuilder = { size(thumbnailWidth.px) },
        ) {
            thumbnailDetails.forEach { details ->
                VideoThumbnailCard(
                    details = details,
                    size = IntSize(width = thumbnailWidth, height = 190),
                )
            }
        }
    }
}

@Composable
private fun DotIndicator(isActive: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .backgroundImage(Styles.Gradient.RED_TO_PINK)
            .clip(Circle())
            .size(DOT_INDICATOR_SIZE)
            .then(modifier),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.background(Styles.WHITE.copyf(alpha = 0.3f)).fillMaxSize())
        if (!isActive) Box(Modifier.background(Styles.SURFACE).clip(Circle()).size(6.px))
    }
}

private val DOT_INDICATOR_SIZE = 9.px
private const val CONTAINER_PADDING_LEFT = 21
