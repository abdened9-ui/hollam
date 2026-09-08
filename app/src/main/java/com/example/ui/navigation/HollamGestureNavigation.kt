package com.example.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import com.example.ui.camera.CameraScreen
import com.example.ui.chat.ChatScreen
import com.example.ui.map.HollamMapScreen
import com.example.ui.profile.ProfileScreen
import com.example.ui.stories.StoriesDiscoverScreen
import com.example.ui.theme.HollamBlack
import kotlinx.coroutines.launch

enum class HollamVerticalSheet {
    NONE,
    TOP_PROFILE,
    BOTTOM_MAP
}

@Composable
fun HollamGestureNavigation(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    // Horizontal 3-way pager:
    // Page 0: Left -> Chat
    // Page 1: Center -> Camera (starts here!)
    // Page 2: Right -> Stories & Discover
    val pagerState = rememberPagerState(initialPage = 1) { 3 }

    // Vertical Sheets:
    // Top -> Profile & Settings
    // Bottom -> hollam Map
    var verticalSheet by remember { mutableStateOf(HollamVerticalSheet.NONE) }
    var totalVerticalDrag by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HollamBlack)
            .testTag("hollam_gesture_navigation_root")
            .pointerInput(pagerState.currentPage, verticalSheet) {
                // When in Camera (page 1) and no sheet open, listen to vertical drag gestures
                if (pagerState.currentPage == 1 && verticalSheet == HollamVerticalSheet.NONE) {
                    detectVerticalDragGestures(
                        onDragStart = { totalVerticalDrag = 0f },
                        onDragEnd = {
                            if (totalVerticalDrag > 60f) {
                                // Dragged down -> Open Top Profile
                                verticalSheet = HollamVerticalSheet.TOP_PROFILE
                            } else if (totalVerticalDrag < -60f) {
                                // Dragged up -> Open Bottom Map
                                verticalSheet = HollamVerticalSheet.BOTTOM_MAP
                            }
                            totalVerticalDrag = 0f
                        },
                        onDragCancel = { totalVerticalDrag = 0f },
                        onVerticalDrag = { _, dragAmount ->
                            totalVerticalDrag += dragAmount
                        }
                    )
                }
            }
    ) {
        // Horizontal Pager for Left (Chat), Center (Camera), Right (Stories)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = verticalSheet == HollamVerticalSheet.NONE
        ) { page ->
            when (page) {
                0 -> {
                    // Left Screen: Chat
                    ChatScreen(
                        onNavigateBackToCamera = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                    )
                }
                1 -> {
                    // Center Screen: Camera (Immediate start)
                    CameraScreen(
                        onNavigateToChat = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                        onNavigateToStories = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(2)
                            }
                        },
                        onNavigateToProfile = {
                            verticalSheet = HollamVerticalSheet.TOP_PROFILE
                        },
                        onNavigateToMap = {
                            verticalSheet = HollamVerticalSheet.BOTTOM_MAP
                        }
                    )
                }
                2 -> {
                    // Right Screen: Stories & Discover
                    StoriesDiscoverScreen(
                        onNavigateBackToCamera = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                    )
                }
            }
        }

        // Top Sheet: Profile & Settings
        AnimatedVisibility(
            visible = verticalSheet == HollamVerticalSheet.TOP_PROFILE,
            enter = slideInVertically(
                initialOffsetY = { -it },
                animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { -it },
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            ) + fadeOut()
        ) {
            ProfileScreen(
                onDismiss = {
                    verticalSheet = HollamVerticalSheet.NONE
                },
                onLogout = {
                    verticalSheet = HollamVerticalSheet.NONE
                    onLogout()
                }
            )
        }

        // Bottom Sheet: hollam Map
        AnimatedVisibility(
            visible = verticalSheet == HollamVerticalSheet.BOTTOM_MAP,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            ) + fadeOut()
        ) {
            HollamMapScreen(
                onDismiss = {
                    verticalSheet = HollamVerticalSheet.NONE
                },
                onQuickSnap = {
                    verticalSheet = HollamVerticalSheet.NONE
                }
            )
        }
    }
}
