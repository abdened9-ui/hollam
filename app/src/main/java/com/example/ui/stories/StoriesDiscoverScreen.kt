package com.example.ui.stories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DiscoverItem
import com.example.model.HollamDataProvider
import com.example.model.UserStory
import com.example.ui.components.HollamSpectralCameraIcon
import com.example.ui.theme.HollamBlack
import com.example.ui.theme.HollamBorder
import com.example.ui.theme.HollamCyberCyan
import com.example.ui.theme.HollamDarkElevated
import com.example.ui.theme.HollamDarkSurface
import com.example.ui.theme.HollamDeepViolet
import com.example.ui.theme.HollamElectricViolet
import com.example.ui.theme.HollamHotPink
import com.example.ui.theme.HollamNeonLime
import com.example.ui.theme.HollamTextMuted
import com.example.ui.theme.HollamTextSecondary
import kotlinx.coroutines.delay

@Composable
fun StoriesDiscoverScreen(
    modifier: Modifier = Modifier,
    onNavigateBackToCamera: () -> Unit = {}
) {
    val stories = remember { HollamDataProvider.sampleStories }
    val discoverItems = remember {
        mutableStateListOf<DiscoverItem>().apply {
            addAll(HollamDataProvider.sampleDiscover)
        }
    }

    var activeStoryIndex by remember { mutableIntStateOf(-1) }
    var selectedCategory by remember { mutableStateOf("الكل") }

    val categories = listOf("الكل", "شائع الآن 🔥", "تكنولوجيا ⚡", "سفر 🗺️", "تحديات 🎯")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HollamBlack)
            .statusBarsPadding()
            .testTag("stories_discover_screen_root")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HollamSpectralCameraIcon(size = 28.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "القصص والاستكشاف",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                IconButton(
                    onClick = onNavigateBackToCamera,
                    modifier = Modifier
                        .background(HollamDarkElevated, CircleShape)
                        .size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "العودة للكاميرا",
                        tint = HollamNeonLime,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // LazyColumn containing Stories Row, Categories, and Discover Feed
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                // Section 1: Stories
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "قصص الأصدقاء الفورية 🌟",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "مشاهدة الكل",
                                fontSize = 12.sp,
                                color = HollamNeonLime,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            items(stories.size) { index ->
                                val story = stories[index]
                                StoryReelItem(
                                    story = story,
                                    isMyStory = index == 0,
                                    onClick = {
                                        if (index == 0) {
                                            onNavigateBackToCamera()
                                        } else {
                                            activeStoryIndex = index
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // Section 2: Discover Categories
                item {
                    Column {
                        Text(
                            text = "المحتوى الشائع والمستكشف 🔥",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(categories) { cat ->
                                val isSelected = cat == selectedCategory
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            if (isSelected) HollamNeonLime else HollamDarkElevated
                                        )
                                        .clickable { selectedCategory = cat }
                                        .padding(horizontal = 14.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = cat,
                                        color = if (isSelected) HollamBlack else Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }

                // Section 3: Discover Grid Cards
                items(discoverItems, key = { it.id }) { item ->
                    DiscoverCard(
                        item = item,
                        onLike = {
                            val idx = discoverItems.indexOf(item)
                            if (idx != -1) {
                                discoverItems[idx] = item.copy(likesCount = item.likesCount + 1)
                            }
                        }
                    )
                }
            }
        }

        // Fullscreen Story Viewer
        if (activeStoryIndex in stories.indices) {
            FullscreenStoryViewer(
                story = stories[activeStoryIndex],
                onClose = { activeStoryIndex = -1 },
                onNext = {
                    if (activeStoryIndex < stories.lastIndex) {
                        activeStoryIndex++
                    } else {
                        activeStoryIndex = -1
                    }
                },
                onPrevious = {
                    if (activeStoryIndex > 1) {
                        activeStoryIndex--
                    }
                }
            )
        }
    }
}

@Composable
private fun StoryReelItem(
    story: UserStory,
    isMyStory: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag("story_item_${story.id}")
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            val ringBrush = if (isMyStory) {
                Brush.sweepGradient(listOf(HollamElectricViolet, HollamDeepViolet))
            } else if (!story.isViewed) {
                Brush.sweepGradient(listOf(HollamNeonLime, HollamCyberCyan, HollamElectricViolet, HollamNeonLime))
            } else {
                Brush.sweepGradient(listOf(HollamBorder, HollamBorder))
            }

            Box(
                modifier = Modifier
                    .size(68.dp)
                    .border(2.5.dp, ringBrush, CircleShape)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(Color(story.avatarBgColor)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = story.avatarInitials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            if (isMyStory) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(HollamNeonLime, CircleShape)
                        .border(1.5.dp, HollamBlack, CircleShape)
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "إضافة قصة",
                        tint = HollamBlack,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = story.authorName,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
private fun DiscoverCard(
    item: DiscoverItem,
    onLike: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .testTag("discover_card_${item.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = HollamDarkElevated)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(item.bgGradientHex1),
                            Color(item.bgGradientHex2),
                            HollamBlack
                        )
                    )
                )
                .padding(16.dp)
        ) {
            // Category Badge
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = item.category,
                    color = HollamNeonLime,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Views pill
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Visibility,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = item.views,
                    color = Color.White,
                    fontSize = 11.sp
                )
            }

            // Content info at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(0.75f)
            ) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.creator,
                    color = HollamCyberCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // Interactive like and play action
            Column(
                modifier = Modifier.align(Alignment.BottomEnd),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = {
                        isLiked = !isLiked
                        onLike()
                    },
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .size(38.dp)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "إعجاب",
                        tint = if (isLiked) HollamHotPink else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "${item.likesCount}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 10.sp
                )
            }
        }
    }
}

/**
 * Fullscreen Interactive Story Viewer
 */
@Composable
private fun FullscreenStoryViewer(
    story: UserStory,
    onClose: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }
    var replyText by remember { mutableStateOf("") }

    LaunchedEffect(story.id) {
        progress = 0f
        while (progress < 1f) {
            delay(50)
            progress += 0.012f
        }
        onNext()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HollamBlack)
            .statusBarsPadding()
            .testTag("fullscreen_story_viewer")
    ) {
        // Story Canvas Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(story.colorOverlay),
                            HollamBlack
                        )
                    )
                )
                .clickable { onNext() }
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HollamSpectralCameraIcon(size = 80.dp)
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = story.caption,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 32.sp
                )
            }
        }

        // Top Story Progress Bar & Author Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = HollamNeonLime,
                trackColor = Color.White.copy(alpha = 0.3f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color(story.avatarBgColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = story.avatarInitials,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = story.authorName,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = story.timeAgo,
                            color = HollamNeonLime,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White)
                }
            }
        }

        // Bottom Reply Bar
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = replyText,
                onValueChange = { replyText = it },
                placeholder = { Text("رد على القصة...", color = HollamTextMuted, fontSize = 13.sp) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = HollamDarkElevated,
                    unfocusedContainerColor = HollamDarkElevated,
                    focusedBorderColor = HollamNeonLime,
                    unfocusedBorderColor = HollamBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            IconButton(
                onClick = {
                    replyText = ""
                },
                modifier = Modifier
                    .background(HollamNeonLime, CircleShape)
                    .size(46.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "إرسال",
                    tint = HollamBlack,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
