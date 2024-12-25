package com.google.youtube

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.youtube.utils.Styles
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.fontFamily
import com.varabyte.kobweb.compose.ui.modifiers.minHeight
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.silk.SilkApp
import com.varabyte.kobweb.silk.components.layout.Surface
import com.varabyte.kobweb.silk.init.InitSilk
import com.varabyte.kobweb.silk.init.InitSilkContext
import com.varabyte.kobweb.silk.init.registerStyleBase
import com.varabyte.kobweb.silk.style.breakpoint.BreakpointSizes
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import kotlinx.browser.document
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh

@InitSilk
fun overrideRootHTMLStyle(ctx: InitSilkContext) {
    ctx.stylesheet.registerStyleBase("html") {
        Modifier.background(Styles.BACKGROUND)
    }
}

@InitSilk
fun initializeBreakpoints(ctx: InitSilkContext) {
    ctx.theme.breakpoints = BreakpointSizes(
        sm = 576.px,
        md = 768.px,
        lg = 992.px,
        xl = 1200.px,
    )
}

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    LaunchedEffect(Unit) {
        // Disable drag/drop globally
        document.apply {
            addEventListener("dragstart", { event -> event.preventDefault() })
            addEventListener("drop", { event -> event.preventDefault() })
            addEventListener("dragover", { event -> event.preventDefault() })
        }
    }

    SilkApp {
        Surface(
            modifier = Modifier
                .minHeight(100.vh)
                .fontFamily("Roboto")
                .background(Styles.BACKGROUND),
            colorModeOverride = ColorMode.DARK,
        ) {
            content()
        }
    }
}
