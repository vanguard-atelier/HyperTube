package com.github.libretube.hyperui.bridge

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.github.libretube.hyperui.model.HyperChannelItem
import com.github.libretube.hyperui.model.HyperPlaylistItem
import com.github.libretube.hyperui.model.HyperTab
import com.github.libretube.hyperui.model.HyperVideoItem
import com.github.libretube.hyperui.screens.HyperHomeScreen
import com.github.libretube.hyperui.screens.HyperLibraryScreen
import com.github.libretube.hyperui.screens.HyperSettingsScreen
import com.github.libretube.hyperui.screens.HyperSplitPlayerScreen
import com.github.libretube.hyperui.screens.HyperSubscriptionsScreen
import com.github.libretube.hyperui.theme.HyperTheme

/**
 * HyperAppShell: The master container coordinating the screens, navigation dock,
 * and seamless transition into the cinematic Split Player.
 */
@Composable
fun HyperAppShell(
    modifier: Modifier = Modifier,
    initialVideos: List<HyperVideoItem> = emptyList(),
    onVideoPlay: (String) -> Unit = {}
) {
    var currentTab by remember { mutableStateOf(HyperTab.HOME) }
    var useMandarin by remember { mutableStateOf(false) }
    var audioOnlyMode by remember { mutableStateOf(false) }
    var activePlayingVideoId by remember { mutableStateOf<String?>(null) }
    var selectedChannelId by remember { mutableStateOf<String?>(null) }

    // Sample high-quality video items for instant preview & zero-jank boot
    val demoVideos = remember {
        if (initialVideos.isNotEmpty()) initialVideos else listOf(
            HyperVideoItem(
                id = "demo_yoasobi",
                title = "YOASOBI「アイドル」 Official Music Video",
                uploaderName = "Ayase / YOASOBI",
                thumbnailUrl = "https://i.ytimg.com/vi/ZRtdQ81jPUQ/maxresdefault.jpg",
                durationText = "3:48",
                uploadDateText = "1 year ago",
                viewCountText = "500M views",
                watchProgressPercent = 0.65f
            ),
            HyperVideoItem(
                id = "demo_clarity",
                title = "Germany's Palantir clone is here: CLARITY Deep Dive",
                uploaderName = "TechAltar",
                thumbnailUrl = "https://i.ytimg.com/vi/dQw4w9WgXcQ/hqdefault.jpg",
                durationText = "18:10",
                uploadDateText = "3 hours ago",
                viewCountText = "9.3K views"
            ),
            HyperVideoItem(
                id = "demo_gta6",
                title = "Is GTA 6 Bad? Everything We Know So Far",
                uploaderName = "Dolan Darkest",
                thumbnailUrl = "https://i.ytimg.com/vi/dQw4w9WgXcQ/hqdefault.jpg",
                durationText = "2:18",
                uploadDateText = "24 minutes ago",
                viewCountText = "20K views"
            ),
            HyperVideoItem(
                id = "demo_qwen",
                title = "Your Local AI Is Now a Year Behind ChatGPT and Claude",
                uploaderName = "Matthew Berman",
                thumbnailUrl = "https://i.ytimg.com/vi/dQw4w9WgXcQ/hqdefault.jpg",
                durationText = "8:35",
                uploadDateText = "2 days ago",
                viewCountText = "85K views"
            ),
            HyperVideoItem(
                id = "demo_clash",
                title = "1 Troop VS 4 Heroes | Clash of Clans Challenge",
                uploaderName = "Clash Everytime",
                thumbnailUrl = "https://i.ytimg.com/vi/dQw4w9WgXcQ/hqdefault.jpg",
                durationText = "8:08",
                uploadDateText = "1 year ago",
                viewCountText = "752K views"
            ),
            HyperVideoItem(
                id = "demo_anthropic",
                title = "Anthropic Projected Total Loss: \$80.7B Breakdown",
                uploaderName = "ColdFusion",
                thumbnailUrl = "https://i.ytimg.com/vi/dQw4w9WgXcQ/hqdefault.jpg",
                durationText = "14:20",
                uploadDateText = "5 hours ago",
                viewCountText = "140K views"
            )
        )
    }

    val demoChannels = remember {
        listOf(
            HyperChannelItem("ch1", "YOASOBI", null, true),
            HyperChannelItem("ch2", "TechAltar", null, true),
            HyperChannelItem("ch3", "ColdFusion", null, false),
            HyperChannelItem("ch4", "Matthew Berman", null, true)
        )
    }

    HyperTheme(darkTheme = true) {
        val playingVideo = demoVideos.firstOrNull { it.id == activePlayingVideoId }

        if (playingVideo != null) {
            HyperSplitPlayerScreen(
                video = playingVideo,
                playlist = demoVideos,
                isPlaying = true,
                progressPercent = 0.42f,
                currentTimeText = "1:24",
                totalTimeText = playingVideo.durationText,
                onPlayPauseToggle = { },
                onSeek = { },
                onClosePlayer = { activePlayingVideoId = null },
                onExpandDock = { activePlayingVideoId = null },
                onVideoClick = { id ->
                    activePlayingVideoId = id
                    onVideoPlay(id)
                },
                useMandarin = useMandarin,
                audioOnlyMode = audioOnlyMode
            )
        } else {
            when (currentTab) {
                HyperTab.HOME -> {
                    HyperHomeScreen(
                        videos = demoVideos,
                        currentTab = currentTab,
                        onTabSelected = { currentTab = it },
                        onVideoClick = { id ->
                            activePlayingVideoId = id
                            onVideoPlay(id)
                        },
                        onSearchClick = { },
                        useMandarin = useMandarin,
                        modifier = modifier
                    )
                }
                HyperTab.SUBS -> {
                    HyperSubscriptionsScreen(
                        channels = demoChannels,
                        feedVideos = demoVideos,
                        currentTab = currentTab,
                        selectedChannelId = selectedChannelId,
                        onTabSelected = { currentTab = it },
                        onChannelSelected = { selectedChannelId = it },
                        onVideoClick = { id ->
                            activePlayingVideoId = id
                            onVideoPlay(id)
                        },
                        useMandarin = useMandarin,
                        modifier = modifier
                    )
                }
                HyperTab.LIBRARY -> {
                    HyperLibraryScreen(
                        watchQueue = demoVideos.take(3),
                        playlists = listOf(
                            HyperPlaylistItem("p1", "Late Night Synthwave", 24),
                            HyperPlaylistItem("p2", "Anime Openings", 18),
                            HyperPlaylistItem("p3", "Tech Deep Dives", 45)
                        ),
                        currentTab = currentTab,
                        onTabSelected = { currentTab = it },
                        onVideoClick = { id ->
                            activePlayingVideoId = id
                            onVideoPlay(id)
                        },
                        onPlaylistClick = { },
                        useMandarin = useMandarin,
                        modifier = modifier
                    )
                }
                HyperTab.SETTINGS -> {
                    HyperSettingsScreen(
                        useMandarin = useMandarin,
                        audioOnlyMode = audioOnlyMode,
                        onMandarinToggle = { useMandarin = it },
                        onAudioOnlyToggle = { audioOnlyMode = it },
                        currentTab = currentTab,
                        onTabSelected = { currentTab = it },
                        modifier = modifier
                    )
                }
            }
        }
    }
}
