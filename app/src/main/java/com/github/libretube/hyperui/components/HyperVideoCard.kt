package com.github.libretube.hyperui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.github.libretube.hyperui.model.HyperVideoItem
import com.github.libretube.hyperui.theme.HyperColors
import com.github.libretube.hyperui.theme.HyperTypography

/**
 * HyperOS Adaptive Video Card with continuous squircle curvature,
 * frosted duration badge, and optional continue-watching progress bar.
 */
@Composable
fun HyperVideoCard(
    video: HyperVideoItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(6.dp)
    ) {
        // 16:9 Thumbnail with squircle corners
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(16.dp))
                .background(HyperColors.DarkCard)
        ) {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // Frosted duration badge in bottom right corner
            if (video.durationText.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.75f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = video.durationText,
                        style = HyperTypography.BadgeText,
                        color = Color.White
                    )
                }
            }

            // Continue watching progress bar at the bottom edge of thumbnail
            if (video.watchProgressPercent != null && video.watchProgressPercent > 0f) {
                LinearProgressIndicator(
                    progress = { video.watchProgressPercent },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(3.dp),
                    color = HyperColors.HyperBlue,
                    trackColor = Color.White.copy(alpha = 0.3f)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title and channel info
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            if (video.uploaderAvatarUrl != null) {
                AsyncImage(
                    model = video.uploaderAvatarUrl,
                    contentDescription = video.uploaderName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(HyperColors.DarkCard)
                )
                Spacer(modifier = Modifier.width(10.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    style = HyperTypography.VideoTitle,
                    color = HyperColors.DarkTextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(3.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = video.uploaderName,
                        style = HyperTypography.MetaText,
                        color = HyperColors.DarkTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (video.uploadDateText != null) {
                        Text(
                            text = " • ${video.uploadDateText}",
                            style = HyperTypography.MetaText,
                            color = HyperColors.DarkTextTertiary,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
