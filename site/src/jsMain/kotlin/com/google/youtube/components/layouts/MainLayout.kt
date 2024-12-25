package com.google.youtube.components.layouts

import androidx.compose.runtime.Composable
import com.google.youtube.components.sections.NavRail
import com.google.youtube.components.sections.TopBar
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.width
import org.jetbrains.compose.web.css.px

@Composable
fun MainLayout() {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar()
        Row(modifier = Modifier.fillMaxSize()) {
            NavRail(modifier = Modifier.width(239.px))
            Box(
                modifier = Modifier
                    .background(Colors.Tan)
                    .fillMaxWidth()
                    .weight(1)
            ) {
            }
        }
    }
}
