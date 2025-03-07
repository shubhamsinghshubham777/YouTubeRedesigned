package com.google.youtube.components.widgets.channel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.models.ChannelListItemData
import com.google.youtube.utils.Assets
import com.google.youtube.utils.BasicGrid
import com.google.youtube.utils.Constants
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.GridGap
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.isGreaterThan
import com.google.youtube.utils.rememberBreakpointAsState
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px

@Composable
fun ChannelPostsPage() {
    val filterState = remember { mutableStateOf(VideosAndPostsFilter.Latest) }
    val layoutTypeState = remember { mutableStateOf(VideosAndPostsLayoutType.Grid) }

    SpacedColumn(spacePx = 28, modifier = Modifier.fillMaxWidth()) {
        VideosAndPostsFilters(filterState, layoutTypeState)
        Crossfade(
            targetState = layoutTypeState.value,
            modifier = Modifier.fillMaxWidth(),
        ) { animatedLayoutType ->
            when (animatedLayoutType) {
                VideosAndPostsLayoutType.Grid -> PostsGrid(posts = remember { SAMPLE_POSTS })
                VideosAndPostsLayoutType.List -> PostsList(posts = remember { SAMPLE_POSTS })
            }
        }
    }
}

@Composable
private fun PostsList(posts: List<ChannelListItemData.Post>) {
    SpacedColumn(spacePx = 20, modifier = Modifier.fillMaxWidth()) {
        posts.forEach { data ->
            OutlinedPost(
                data = data,
                modifier = Modifier.fillMaxWidth(),
                onComment = {},
                onDislike = {},
                onLike = {},
                onShare = {},
                showImageOnBottom = true,
            )
        }
    }
}

@Composable
private fun PostsGrid(posts: List<ChannelListItemData.Post>) {
    val breakpoint by rememberBreakpointAsState()
    BasicGrid(
        modifier = Modifier.fillMaxWidth().display(DisplayStyle.Grid),
        columnBuilder = {
            minmax(
                min = if (breakpoint.isGreaterThan(Breakpoint.MD)) 478.67.px
                else Constants.MOBILE_MAX_AVAILABLE_WIDTH.px,
                max = 1.fr
            )
        },
        gridGap = GridGap(17.px, 17.px),
    ) {
        posts.forEach { data ->
            OutlinedPost(
                modifier = Modifier.fillMaxWidth().height(289.px),
                data = data,
                onLike = {},
                onDislike = {},
                onComment = {},
                onShare = {},
            )
        }
    }
}

private val SAMPLE_POSTS = List(10) { index ->
    ChannelListItemData.Post(
        id = index.toString(),
        channelAsset = Assets.Icons.USER_AVATAR,
        channelName = "Juxtopposed",
        daysSinceUploaded = "1 day",
        isChannelVerified = true,
        subscribersCount = "295K",
        commentCount = "1.1K",
        dislikeCount = "12",
        likeCount = "2.9K",
        message = "it’s finally time for youtube. what are your biggest issues with it?",
        postAsset = Assets.Banners.BANNER_1,
    )
}
