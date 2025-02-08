package com.google.youtube.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.utils.animatedColor
import com.google.youtube.utils.clickable
import com.google.youtube.utils.rememberMouseEventAsState
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.dom.ref
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.theme.shapes.Circle
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.w3c.dom.Element
import com.varabyte.kobweb.compose.ui.graphics.Color.Rgb as KobwebColor

@Composable
fun AssetImageButton(
    asset: String,
    modifier: Modifier = Modifier,
    containerColor: KobwebColor = Colors.Transparent,
    onClick: (() -> Unit)? = null,
) {
    var buttonRef by remember { mutableStateOf<Element?>(null) }
    val animatedBackgroundColor by rememberMouseEventAsState(buttonRef).animatedColor(initialColor = containerColor)

    Image(
        ref = ref { e -> buttonRef = e },
        modifier = Modifier
            .thenIf(onClick != null) { Modifier.background(animatedBackgroundColor.toKobwebColor()) }
            .borderRadius(100.percent)
            .clickable(onClick)
            .clip(Circle())
            .padding(8.px)
            .then(modifier),
        src = asset,
    )
}
