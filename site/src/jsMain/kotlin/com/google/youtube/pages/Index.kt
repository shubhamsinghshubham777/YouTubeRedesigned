package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.google.youtube.components.layouts.MainLayout
import com.google.youtube.utils.LocalNavigator
import com.google.youtube.utils.Navigator
import com.google.youtube.utils.Route
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext
import kotlinx.browser.window

@Page("{...path?}")
@Composable
fun IndexPage() {
    val context = rememberPageContext()
    val route = remember(context.route.params) {
        Route.valueOf(context.route.params.getValue("path"))
    }
    if (route == null) window.history.replaceState(null, "", Route.Home.path)
    CompositionLocalProvider(LocalNavigator provides Navigator(route ?: Route.Home)) {
        MainLayout()
    }
}
