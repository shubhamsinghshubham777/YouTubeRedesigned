package com.google.youtube.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlin.coroutines.CoroutineContext

@Stable
class Navigator(private val initialRoute: Route) : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    private val currentRouteFlow = MutableStateFlow(arrayOf(initialRoute))

    val currentRouteState: State<Route>
        @Composable get() = currentRouteFlow
            .map { routes -> routes.last() }
            .collectAsState(initialRoute)

    fun pushRoute(route: Route) {
        if (currentRouteFlow.value.last() == route) {
            log("Provided route is same as the current route. Push event ignored.")
            return
        }
        window.history.pushState(null, "", route.path)
        currentRouteFlow.update { routes -> arrayOf(*routes, route) }
    }

    fun pop() {
        if (currentRouteFlow.value.size <= 1) {
            log("Backstack doesn't have enough routes to pop! Pop request ignored.")
            return
        }
        window.history.back()
        currentRouteFlow.update { routes -> routes.sliceArray(0..<routes.lastIndex) }
    }
}

val LocalNavigator = compositionLocalOf<Navigator> {
    error("Please override the compositionLocal with a Navigator object!")
}

sealed class Route(val path: String) {
    data object Home : Route(path = HOME)
    data object Explore : Route(path = EXPLORE)
    data object Shorts : Route(path = SHORTS)
    data class Short(val id: String) : Route(path = "$SHORTS/$id")
    data object History : Route(path = HISTORY)
    data object TVMode : Route(path = TV_MODE)
    data object WatchLater : Route(path = WATCH_LATER)
    data object LikedVideos : Route(path = LIKED_VIDEOS)
    data object Playlists : Route(path = PLAYLISTS)
    data class Playlist(val id: String) : Route(path = "$PLAYLIST$id")
    data object Collections : Route(path = COLLECTIONS)
    data class Collection(val id: String) : Route(path = "$COLLECTION$id")
    data object Subscriptions : Route(path = SUBSCRIPTIONS)
    data class Page(val id: String) : Route(path = "$PAGE$id")
    data class Video(val id: String) : Route(path = "$VIDEO$id")

    companion object {
        fun valueOf(path: String): Route? {
            return when {
                path == HOME || path.isEmpty() -> Home
                path.contains(EXPLORE) -> Explore
                path == SHORTS -> Shorts
                path.contains(SHORTS) -> Short(id = path.substringAfterLast('/'))
                path.contains(HISTORY) -> History
                path.contains(TV_MODE) -> TVMode
                path.contains(WATCH_LATER) -> WatchLater
                path.contains(LIKED_VIDEOS) -> LikedVideos
                path.contains(PLAYLISTS) -> Playlists
                path.contains(PLAYLIST) -> Playlist(id = path.substringAfterLast('='))
                path.contains(COLLECTIONS) -> Collections
                path.contains(COLLECTION) -> Collection(id = path.substringAfterLast('='))
                path.contains(SUBSCRIPTIONS) -> Subscriptions
                path.contains(PAGE) -> Page(id = path.substringAfterLast('@'))
                path.contains(VIDEO) -> Video(id = path.substringAfterLast('='))
                else -> null
            }
        }
    }
}

private const val HOME = "/"
private const val EXPLORE = "/feed/trending"
private const val SHORTS = "/shorts"
private const val HISTORY = "/feed/history"
private const val TV_MODE = "/tv_mode"
private const val WATCH_LATER = "/playlist?list=WL"
private const val LIKED_VIDEOS = "/playlist?list=LL"
private const val PLAYLISTS = "/feed/playlists"
private const val PLAYLIST = "/playlist?list="
private const val COLLECTIONS = "/collections"
private const val COLLECTION = "/collection?list="
private const val SUBSCRIPTIONS = "/feed/subscriptions"
private const val PAGE = "/@"
private const val VIDEO = "/watch?v="

private fun log(message: String) = println("$TAG - $message")

private const val TAG = "Navigator"
