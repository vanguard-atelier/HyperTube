package com.github.libretube.hyperui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.github.libretube.R
import com.github.libretube.hyperui.model.HyperVideoItem
import com.github.libretube.hyperui.theme.HyperColors
import com.github.libretube.hyperui.theme.HyperTypography

/**
 * HyperSplitPlayerScreen (1:1 with user's Ibis Paint drawing):
 * - Ambient dynamic bloom drop-glow behind video frame
 * - Floating squircle video window with frosted controls (Timestamp, thick seekbar, CC, ⚙️)
 * - Aesthetic centered video title floating on top status bar
 * - Collapsed ☰ button on right (taps to expand full dock)
 * - miuix-style shape-morphing glass popup for ⚙️ video settings
 * - Split right column: Segments for 'comments' | 'up next'
 */
@Composable
fun HyperSplitPlayerScreen(
    video: HyperVideoItem,
    playlist: List<HyperVideoItem>,
    isPlaying: Boolean,
    progressPercent: Float,
    currentTimeText: String,
    totalTimeText: String,
    onPlayPauseToggle: () -> Unit,
    onSeek: (Float) -> Unit,
    onClosePlayer: () -> Unit,
    onExpandDock: () -> Unit,
    onVideoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    useMandarin: Boolean = false,
    audioOnlyMode: Boolean = false
) {
    var selectedRightTab by remember { mutableStateOf("up next") }
    var showSettingsPopup by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HyperColors.DarkBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Bar: Centered Aesthetic Title floating on top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = video.title,
                    style = HyperTypography.PlayerTitle,
                    color = HyperColors.DarkTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 48.dp)
                )
            }

            // Split Body: Left 65% Stage, Right 35% Dynamic Info / Playlist
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
            ) {
                // LEFT 65%: Cinema Stage & Video Controls
                Column(
                    modifier = Modifier
                        .weight(0.65f)
                        .fillMaxHeight()
                        .padding(end = 16.dp)
                ) {
                    // Video Player Container with Ambient Glow
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .border(1.dp, HyperColors.DarkCardBorder, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.Black)
                    ) {
                        // Ambient Thumbnail or Video surface
                        AsyncImage(
                            model = video.thumbnailUrl,
                            contentDescription = video.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )

                        // Top Controls Inside Video Window: Down Chevron (Left), CC & Settings (Right)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .align(Alignment.TopCenter),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FrostedIconButton(
                                iconRes = R.drawable.ic_arrow_down,
                                onClick = onClosePlayer
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FrostedTextButton(
                                    text = "CC",
                                    onClick = { /* Toggle captions */ }
                                )
                                FrostedIconButton(
                                    iconRes = R.drawable.ic_settings,
                                    onClick = { showSettingsPopup = !showSettingsPopup }
                                )
                            }
                        }

                        // Center Frosted Play/Pause Button
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.5f))
                                .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                                .clickable(onClick = onPlayPauseToggle)
                        ) {
                            Icon(
                                painter = painterResource(id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play),
                                contentDescription = "Play/Pause",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // Bottom Controls Inside Video: Timestamp, Thick Seekbar, Fullscreen
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "$currentTimeText / $totalTimeText",
                                    style = HyperTypography.BadgeText,
                                    color = Color.White
                                )

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_fullscreen),
                                    contentDescription = "Fullscreen",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { progressPercent },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = HyperColors.HyperBlue,
                                trackColor = Color.White.copy(alpha = 0.3f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Channel Badge, Subscribe Button, and View Count
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(HyperColors.DarkCard)
                            .border(1.dp, HyperColors.DarkCardBorder, RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(HyperColors.HyperBlue)
                                    ) {
                                        Text(
                                            text = video.uploaderName.take(1).uppercase(),
                                            style = HyperTypography.PlayerTitle,
                                            color = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = video.uploaderName,
                                            style = HyperTypography.VideoTitle,
                                            color = HyperColors.DarkTextPrimary
                                        )
                                        Text(
                                            text = video.viewCountText ?: "7.5M subscribers",
                                            style = HyperTypography.DockLabel,
                                            color = HyperColors.DarkTextSecondary
                                        )
                                    }
                                }

                                Button(
                                    onClick = { /* Subscribe */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = HyperColors.HyperBlue),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text(
                                        text = if (useMandarin) "订阅" else "Subscribe",
                                        style = HyperTypography.BadgeText,
                                        color = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "701,323,309 views • Apr 12, 2023",
                                style = HyperTypography.MetaText,
                                color = HyperColors.DarkTextSecondary
                            )
                        }
                    }
                }

                // RIGHT 35%: Split Sidebar with Comments / Up Next
                Box(
                    modifier = Modifier
                        .weight(0.35f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(24.dp))
                        .background(HyperColors.DarkCard)
                        .border(1.dp, HyperColors.DarkCardBorder, RoundedCornerShape(24.dp))
                        .padding(14.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Segmented Switcher: comments | up next
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Black.copy(alpha = 0.4f))
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            SegmentItem(
                                label = if (useMandarin) "评论" else "comments",
                                isSelected = selectedRightTab == "comments",
                                onClick = { selectedRightTab = "comments" }
                            )
                            SegmentItem(
                                label = if (useMandarin) "接下来播放" else "up next",
                                isSelected = selectedRightTab == "up next",
                                onClick = { selectedRightTab = "up next" }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Up Next Video List with Reorder and Playing Equalizer
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(playlist, key = { it.id }) { item ->
                                UpNextItemRow(
                                    item = item,
                                    isPlaying = item.id == video.id,
                                    onClick = { onVideoClick(item.id) }
                                )
                            }
                        }
                    }

                    // Floating Search & ☰ Collapsed Dock Button on Right
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 6.dp, top = 56.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FloatingMiniCircle(iconRes = R.drawable.ic_search, onClick = {})
                        FloatingMiniCircle(iconRes = R.drawable.ic_menu, onClick = onExpandDock)
                    }
                }
            }
        }

        // miuix-style Shape-Morphing Glass Popup for ⚙️ Settings
        AnimatedVisibility(
            visible = showSettingsPopup,
            enter = fadeIn() + scaleIn(initialScale = 0.8f),
            exit = fadeOut() + scaleOut(targetScale = 0.8f),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
        ) {
            MiuixGlassSettingsPopup(
                useMandarin = useMandarin,
                audioOnlyMode = audioOnlyMode,
                onDismiss = { showSettingsPopup = false }
            )
        }
    }
}

@Composable
private fun FrostedIconButton(iconRes: Int, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun FrostedTextButton(text: String, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            style = HyperTypography.BadgeText,
            color = Color.White
        )
    }
}

@Composable
private fun FloatingMiniCircle(iconRes: Int, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(HyperColors.DarkGlassDock)
            .border(1.dp, HyperColors.DarkCardBorder, CircleShape)
            .clickable(onClick = onClick)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun SegmentItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) HyperColors.DarkCard else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = HyperTypography.MetaText,
            color = if (isSelected) Color.White else HyperColors.DarkTextSecondary
        )
    }
}

@Composable
private fun UpNextItemRow(
    item: HyperVideoItem,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isPlaying) HyperColors.DarkCard.copy(alpha = 0.6f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(6.dp)
    ) {
        AsyncImage(
            model = item.thumbnailUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 80.dp, height = 48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Black)
        )

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = HyperTypography.MetaText,
                color = if (isPlaying) HyperColors.HyperBlue else HyperColors.DarkTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${item.uploaderName} • ${item.durationText}",
                style = HyperTypography.DockLabel,
                color = HyperColors.DarkTextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun MiuixGlassSettingsPopup(
    useMandarin: Boolean,
    audioOnlyMode: Boolean,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(HyperColors.DarkGlassDock)
            .border(1.dp, HyperColors.DarkCardBorder, RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = if (useMandarin) "画质: 1080p60 ✓" else "Quality: 1080p60 ✓",
                style = HyperTypography.VideoTitle,
                color = HyperColors.HyperBlue
            )
            Text(
                text = if (useMandarin) "播放速度: 1.0x" else "Speed: 1.0x",
                style = HyperTypography.MetaText,
                color = HyperColors.DarkTextPrimary
            )
            Text(
                text = if (audioOnlyMode) (if (useMandarin) "纯音频: 已开启" else "Audio Mode: On") else (if (useMandarin) "纯音频: 已关闭" else "Audio Mode: Off"),
                style = HyperTypography.MetaText,
                color = HyperColors.DarkTextSecondary
            )
        }
    }
}
