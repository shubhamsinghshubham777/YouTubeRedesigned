package com.google.youtube.components.sections

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.google.youtube.components.widgets.AssetImageButton
import com.google.youtube.components.widgets.AssetSvg
import com.google.youtube.utils.Asset
import com.google.youtube.utils.Constants
import com.google.youtube.utils.Styles
import com.google.youtube.utils.clickable
import com.google.youtube.utils.isGreaterThan
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.browser.dom.ElementTarget
import com.varabyte.kobweb.browser.dom.clearFocus
import com.varabyte.kobweb.compose.css.Background
import com.varabyte.kobweb.compose.css.Cursor
import com.varabyte.kobweb.compose.css.background
import com.varabyte.kobweb.compose.css.color
import com.varabyte.kobweb.compose.css.cursor
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.border
import com.varabyte.kobweb.compose.ui.modifiers.borderRadius
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.modifiers.zIndex
import com.varabyte.kobweb.silk.components.graphics.Image
import com.varabyte.kobweb.silk.components.overlay.Tooltip
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.style.until
import com.varabyte.kobweb.silk.theme.breakpoint.rememberBreakpoint
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.flex
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.marginLeft
import org.jetbrains.compose.web.css.marginRight
import org.jetbrains.compose.web.css.outline
import org.jetbrains.compose.web.css.paddingBottom
import org.jetbrains.compose.web.css.paddingTop
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.TextInput
import org.w3c.dom.HTMLElement

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onLogoClick: () -> Unit,
    onDrawerButtonClick: () -> Unit,
    onOpenNotificationPanel: () -> Unit,
    onSearch: (query: String) -> Unit,
) {
    var addBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var notificationBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var settingsBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var accountBtnRef by remember { mutableStateOf<HTMLElement?>(null) }
    var textInputRef by remember { mutableStateOf<HTMLElement?>(null) }

    val breakpoint = rememberBreakpoint()

    var searchQuery by remember { mutableStateOf("") }
    var isTextFieldFocused by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }

    val animatedTextFieldBorderColor by animateColorAsState(
        if (isTextFieldFocused) Styles.WHITE.toComposeColor()
        else Styles.BORDER_COLOR.toComposeColor()
    )
    val animatedTextFieldContentColor by animateColorAsState(
        if (isTextFieldFocused) Color.White
        else Color.White.copy(alpha = Styles.Opacity.TOP_BAR_CONTENT)
    )

    val movableSearchBar = remember {
        movableContentOf<Modifier> { modifier ->
            Row(
                modifier = modifier
                    .margin(left = 24.px, right = 12.px)
                    .border(
                        width = if (isTextFieldFocused) 1.5.px else 1.px,
                        style = LineStyle.Solid,
                        color = animatedTextFieldBorderColor.toKobwebColor()
                    )
                    .borderRadius(24.px),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AssetSvg(
                    id = Asset.Path::SEARCH.name,
                    path = Asset.Path.SEARCH,
                    primaryColor = animatedTextFieldContentColor.toKobwebColor(),
                ) {
                    marginLeft(16.px)
                    marginRight(10.px)
                }
                TextInput(value = searchQuery) {
                    ref { element ->
                        textInputRef = element
                        onDispose {}
                    }
                    onBlur { isTextFieldFocused = false }
                    onFocus { isTextFieldFocused = true }
                    onInput { event -> searchQuery = event.value }
                    onKeyUp { event ->
                        event.stopPropagation()
                        if (event.key == "Enter") {
                            if (searchQuery.isNotBlank()) {
                                onSearch(searchQuery)
                                searchQuery = ""
                            }
                            textInputRef?.clearFocus()
                        }
                    }
                    inputMode("search")
                    placeholder("Search")
                    style {
                        background(Background.None)
                        border(0.px)
                        color(Styles.WHITE.toString())
                        flex(1)
                        fontSize(16.px)
                        outline("none")
                        paddingBottom(12.px)
                        paddingTop(12.px)
                        width(100.percent)
                    }
                }
                if (searchQuery.isNotEmpty()) {
                    AssetSvg(
                        id = Asset.Path::CLOSE.name,
                        path = Asset.Path.CLOSE,
                        primaryColor = Colors.White,
                        onClick = { searchQuery = "" },
                    ) {
                        cursor(Cursor.Pointer)
                        marginRight(12.px)
                    }
                } else {
                    Box(modifier = Modifier.width(36.px))
                }
                AssetSvg(
                    id = Asset.Path::MIC.name,
                    path = Asset.Path.MIC,
                    primaryColor = animatedTextFieldContentColor.toKobwebColor(),
                    onClick = {}
                ) {
                    cursor(Cursor.Pointer)
                    marginRight(20.px)
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().height(TopBarDefaults.HEIGHT),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.width(4.px))
            AssetImageButton(asset = Asset.Icon.MENU, onClick = onDrawerButtonClick)
            Box(modifier = Modifier.width(12.px))
            Image(
                modifier = Modifier
                    .height(44.px)
                    .padding(topBottom = 12.px)
                    .clickable(onClick = onLogoClick),
                src = Asset.Icon.YOUTUBE_LOGO,
            )

            Spacer()
            if (breakpoint != Breakpoint.ZERO) movableSearchBar(Modifier.weight(1.5f))
            Spacer()

            // Actions
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.px),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AssetImageButton(
                    modifier = TopBarSearchButtonStyle.toModifier(),
                    asset = Asset.Icon.SEARCH,
                ) { showSearchBar = true }

                if (breakpoint isGreaterThan Breakpoint.SM) {
                    AssetImageButton(
                        asset = Asset.Icon.ADD,
                        onRefAvailable = { addBtnRef = it },
                    ) {}

                    addBtnRef?.let {
                        Tooltip(
                            target = ElementTarget.of(it),
                            text = "Add Video / Post",
                            showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                        )
                    }
                }

                AssetImageButton(
                    asset = Asset.Icon.NOTIFS,
                    onRefAvailable = { notificationBtnRef = it },
                    onClick = onOpenNotificationPanel,
                )

                notificationBtnRef?.let {
                    Tooltip(
                        target = ElementTarget.of(it),
                        text = "Notifications",
                        showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                    )
                }

                AssetImageButton(
                    asset = Asset.Icon.SETTINGS,
                    onRefAvailable = { settingsBtnRef = it },
                ) {}

                settingsBtnRef?.let {
                    Tooltip(
                        target = ElementTarget.of(it),
                        text = "Settings",
                        showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                    )
                }

                AssetImageButton(
                    modifier = Modifier.size(48.px),
                    onRefAvailable = { accountBtnRef = it },
                    asset = Asset.Channel.JUXTOPPOSED,
                ) {}

                accountBtnRef?.let {
                    Tooltip(
                        target = ElementTarget.of(it),
                        text = "Account",
                        showDelayMs = Constants.POPUP_SHOW_DELAY_MS,
                    )
                }
            }
        }

        if (breakpoint == Breakpoint.ZERO && showSearchBar) {
            Row(
                modifier = Modifier.zIndex(1).background(Styles.SURFACE).fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AssetImageButton(asset = Asset.Icon.ARROW_LEFT) {
                    showSearchBar = false
                    searchQuery = ""
                }
                movableSearchBar(Modifier.weight(1))
            }
        }
    }
}

val TopBarSearchButtonStyle = CssStyle {
    base { Modifier.display(DisplayStyle.None) }
    until(Breakpoint.SM) { Modifier.display(DisplayStyle.Inherit) }
}

object TopBarDefaults {
    val HEIGHT = 64.px
}
