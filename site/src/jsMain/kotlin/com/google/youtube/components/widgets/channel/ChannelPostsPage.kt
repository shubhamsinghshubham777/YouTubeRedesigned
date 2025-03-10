package com.google.youtube.components.widgets.channel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.youtube.data.ChannelDataProvider
import com.google.youtube.models.ChannelListItemData
import com.google.youtube.utils.BasicGrid
import com.google.youtube.utils.Constants
import com.google.youtube.utils.Crossfade
import com.google.youtube.utils.DescendingStringDurationComparator
import com.google.youtube.utils.GridGap
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.StringDurationComparator
import com.google.youtube.utils.StringViewCountComparator
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
    val channelDataProvider = remember { ChannelDataProvider() }
    val posts = remember(channelDataProvider) { channelDataProvider.getAllPosts() }
    val pinnedPosts = remember(posts) { posts.filter { it.isPinned } }
    val unpinnedPosts = remember(posts) { posts.filterNot { it.isPinned } }

    SpacedColumn(spacePx = 28, modifier = Modifier.fillMaxWidth()) {
        VideosAndPostsFilters(filterState, layoutTypeState)
        Crossfade(
            targetState = filterState.value,
            modifier = Modifier.fillMaxWidth(),
        ) { animatedFilter ->
            val sortedPinnedPosts = when (animatedFilter) {
                VideosAndPostsFilter.Latest ->
                    pinnedPosts.sortedWith(PostDurationComparator)

                VideosAndPostsFilter.Popular ->
                    pinnedPosts.sortedWith(PostLikeCountComparator)

                VideosAndPostsFilter.Oldest ->
                    pinnedPosts.sortedWith(DescendingPostDurationComparator)
            }

            val sortedUnpinnedPosts = when (animatedFilter) {
                VideosAndPostsFilter.Latest ->
                    unpinnedPosts.sortedWith(PostDurationComparator)

                VideosAndPostsFilter.Popular ->
                    unpinnedPosts.sortedWith(PostLikeCountComparator)

                VideosAndPostsFilter.Oldest ->
                    unpinnedPosts.sortedWith(DescendingPostDurationComparator)
            }

            val sortedPosts = sortedPinnedPosts + sortedUnpinnedPosts

            Crossfade(
                targetState = layoutTypeState.value,
                modifier = Modifier.fillMaxWidth(),
            ) { animatedLayoutType ->
                when (animatedLayoutType) {
                    VideosAndPostsLayoutType.Grid -> PostsGrid(sortedPosts)
                    VideosAndPostsLayoutType.List -> PostsList(sortedPosts)
                }
            }
        }
    }
}

private object PostDurationComparator : Comparator<ChannelListItemData.Post> {
    override fun compare(a: ChannelListItemData.Post, b: ChannelListItemData.Post): Int {
        return StringDurationComparator.compare(a.daysSinceUploaded, b.daysSinceUploaded)
    }
}

private object DescendingPostDurationComparator : Comparator<ChannelListItemData.Post> {
    override fun compare(a: ChannelListItemData.Post, b: ChannelListItemData.Post): Int {
        return DescendingStringDurationComparator.compare(a.daysSinceUploaded, b.daysSinceUploaded)
    }
}

private object PostLikeCountComparator : Comparator<ChannelListItemData.Post> {
    override fun compare(a: ChannelListItemData.Post, b: ChannelListItemData.Post): Int {
        return StringViewCountComparator.compare(a.likeCount, b.likeCount)
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
