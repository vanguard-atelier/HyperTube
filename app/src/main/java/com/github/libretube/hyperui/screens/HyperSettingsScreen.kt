package com.github.libretube.hyperui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.libretube.hyperui.components.HyperFloatingDock
import com.github.libretube.hyperui.model.HyperTab
import com.github.libretube.hyperui.theme.HyperColors
import com.github.libretube.hyperui.theme.HyperTypography

/**
 * Minimalist Settings Screen designed with HyperOS grouped squircle cards.
 * No Material You dynamic color junk; clean toggles for Audio Mode, Mandarin, and instances.
 */
@Composable
fun HyperSettingsScreen(
    useMandarin: Boolean,
    audioOnlyMode: Boolean,
    onMandarinToggle: (Boolean) -> Unit,
    onAudioOnlyToggle: (Boolean) -> Unit,
    currentTab: HyperTab,
    onTabSelected: (HyperTab) -> Unit,
    modifier: Modifier = Modifier
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
                text = if (useMandarin) "设置" else "Settings",
                style = HyperTypography.AppBrand,
                color = HyperColors.DarkTextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Group 1: General & Language
            SettingsGroup(title = if (useMandarin) "常规与语言" else "General & Language") {
                SettingsSwitchRow(
                    title = if (useMandarin) "简体中文界面" else "Mandarin Language (简体中文)",
                    subtitle = if (useMandarin) "Xiaomi HyperOS 原生中文字体支持" else "Native Chinese typography support",
                    checked = useMandarin,
                    onCheckedChange = onMandarinToggle
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Group 2: Audio & Playback Mode
            SettingsGroup(title = if (useMandarin) "音频与播放" else "Audio & Playback") {
                SettingsSwitchRow(
                    title = if (useMandarin) "纯音频省电模式" else "Audio-Only Mode",
                    subtitle = if (useMandarin) "关闭视频解码以节省 90% 流量和电量" else "Disables video stream decoding to save 90% battery & bandwidth",
                    checked = audioOnlyMode,
                    onCheckedChange = onAudioOnlyToggle
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsInfoRow(
                    title = if (useMandarin) "后台播放" else "Background Playback",
                    value = if (useMandarin) "已启用" else "Enabled"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Group 3: About Vanguard
            SettingsGroup(title = if (useMandarin) "关于" else "About") {
                SettingsInfoRow(
                    title = "HyperTube",
                    value = "v1.0 (Redmi Pad Pro Edition)"
                )
                Spacer(modifier = Modifier.height(12.dp))
                SettingsInfoRow(
                    title = if (useMandarin) "开发者" else "Developer",
                    value = "Shumail (vanguard-atelier)"
                )
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
private fun SettingsGroup(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = HyperTypography.MetaText,
            color = HyperColors.DarkTextSecondary,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(HyperColors.DarkCard)
                .border(1.dp, HyperColors.DarkCardBorder, RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column {
                content()
            }
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = HyperTypography.VideoTitle,
                color = HyperColors.DarkTextPrimary
            )
            Text(
                text = subtitle,
                style = HyperTypography.MetaText,
                color = HyperColors.DarkTextSecondary
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = HyperColors.HyperBlueDark,
                uncheckedThumbColor = HyperColors.DarkTextSecondary,
                uncheckedTrackColor = HyperColors.DarkCard
            )
        )
    }
}

@Composable
private fun SettingsInfoRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = HyperTypography.VideoTitle,
            color = HyperColors.DarkTextPrimary
        )
        Text(
            text = value,
            style = HyperTypography.MetaText,
            color = HyperColors.HyperBlue
        )
    }
}
