package com.google.youtube.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.google.youtube.components.widgets.FilterRow
import com.varabyte.kobweb.compose.foundation.layout.Column

@Composable
fun HomePage(showPersonalisedFeedDialogState: MutableState<Boolean>) {
    Column {
        FilterRow(showPersonalisedFeedDialogState = showPersonalisedFeedDialogState)
    }
}
