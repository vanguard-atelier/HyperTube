package com.github.libretube.hyperui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.libretube.R
import com.github.libretube.hyperui.model.HyperTab
import com.github.libretube.hyperui.theme.HyperColors
import com.github.libretube.hyperui.theme.HyperTypography

/**
 * Floating frosted-glass dock pill pinned to the right edge on tablet.
 * Exactly replicated from the user's Ibis Paint visual reference.
 */
@Composable
fun HyperFloatingDock(
    currentTab: HyperTab,
    onTabSelected: (HyperTab) -> Unit,
    modifier: Modifier = Modifier,
    useMandarin: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(32.dp))
            .background(HyperColors.DarkGlassDock)
            .border(1.dp, HyperColors.DarkCardBorder, RoundedCornerShape(32.dp))
            .padding(vertical = 12.dp, horizontal = 8.dp)
            .width(64.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HyperDockItem(
                tab = HyperTab.HOME,
                isSelected = currentTab == HyperTab.HOME,
                iconRes = R.drawable.ic_home,
                useMandarin = useMandarin,
                onClick = { onTabSelected(HyperTab.HOME) }
            )

            HyperDockItem(
                tab = HyperTab.SUBS,
                isSelected = currentTab == HyperTab.SUBS,
                iconRes = R.drawable.ic_subscriptions,
                useMandarin = useMandarin,
                onClick = { onTabSelected(HyperTab.SUBS) }
            )

            HyperDockItem(
                tab = HyperTab.LIBRARY,
                isSelected = currentTab == HyperTab.LIBRARY,
                iconRes = R.drawable.ic_library,
                useMandarin = useMandarin,
                onClick = { onTabSelected(HyperTab.LIBRARY) }
            )

            HyperDockItem(
                tab = HyperTab.SETTINGS,
                isSelected = currentTab == HyperTab.SETTINGS,
                iconRes = R.drawable.ic_settings,
                useMandarin = useMandarin,
                onClick = { onTabSelected(HyperTab.SETTINGS) }
            )
        }
    }
}

@Composable
private fun HyperDockItem(
    tab: HyperTab,
    isSelected: Boolean,
    iconRes: Int,
    useMandarin: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else HyperColors.DarkTextSecondary,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "IconColor"
    )

    val backgroundPillColor by animateColorAsState(
        targetValue = if (isSelected) HyperColors.HyperBlueDark.copy(alpha = 0.85f) else Color.Transparent,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 400f),
        label = "BgColor"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 4.dp, vertical = 6.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(backgroundPillColor)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = tab.name,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Text(
            text = if (useMandarin) tab.labelZh else tab.labelEn,
            style = HyperTypography.DockLabel,
            color = iconColor
        )
    }
}
