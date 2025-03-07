package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import com.google.youtube.utils.Assets
import com.google.youtube.utils.SpacedColumn
import com.google.youtube.utils.TextBox
import com.varabyte.kobweb.compose.css.TextAlign
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.silk.components.graphics.Image

@Composable
fun WorkInProgressSection(modifier: Modifier = Modifier) {
    SpacedColumn(
        spacePx = 16,
        modifier = Modifier.fillMaxWidth().then(modifier),
        centerContentHorizontally = true,
    ) {
        Image(src = Assets.Icons.INFO, width = 100, height = 100)
        TextBox(
            text = "This screen is a work-in-progress at the moment. Please check-in again later.",
            size = 24,
            textAlign = TextAlign.Center,
        )
    }
}
