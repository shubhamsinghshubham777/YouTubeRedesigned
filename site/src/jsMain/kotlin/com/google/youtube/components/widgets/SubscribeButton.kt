package com.google.youtube.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.components.widgets.context.ContextMenuPage
import com.google.youtube.utils.Asset
import com.google.youtube.utils.Styles
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.silk.components.overlay.Popover
import com.varabyte.kobweb.silk.components.overlay.PopupPlacement
import org.jetbrains.compose.web.css.px
import org.w3c.dom.HTMLElement

@Composable
fun SubscribeButton(
    initialIsSubscribed: Boolean = false,
    showPopup: Boolean = true,
    modifier: Modifier = Modifier,
    popoverModifier: Modifier = Modifier,
    popupPlacement: PopupPlacement = PopupPlacement.BottomLeft,
) {
    var elementRef by remember { mutableStateOf<HTMLElement?>(null) }
    val subscribedTypes = remember { mutableStateListOf<SubscriptionType>() }
    var isSubscribed by remember { mutableStateOf(initialIsSubscribed) }
    val startIconPath by remember {
        derivedStateOf {
            when {
                !isSubscribed -> null
                subscribedTypes.containsAll(SubscriptionType.entries) -> Asset.Path.NOTIFS_SELECTED
                subscribedTypes.isNotEmpty() -> Asset.Path.NOTIFS
                else -> Asset.Path.NOTIFS_DISABLED
            }
        }
    }
    val animatedContainerColor by animateColorAsState(
        if (isSubscribed) Styles.WHITE.copyf(alpha = 0.08f).toComposeColor()
        else Styles.PINK_DARKENED.toComposeColor()
    )

    AssetSvgButton(
        ref = ref { e -> elementRef = e },
        modifier = modifier,
        id = "subscribe_button",
        text = if (isSubscribed) "Subscribed" else "Subscribe",
        containerColor = animatedContainerColor.toKobwebColor(),
        contentColor = Styles.WHITE,
        startIconPath = startIconPath,
        onClick = {
            if (showPopup) {
                if (!isSubscribed) {
                    subscribedTypes.addAll(SubscriptionType.entries)
                    isSubscribed = true
                }
            } else {
                isSubscribed = !isSubscribed
            }
        },
    )

    if (showPopup && isSubscribed && elementRef != null) {
        Popover(
            modifier = popoverModifier,
            target = ElementTarget.of(elementRef!!),
            hideDelayMs = 100,
            placement = popupPlacement,
        ) {
            ContextMenu(modifier = Modifier.width(216.px), state = Unit) {
                ContextMenuPage.subscription(
                    isPostsSelected = subscribedTypes.contains(SubscriptionType.Posts),
                    isVideosSelected = subscribedTypes.contains(SubscriptionType.Videos),
                    isLiveSelected = subscribedTypes.contains(SubscriptionType.Live),
                    onPostsClick = {
                        if (subscribedTypes.contains(SubscriptionType.Posts)) {
                            subscribedTypes.remove(SubscriptionType.Posts)
                        } else {
                            subscribedTypes.add(SubscriptionType.Posts)
                        }
                    },
                    onVideosClick = {
                        if (subscribedTypes.contains(SubscriptionType.Videos)) {
                            subscribedTypes.remove(SubscriptionType.Videos)
                        } else {
                            subscribedTypes.add(SubscriptionType.Videos)
                        }
                    },
                    onLiveClick = {
                        if (subscribedTypes.contains(SubscriptionType.Live)) {
                            subscribedTypes.remove(SubscriptionType.Live)
                        } else {
                            subscribedTypes.add(SubscriptionType.Live)
                        }
                    },
                    onUnsubscribeClick = {
                        subscribedTypes.clear()
                        isSubscribed = false
                    },
                )
            }
        }
    }
}

private enum class SubscriptionType { Posts, Videos, Live }
