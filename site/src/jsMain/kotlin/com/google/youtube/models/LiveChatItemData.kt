package com.google.youtube.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class LiveChatItemData(
    val userId: String,
    val userInitial: Char? = null,
    val message: String,
    val color: Color = colorFromUserId(userId),
)

fun colorFromUserId(id: String): Color = Color(
    when (id) {
        "0" -> 0xFF7CB2FD
        "1" -> 0xFF7CFDA7
        "2" -> 0xFFFD7CDB
        "3" -> 0xFFCCFD7C
        "youtube-enjoyer" -> 0xFF7B9EFF
        "videowatcher", "current-user" -> 0xFFB86697
        "therizzler" -> 0xFFD2B136
        "gamer123" -> 0xFFA36CD3
        "imhonestlyoutofusernameideas" -> 0xFFCC5390
        "bob" -> 0xFF61AD74
        "videoenthusiast" -> 0xFFD3AC6C
        else -> 0xFFFFFFFF
    }
)
