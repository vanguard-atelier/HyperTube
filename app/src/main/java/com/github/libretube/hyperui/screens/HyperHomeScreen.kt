package com.github.libretube.hyperui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.libretube.R
import com.github.libretube.hyperui.components.HyperFloatingDock
import com.github.libretube.hyperui.components.HyperProgressiveBlurOverlay
import com.github.libretube.hyperui.components.HyperVideoCard
import com.github.libretube.hyperui.model.HyperTab
import com.github.libretube.hyperui.model.HyperVideoItem
import com.github.libretube.hyperui.theme.HyperColors
import com.github.libretube.hyperui.theme.HyperTypography

/**
 * HyperOS Home Screen for Tablets (Redmi Pad Pro).
 * 3-column video grid with zero-algo feed, top progressive blur,
 * floating search circle in the top right, and floating dock pill on the right edge.
 */
@Composable
fun HyperHomeScreen(
    videos: List<HyperVideoItem>,
    currentTab: HyperTab,
    onTabSelected: (HyperTab) -> Unit,
    onVideoClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    useMandarin: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HyperColors.DarkBackground)
    ) {
        // 3-Column Adaptive Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(
                start = 24.dp,
                top = 72.dp,
                end = 92.dp, // Generous padding so floating dock doesn't cover cards
                bottom = 48.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                items = videos,
                key = { it.id } // Strict immutable key to eliminate ghosting
            ) { video ->
                HyperVideoCard(
                    video = video,
                    onClick = { onVideoClick(video.id) }
                )
            }
        }

        // Top Status Bar Progressive Blur
        HyperProgressiveBlurOverlay(
            isTop = true,
            height = 68.dp,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // Bottom Bar Progressive Blur
        HyperProgressiveBlurOverlay(
            isTop = false,
            height = 36.dp,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Top Bar: HyperTube Wordmark (Left) and Floating Search Circle (Right)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "HyperTube",
                style = HyperTypography.AppBrand,
                color = HyperColors.DarkTextPrimary
            )

            // Top-right floating circular search button
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(HyperColors.DarkGlassDock)
                    .border(1.dp, HyperColors.DarkCardBorder, CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onSearchClick
                    )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Floating Right-Side Navigation Dock Pill
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
