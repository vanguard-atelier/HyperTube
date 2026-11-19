package com.github.libretube.hyperui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.libretube.hyperui.components.HyperFloatingDock
import com.github.libretube.hyperui.components.HyperVideoCard
import com.github.libretube.hyperui.model.HyperChannelItem
import com.github.libretube.hyperui.model.HyperTab
import com.github.libretube.hyperui.model.HyperVideoItem
import com.github.libretube.hyperui.theme.HyperColors
import com.github.libretube.hyperui.theme.HyperTypography

/**
 * HyperSubscriptionsScreen with story-style horizontal channel avatar rail
 * and chronological 3-column video feed below.
 */
@Composable
fun HyperSubscriptionsScreen(
    channels: List<HyperChannelItem>,
    feedVideos: List<HyperVideoItem>,
    currentTab: HyperTab,
    selectedChannelId: String?,
    onTabSelected: (HyperTab) -> Unit,
    onChannelSelected: (String?) -> Unit,
    onVideoClick: (String) -> Unit,
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
            Text(
                text = if (useMandarin) "关注动态" else "Subscriptions",
                style = HyperTypography.AppBrand,
                color = HyperColors.DarkTextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Horizontal Story-Style Channel Avatar Rail
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // "All" filter chip
                item {
                    ChannelAvatarCircle(
                        name = if (useMandarin) "全部" else "All",
                        avatarUrl = null,
                        isSelected = selectedChannelId == null,
                        onClick = { onChannelSelected(null) }
                    )
                }

                items(channels, key = { it.id }) { channel ->
                    ChannelAvatarCircle(
                        name = channel.name,
                        avatarUrl = channel.avatarUrl,
                        isSelected = selectedChannelId == channel.id,
                        onClick = { onChannelSelected(channel.id) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 3-Column Video Feed
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(feedVideos, key = { it.id }) { video ->
                    HyperVideoCard(
                        video = video,
                        onClick = { onVideoClick(video.id) }
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
private fun ChannelAvatarCircle(
    name: String,
    avatarUrl: String?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(2.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(HyperColors.DarkCard)
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) HyperColors.HyperBlue else HyperColors.DarkCardBorder,
                    shape = CircleShape
                )
        ) {
            if (avatarUrl != null) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            } else {
                Text(
                    text = name.take(2).uppercase(),
                    style = HyperTypography.BadgeText,
                    color = if (isSelected) HyperColors.HyperBlue else HyperColors.DarkTextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = name,
            style = HyperTypography.DockLabel,
            color = if (isSelected) HyperColors.DarkTextPrimary else HyperColors.DarkTextSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
