package com.github.libretube.hyperui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.libretube.R
import com.github.libretube.hyperui.components.HyperFloatingDock
import com.github.libretube.hyperui.components.HyperProgressiveBlurOverlay
import com.github.libretube.hyperui.model.HyperPlaylistItem
import com.github.libretube.hyperui.model.HyperTab
import com.github.libretube.hyperui.model.HyperVideoItem
import com.github.libretube.hyperui.theme.HyperColors
import com.github.libretube.hyperui.theme.HyperTypography

/**
 * HyperLibraryScreen matching the user's Xiaomi Gallery screenshot:
 * Top: Active Watch Queue ("Browser tabs" without RAM murder)
 * Bottom: 6-column album squircle grid (History, Playlists, Liked, Offline)
 */
@Composable
fun HyperLibraryScreen(
    watchQueue: List<HyperVideoItem>,
    playlists: List<HyperPlaylistItem>,
    currentTab: HyperTab,
    onTabSelected: (HyperTab) -> Unit,
    onVideoClick: (String) -> Unit,
    onPlaylistClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    useMandarin: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HyperColors.DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, top = 24.dp, end = 92.dp, bottom = 24.dp)
        ) {
            // Screen Header
            Text(
                text = if (useMandarin) "媒体库" else "Library",
                style = HyperTypography.AppBrand,
                color = HyperColors.DarkTextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Active Watch Queue ("Browser Tabs")
            if (watchQueue.isNotEmpty()) {
                Text(
                    text = if (useMandarin) "正在排队观看" else "Active Watch Queue",
                    style = HyperTypography.PlayerTitle,
                    color = HyperColors.DarkTextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(watchQueue, key = { it.id }) { item ->
                        QueueTabCard(
                            video = item,
                            onClick = { onVideoClick(item.id) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Section 2: Xiaomi Gallery-Style Album Squircles (6 per row on tablet)
            Text(
                text = if (useMandarin) "相册与播放列表" else "Albums & Playlists",
                style = HyperTypography.PlayerTitle,
                color = HyperColors.DarkTextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Built-in standard album cards
                item {
                    XiaomiGalleryTile(
                        title = if (useMandarin) "观看历史" else "Watch History",
                        subtitle = if (useMandarin) "最近播放" else "Recent",
                        iconRes = R.drawable.ic_history,
                        onClick = { onPlaylistClick("history") }
                    )
                }

                item {
                    XiaomiGalleryTile(
                        title = if (useMandarin) "喜欢的内容" else "Liked Videos",
                        subtitle = if (useMandarin) "已收藏" else "Favorites",
                        iconRes = R.drawable.ic_hearted,
                        onClick = { onPlaylistClick("liked") }
                    )
                }

                item {
                    XiaomiGalleryTile(
                        title = if (useMandarin) "离线缓存" else "Downloads",
                        subtitle = if (useMandarin) "本地存储" else "Offline",
                        iconRes = R.drawable.ic_download_filled,
                        onClick = { onPlaylistClick("downloads") }
                    )
                }

                items(playlists, key = { it.id }) { playlist ->
                    XiaomiGalleryTile(
                        title = playlist.title,
                        subtitle = "${playlist.videoCount} " + if (useMandarin) "个视频" else "videos",
                        thumbnailUrl = playlist.thumbnailUrls.firstOrNull(),
                        onClick = { onPlaylistClick(playlist.id) }
                    )
                }
            }
        }

        // Floating Right-Side Dock Pill
        HyperFloatingDock(
            currentTab = currentTab,
            onTabSelected = onTabSelected,
            useMandarin = useMandarin,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
        )
    }
}

@Composable
private fun QueueTabCard(
    video: HyperVideoItem,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .width(240.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(HyperColors.DarkCard)
            .border(1.dp, HyperColors.DarkCardBorder, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = video.thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 64.dp, height = 40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = video.title,
                style = HyperTypography.MetaText,
                color = HyperColors.DarkTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = video.uploaderName,
                style = HyperTypography.DockLabel,
                color = HyperColors.DarkTextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun XiaomiGalleryTile(
    title: String,
    subtitle: String,
    iconRes: Int? = null,
    thumbnailUrl: String? = null,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(18.dp))
                .background(HyperColors.DarkCard)
                .border(1.dp, HyperColors.DarkCardBorder, RoundedCornerShape(18.dp))
        ) {
            if (thumbnailUrl != null) {
                AsyncImage(
                    model = thumbnailUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            } else if (iconRes != null) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    tint = HyperColors.HyperBlue,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = HyperTypography.VideoTitle,
            color = HyperColors.DarkTextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = subtitle,
            style = HyperTypography.MetaText,
            color = HyperColors.DarkTextSecondary
        )
    }
}
