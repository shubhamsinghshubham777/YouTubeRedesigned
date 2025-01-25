package com.google.youtube.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.youtube.models.TagData
import com.google.youtube.utils.Assets
import com.google.youtube.utils.PaddingValues
import com.google.youtube.utils.Styles
import com.google.youtube.utils.toComposeColor
import com.google.youtube.utils.toKobwebColor
import com.varabyte.kobweb.compose.css.Background
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.background
import com.varabyte.kobweb.compose.css.color
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.foundation.layout.Spacer
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.background
import com.varabyte.kobweb.compose.ui.modifiers.display
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.flexWrap
import com.varabyte.kobweb.compose.ui.modifiers.fontSize
import com.varabyte.kobweb.compose.ui.modifiers.gap
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.lineHeight
import com.varabyte.kobweb.compose.ui.modifiers.maxHeight
import com.varabyte.kobweb.compose.ui.modifiers.opacity
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.width
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.theme.shapes.Rect
import com.varabyte.kobweb.silk.theme.shapes.clip
import org.jetbrains.compose.web.attributes.placeholder
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.FlexWrap
import org.jetbrains.compose.web.css.LineStyle
import org.jetbrains.compose.web.css.border
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.outline
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.css.vw
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.TextInput

@Composable
fun PersonalisedFeedDialog(
    contentPadding: PaddingValues = PaddingValues(24.px),
    onClose: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Styles.SURFACE_ELEVATED)
            .clip(Rect(20.px))
            .maxHeight(80.vh)
            .width(75.vw)
            .overflow { y(Overflow.Scroll) }
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(left = 20.px, top = 12.px, right = 12.px, bottom = 12.px),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.fontSize(18.px)) { Text("Personalized Feed") }
            Spacer()
            Row(modifier = Modifier.opacity(0.5f)) {
                AssetImageButton(Assets.Icons.SETTINGS) {}
                AssetImageButton(Assets.Icons.CLOSE, onClick = onClose)
            }
        }

        // Divider
        Box(modifier = Modifier.background(Styles.DIVIDER_LIGHTER).fillMaxWidth().height(1.px))

        // Your Tags
        Content(
            modifier = Modifier.padding(
                left = contentPadding.left,
                top = contentPadding.top,
                right = contentPadding.right,
                bottom = 32.px,
            ),
            title = "Your Tags",
            message = "These tags will be used to build your custom feed. You can also " +
                    "filter out the content on your feed using these tags. \n" +
                    "Enter a comma after a keyword to save it as a tag. Tags can " +
                    "include: topics, content types (e.g., shorts, playlists, etc.), " +
                    "channels, collections, etc.",
            allTags = arrayOf(
                TagData("Subscriptions", Assets.Paths.SUBSCRIPTIONS),
                TagData("Posts", Assets.Paths.POSTS),
                TagData("Music"),
                TagData("Tech"),
                TagData("Design"),
                TagData("Live"),
                TagData("Playlists"),
                TagData("Cats"),
                TagData("UI/UX Design"),
                TagData("Electronics"),
                TagData("Art"),
                TagData("Tech news"),
                TagData("Lofi beats"),
                TagData("UI/UX Redesign"),
                TagData("Video Games"),
                TagData("Apple"),
                TagData("Linux"),
            ),
            selectedTags = arrayOf(
                TagData("Video Games"),
                TagData("Apple"),
                TagData("Linux"),
            ),
        )

        // Exclude Tags
        Content(
            modifier = Modifier.padding(
                left = contentPadding.left,
                right = contentPadding.right,
                bottom = contentPadding.bottom,
            ),
            title = "Exclude Tags",
            message = "You can prevent certain content or creators from appearing in your feed.",
            allTags = arrayOf(
                TagData("Skibidi"),
                TagData("brainrot"),
                TagData("rizz"),
                TagData("Shorts", Assets.Paths.SHORTS),
                TagData("Fortnite"),
            ),
        )
    }
}

@Composable
private fun Content(
    title: String,
    message: String,
    allTags: Array<TagData> = emptyArray(),
    selectedTags: Array<TagData> = emptyArray(),
    modifier: Modifier = Modifier,
) {
    val searchQueryState = remember { mutableStateOf("") }
    val selectedTagsList = remember { mutableStateListOf(*selectedTags) }

    Column(
        modifier = Modifier.fillMaxWidth().then(modifier),
        verticalArrangement = Arrangement.spacedBy(22.px)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.px)) {
            Box { Text(title) }
            Box(
                modifier = Modifier.fontSize(14.px).lineHeight(24.px).opacity(0.7f)
            ) { Text(message) }
        }

        TextField(textState = searchQueryState)

        Div(Modifier.display(DisplayStyle.Flex).flexWrap(FlexWrap.Wrap).gap(8.px).toAttrs()) {
            allTags
                .filter { tag -> tag.label.contains(searchQueryState.value, ignoreCase = true) }
                .forEach { tag ->
                    val isSelected = selectedTagsList.contains(tag)
                    key(tag.label) {
                        AssetSvgButton(
                            containerColor = if (isSelected) Styles.BACKGROUND_SELECTED else Styles.WHITE,
                            contentColor = if (isSelected) Styles.WHITE else Styles.BLACK,
                            endIconPath = Assets.Paths.CLOSE,
                            iconPrimaryColor = if (isSelected) Styles.RED else Styles.BLACK,
                            iconSecondaryColor = if (isSelected) Styles.PINK else null,
                            id = "tag_${tag.label}",
                            isDense = true,
                            isSelected = isSelected,
                            onClick = {
                                if (!selectedTagsList.contains(tag)) selectedTagsList.add(tag)
                            },
                            onEndIconClick = { selectedTagsList.remove(tag) },
                            startIconPath = if (isSelected) Assets.Paths.CHECK else tag.iconPath,
                            text = tag.label,
                            type = AssetSvgButtonType.SelectableChip,
                        )
                    }
                }
        }
    }
}

@Composable
private fun TextField(textState: MutableState<String>) {
    var isFocused by remember { mutableStateOf(false) }
    val animatedBorderColor by animateColorAsState(
        Styles.WHITE.copyf(alpha = if (isFocused) 1f else 0.15f).toComposeColor()
    )
    TextInput(value = textState.value) {
        onInput { event -> textState.value = event.value }
        onFocus { isFocused = true }
        onBlur { isFocused = false }
        style {
            background(Background.None)
            border(1.px, LineStyle.Solid, animatedBorderColor.toKobwebColor())
            borderRadius(8.px)
            color(Styles.WHITE.toString())
            outline("none")
            padding(16.px)
            width(100.percent)
        }
        placeholder("Search for tags...")
    }
}
