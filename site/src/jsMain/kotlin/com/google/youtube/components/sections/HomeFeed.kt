package com.google.youtube.components.sections

import androidx.compose.runtime.Composable
import com.google.youtube.components.widgets.MissedVideosContainer
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth

@Composable
fun HomeFeed(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        MissedVideosContainer()
    }
}
