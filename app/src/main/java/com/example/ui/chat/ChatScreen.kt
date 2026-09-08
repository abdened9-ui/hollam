package com.example.ui.chat

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.model.ChatMessage
import com.example.model.FriendChat
import com.example.model.HollamDataProvider
import com.example.model.SnapStatus
import com.example.ui.components.HollamSpectralCameraIcon
import com.example.ui.theme.HollamBlack
import com.example.ui.theme.HollamBorder
import com.example.ui.theme.HollamCyberCyan
import com.example.ui.theme.HollamDarkElevated
import com.example.ui.theme.HollamDarkSurface
import com.example.ui.theme.HollamElectricViolet
import com.example.ui.theme.HollamHotPink
import com.example.ui.theme.HollamNeonLime
import com.example.ui.theme.HollamTextMuted
import com.example.ui.theme.HollamTextPrimary
import com.example.ui.theme.HollamTextSecondary

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    onNavigateBackToCamera: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterTab by remember { mutableStateOf(0) } // 0: All, 1: Unread, 2: Streaks
    var activeConversationFriend by remember { mutableStateOf<FriendChat?>(null) }

    val friendsList = remember {
        mutableStateListOf<FriendChat>().apply {
            addAll(HollamDataProvider.sampleFriends)
        }
    }

    val filteredFriends = friendsList.filter { friend ->
        val matchesQuery = friend.name.contains(searchQuery, ignoreCase = true) ||
                friend.username.contains(searchQuery, ignoreCase = true)
        when (selectedFilterTab) {
            1 -> matchesQuery && friend.unreadCount > 0
            2 -> matchesQuery && friend.streakDays > 0
            else -> matchesQuery
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HollamBlack)
            .statusBarsPadding()
            .testTag("chat_screen_root")
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
                        text = "المحادثات",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("chat_search_field"),
                placeholder = {
                    Text(
                        text = "ابحث عن أصدقاء أو محادثات...",
                        color = HollamTextMuted,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = HollamTextSecondary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = HollamDarkSurface,
                    unfocusedContainerColor = HollamDarkSurface,
                    focusedBorderColor = HollamElectricViolet,
                    unfocusedBorderColor = HollamBorder,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Active Streaks Quick Row
            Text(
                text = "سلاسل اللحظات الفورية 🔥",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = HollamNeonLime
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                contentPadding = PaddingValues(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(friendsList.filter { it.streakDays > 0 }) { friend ->
                    StreakAvatarItem(
                        friend = friend,
                        onClick = { activeConversationFriend = friend }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Filter Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf("الكل", "غير مقروء 💬", "السلاسل 🔥").forEachIndexed { index, title ->
                    val isSelected = selectedFilterTab == index
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) HollamElectricViolet else HollamDarkElevated)
                            .clickable { selectedFilterTab = index }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = title,
                            color = if (isSelected) Color.White else HollamTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Conversations List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(filteredFriends, key = { it.id }) { friend ->
                    FriendChatRow(
                        friend = friend,
                        onClick = { activeConversationFriend = friend },
                        onQuickSnap = onNavigateBackToCamera
                    )
                }
            }
        }

        // Floating Action Button for New Chat
        FloatingActionButton(
            onClick = { /* New conversation action */ },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("new_chat_fab"),
            containerColor = HollamNeonLime,
            contentColor = HollamBlack,
            shape = CircleShape
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = "محادثة جديدة")
        }

        // Direct Messaging Thread Dialog / Sheet
        activeConversationFriend?.let { friend ->
            ChatThreadView(
                friend = friend,
                onDismiss = { activeConversationFriend = null },
                onSendSnap = onNavigateBackToCamera
            )
        }
    }
}

@Composable
private fun StreakAvatarItem(
    friend: FriendChat,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag("streak_avatar_${friend.id}")
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .border(2.dp, HollamNeonLime, CircleShape)
                .padding(3.dp)
                .clip(CircleShape)
                .background(Color(friend.avatarBgColor)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = friend.avatarInitials,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🔥 ${friend.streakDays}", fontSize = 11.sp, color = HollamNeonLime, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FriendChatRow(
    friend: FriendChat,
    onClick: () -> Unit,
    onQuickSnap: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("friend_chat_${friend.id}"),
        colors = CardDefaults.cardColors(containerColor = HollamDarkSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Avatar with online dot
                Box {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(friend.avatarBgColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = friend.avatarInitials,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    if (friend.isOnline) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(HollamNeonLime, CircleShape)
                                .border(2.dp, HollamBlack, CircleShape)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = friend.name,
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (friend.streakDays > 0) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🔥 ${friend.streakDays}",
                                fontSize = 12.sp,
                                color = HollamNeonLime,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Snap status indicator icon
                        val (statusColor, statusIcon) = when (friend.snapStatus) {
                            SnapStatus.RECEIVED_NEW_SNAP -> HollamHotPink to "📸 لقطة جديدة"
                            SnapStatus.RECEIVED_NEW_VIDEO -> HollamElectricViolet to "🎥 فيديو جديد"
                            SnapStatus.TYPING -> HollamNeonLime to "يكتب الآن..."
                            SnapStatus.SENT_OPENED -> HollamTextMuted to "تم الفتح"
                            else -> HollamCyberCyan to friend.lastMessage
                        }

                        Text(
                            text = "$statusIcon • ${friend.timeAgo}",
                            color = statusColor,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            // Quick Camera Snap Button
            IconButton(
                onClick = onQuickSnap,
                modifier = Modifier
                    .size(36.dp)
                    .background(HollamDarkElevated, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "لقطة سريعة",
                    tint = HollamNeonLime,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

/**
 * Interactive Chat View for conversation with friends
 */
@Composable
private fun ChatThreadView(
    friend: FriendChat,
    onDismiss: () -> Unit,
    onSendSnap: () -> Unit
) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage("m1", friend.id, "أهلاً! كيف كانت أجواء اليوم؟", "10:14 م", isFromMe = false),
            ChatMessage("m2", "me", "ممتازة جداً، التقطت لقطة مميزة من برج المملكة!", "10:15 م", isFromMe = true),
            ChatMessage("m3", friend.id, "شاركنياها عبر كاميرا hollam فوراً! 🔥", "10:16 م", isFromMe = false)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HollamBlack)
            .statusBarsPadding()
            .testTag("chat_thread_view")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Thread Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HollamDarkSurface)
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(friend.avatarBgColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = friend.avatarInitials, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(text = friend.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(text = "@${friend.username}", color = HollamNeonLime, fontSize = 11.sp)
                    }
                }

                Row {
                    IconButton(onClick = onSendSnap) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = "كاميرا", tint = HollamNeonLime)
                    }
                }
            }

            // Message History
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(messages) { msg ->
                    val isMe = msg.isFromMe
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (isMe) Arrangement.End else Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = if (isMe) 16.dp else 4.dp,
                                        bottomEnd = if (isMe) 4.dp else 16.dp
                                    )
                                )
                                .background(
                                    if (isMe) HollamElectricViolet
                                    else HollamDarkElevated
                                )
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Column {
                                Text(
                                    text = msg.text,
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = msg.timestamp,
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 10.sp,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }
                }
            }

            // Input Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HollamDarkSurface)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onSendSnap,
                    modifier = Modifier.background(HollamNeonLime, CircleShape).size(38.dp)
                ) {
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = HollamBlack, modifier = Modifier.size(20.dp))
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("أرسل رسالة...", color = HollamTextMuted, fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = HollamDarkElevated,
                        unfocusedContainerColor = HollamDarkElevated,
                        focusedBorderColor = HollamElectricViolet,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            messages.add(
                                ChatMessage(
                                    id = "m_${System.currentTimeMillis()}",
                                    senderId = "me",
                                    text = messageText,
                                    timestamp = "الآن",
                                    isFromMe = true
                                )
                            )
                            messageText = ""
                        }
                    },
                    modifier = Modifier.background(HollamElectricViolet, CircleShape).size(38.dp)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "إرسال", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
