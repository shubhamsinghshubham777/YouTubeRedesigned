package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.google.youtube.components.layouts.MainLayout
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.Navigator
import com.google.youtube.utils.Route
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.window

@Page("404")
@Composable
fun FallbackPage() {
    IndexPage()
}

@Page("{...path?}")
@Composable
fun IndexPage() {
    val context = rememberPageContext()

    if (!::navigator.isInitialized) {
        val route = Route.valueOf(context.route.pathQueryAndFragment)
        navigator = Navigator(initialRoute = route ?: Route.Home)
        if (route == null) window.history.replaceState(null, "", Route.Home.path)
    }

    CompositionLocalProvider(LocalNavigator provides navigator) {
        MainLayout()
    }
}

private lateinit var navigator: Navigator
