package com.google.youtube.components.widgets.comments

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.AssetSvgButton
import com.google.youtube.components.widgets.AssetSvgButtonType
import com.google.youtube.components.widgets.context.RoundedSearchTextField
import com.google.youtube.data.VideoPlayerDataProvider
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.SpacedRow
import com.google.youtube.utils.Styles
import com.google.youtube.utils.TextBox
import com.google.youtube.utils.hideScrollBar
import com.google.youtube.utils.rememberIsSmallBreakpoint
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.ScrollBehavior
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.scrollBehavior
import org.jetbrains.compose.web.css.px
import kotlin.math.roundToInt

@Composable
fun CommentsSection(
    videoId: String,
    modifier: Modifier = Modifier,
) {
    val isSmallBreakpoint by rememberIsSmallBreakpoint()
    var selectedCommentType by remember { mutableStateOf(COMMENT_TYPES.first()) }
    val searchQueryState = remember { mutableStateOf("") }

    // Data States
    val videoPlayerDataProvider = remember { VideoPlayerDataProvider() }
    val comments = remember(videoPlayerDataProvider, videoId) {
        videoPlayerDataProvider.getCommentsForId(videoId)
    }

    SpacedColumn(spacePx = 24, modifier = modifier) {
        // Title
        SpacedRow(8) {
            TextBox(
                text = "Comments",
                size = 18,
                weight = FontWeight.Medium,
            )
            TextBox(
                text = comments.sumOf { it.replies.size }.plus(comments.size).toString(),
                size = 18,
                color = Styles.WHITE.copyf(alpha = 0.6f),
            )
        }

        // Filters & Search Bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(right = 8.px),
            horizontalArrangement = Arrangement.spacedBy(8.px),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                SpacedRow(
                    spacePx = FILTERS_GAP,
                    modifier = Modifier
                        .hideScrollBar()
                        .overflow { x(Overflow.Scroll) }
                        .scrollBehavior(ScrollBehavior.Smooth),
                ) {
                    COMMENT_TYPES.forEach { type ->
                        AssetSvgButton(
                            id = "${type}_comment_type_filter",
                            onClick = { selectedCommentType = type },
                            text = type,
                            type = AssetSvgButtonType.FilterChip,
                            isSelected = type == selectedCommentType,
                        )
                    }
                }
            }
            Spacer()
            RoundedSearchTextField(
                textState = searchQueryState,
                hintText = if (isSmallBreakpoint) "Search" else "Search comments",
                widthPx = 314f.times(if (isSmallBreakpoint) 0.5f else 1f).roundToInt(),
            )
        }

        // Comments
        SpacedColumn(spacePx = 25, modifier = Modifier.fillMaxWidth().padding(20.px)) {
            comments.forEach { commentData -> CommentItem(data = commentData) }
        }
    }
}

private val COMMENT_TYPES = listOf("Top", "Most Liked", "Newest", "Timed", "Topics")
private const val FILTERS_GAP = 8
