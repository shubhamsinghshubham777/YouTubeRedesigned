package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import com.google.youtube.components.sections.HomeFeed
import com.google.youtube.components.widgets.FilterRow
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.px

@Composable
fun HomePage(
    showPersonalisedFeedDialogState: MutableState<Boolean>,
    horizontalPaddingState: State<Float>,
) {
    Column {
        FilterRow(showPersonalisedFeedDialogState = showPersonalisedFeedDialogState)
        HomeFeed(modifier = Modifier.padding(top = 27.px, right = horizontalPaddingState.value.px))
    }
}
