package com.github.libretube.hyperui.model

import androidx.compose.runtime.Immutable

@Immutable
data class HyperVideoItem(
    val id: String,
    val title: String,
    val uploaderName: String,
    val uploaderAvatarUrl: String? = null,
    val thumbnailUrl: String,
    val durationText: String = "",
    val uploadDateText: String? = null,
    val viewCountText: String? = null,
    val watchProgressPercent: Float? = null // 0.0 to 1.0 for continue watching
)

@Immutable
data class HyperChannelItem(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val hasRecentUpload: Boolean = false
)

@Immutable
data class HyperPlaylistItem(
    val id: String,
    val title: String,
    val videoCount: Int,
    val thumbnailUrls: List<String> = emptyList()
)

enum class HyperTab(val labelEn: String, val labelZh: String) {
    HOME("Home", "首页"),
    SUBS("Subs", "关注"),
    LIBRARY("Library", "媒体库"),
    SETTINGS("Settings", "设置")
}
