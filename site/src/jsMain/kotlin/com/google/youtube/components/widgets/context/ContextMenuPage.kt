package com.google.youtube.components.widgets.context

import androidx.compose.runtime.MutableState
import com.google.youtube.components.widgets.ContextMenuChild
import com.google.youtube.components.widgets.player.VideoQuality
import com.google.youtube.utils.Asset

object ContextMenuPage {
    fun videoOptionsMain(onMoreOptionsClick: () -> Unit): List<ContextMenuChild> = listOf(
        ContextMenuChild.ListItem(
            text = "More Options",
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(path = Asset.Path.FILTER, hasHalfOpacity = true),
            trailingContent = ContextMenuChild.ListItem.TrailingContent.Arrow,
            isTopItem = true,
            onClick = onMoreOptionsClick,
        ),
        ContextMenuChild.HorizontalDivider,
        ContextMenuChild.VerticalSpacer,
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(path = Asset.Path.SLEEP_TIMER, hasHalfOpacity = true),
            text = "Sleep Timer",
            trailingContent = ContextMenuChild.ListItem.TrailingContent.Text(value = "OFF"),
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(path = Asset.Path.CC, hasHalfOpacity = true),
            text = "Subtitles",
            trailingContent = ContextMenuChild.ListItem.TrailingContent.Text(value = "OFF"),
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(path = Asset.Path.SPEED, hasHalfOpacity = true),
            text = "Playback speed",
            trailingContent = ContextMenuChild.ListItem.TrailingContent.Text(value = "1x"),
        ),
        ContextMenuChild.VerticalSpacer,
    )

    fun videoOptionsMore(
        annotationSwitchState: MutableState<Boolean>,
        stickyOnScrollState: MutableState<Boolean>,
        stableVolumeState: MutableState<Boolean>,
        ambientModeState: MutableState<Boolean>,
        onBackClick: () -> Unit,
    ): List<ContextMenuChild> = listOf(
        ContextMenuChild.ListItem(
            text = "Back",
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(path = Asset.Path.ARROW_LEFT, hasHalfOpacity = true),
            isTopItem = true,
            onClick = onBackClick,
        ),
        ContextMenuChild.HorizontalDivider,
        ContextMenuChild.VerticalSpacer,
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(path = Asset.Path.AUDIO_CHANNEL, hasHalfOpacity = true),
            text = "Audio Channel",
            trailingContent = ContextMenuChild.ListItem.TrailingContent.Text(value = "Stereo"),
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(path = Asset.Path.ANNOTATIONS, hasHalfOpacity = true),
            text = "Annotations",
            trailingContent = ContextMenuChild.ListItem.TrailingContent.Switch(
                isSelected = annotationSwitchState.value,
            ),
            onClick = { annotationSwitchState.value = !annotationSwitchState.value },
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(path = Asset.Path.STICKY, hasHalfOpacity = true),
            text = "Sticky on scroll",
            trailingContent = ContextMenuChild.ListItem.TrailingContent.Switch(
                isSelected = stickyOnScrollState.value,
            ),
            onClick = { stickyOnScrollState.value = !stickyOnScrollState.value },
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(path = Asset.Path.STABLE_AUDIO, hasHalfOpacity = true),
            text = "Stable volume",
            trailingContent = ContextMenuChild.ListItem.TrailingContent.Switch(
                isSelected = stableVolumeState.value,
            ),
            onClick = { stableVolumeState.value = !stableVolumeState.value },
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(path = Asset.Path.AMBIENT_MODE, hasHalfOpacity = true),
            text = "Ambient mode",
            trailingContent = ContextMenuChild.ListItem.TrailingContent.Switch(
                isSelected = ambientModeState.value,
            ),
            onClick = { ambientModeState.value = !ambientModeState.value },
        ),
        ContextMenuChild.VerticalSpacer,
    )

    fun videoQuality(qualityState: MutableState<VideoQuality>): List<ContextMenuChild> = listOf(
        ContextMenuChild.ListItem(text = "Video Quality", isTopItem = true),
        ContextMenuChild.HorizontalDivider,
        ContextMenuChild.VerticalSpacer,
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(
                    path = if (qualityState.value == VideoQuality.P4K) Asset.Path.CHECK else "",
                    hasHalfOpacity = true,
                ),
            text = "2160p",
            onClick = { qualityState.value = VideoQuality.P4K },
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(
                    path = if (qualityState.value == VideoQuality.P1080) Asset.Path.CHECK else "",
                    hasHalfOpacity = true,
                ),
            text = "1080p",
            onClick = { qualityState.value = VideoQuality.P1080 },
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(
                    path = if (qualityState.value == VideoQuality.P720) Asset.Path.CHECK else "",
                    hasHalfOpacity = true,
                ),
            text = "720p",
            onClick = { qualityState.value = VideoQuality.P720 },
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(
                    path = if (qualityState.value == VideoQuality.P480) Asset.Path.CHECK else "",
                    hasHalfOpacity = true,
                ),
            text = "480p",
            onClick = { qualityState.value = VideoQuality.P480 },
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(
                    path = if (qualityState.value == VideoQuality.P360) Asset.Path.CHECK else "",
                    hasHalfOpacity = true,
                ),
            text = "360p",
            onClick = { qualityState.value = VideoQuality.P360 },
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(
                    path = if (qualityState.value == VideoQuality.P240) Asset.Path.CHECK else "",
                    hasHalfOpacity = true,
                ),
            text = "240p",
            onClick = { qualityState.value = VideoQuality.P240 },
        ),
        ContextMenuChild.ListItem(
            leadingContent = ContextMenuChild.ListItem
                .LeadingContent
                .Asset(
                    path = if (qualityState.value == VideoQuality.P144) Asset.Path.CHECK else "",
                    hasHalfOpacity = true,
                ),
            text = "144p",
            onClick = { qualityState.value = VideoQuality.P144 },
        ),
        ContextMenuChild.VerticalSpacer,
    )

    fun subscription(
        isPostsSelected: Boolean,
        isVideosSelected: Boolean,
        isLiveSelected: Boolean,
        onPostsClick: () -> Unit,
        onVideosClick: () -> Unit,
        onLiveClick: () -> Unit,
        onUnsubscribeClick: () -> Unit,
    ): List<ContextMenuChild> = listOf(
        ContextMenuChild.ListItem(
            text = "Posts",
            leadingContent = ContextMenuChild.ListItem.LeadingContent.Check(isPostsSelected),
            onClick = onPostsClick,
        ),
        ContextMenuChild.ListItem(
            text = "Videos",
            leadingContent = ContextMenuChild.ListItem.LeadingContent.Check(isVideosSelected),
            onClick = onVideosClick,
        ),
        ContextMenuChild.ListItem(
            text = "Live",
            leadingContent = ContextMenuChild.ListItem.LeadingContent.Check(isLiveSelected),
            onClick = onLiveClick,
        ),
        ContextMenuChild.VerticalSpacer,
        ContextMenuChild.HorizontalDivider,
        ContextMenuChild.VerticalSpacer,
        ContextMenuChild.ListItem(
            text = "Collection",
            leadingContent = ContextMenuChild.ListItem.LeadingContent.Asset(Asset.Path.COLLECTIONS),
            trailingContent = ContextMenuChild.ListItem.TrailingContent.Arrow,
        ),
        ContextMenuChild.ListItem(
            text = "Unsubscribe",
            leadingContent = ContextMenuChild.ListItem.LeadingContent.Asset(Asset.Path.UNSUBSCRIBE),
            onClick = onUnsubscribeClick,
        ),
    )
}

enum class VideoOptionPage { Main, MoreOptions }
