package com.example.model

data class FriendChat(
    val id: String,
    val name: String,
    val username: String,
    val avatarInitials: String,
    val avatarBgColor: Long,
    val lastMessage: String,
    val timeAgo: String,
    val streakDays: Int = 0,
    val unreadCount: Int = 0,
    val snapStatus: SnapStatus = SnapStatus.DELIVERED_TEXT,
    val isOnline: Boolean = false
)

enum class SnapStatus {
    RECEIVED_NEW_SNAP,
    RECEIVED_NEW_VIDEO,
    SENT_OPENED,
    DELIVERED_TEXT,
    TYPING
}

data class ChatMessage(
    val id: String,
    val senderId: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val isSnap: Boolean = false
)

data class UserStory(
    val id: String,
    val authorName: String,
    val authorUsername: String,
    val avatarInitials: String,
    val avatarBgColor: Long,
    val timeAgo: String,
    val caption: String,
    val colorOverlay: Long,
    val isViewed: Boolean = false
)

data class DiscoverItem(
    val id: String,
    val title: String,
    val creator: String,
    val views: String,
    val category: String,
    val bgGradientHex1: Long,
    val bgGradientHex2: Long,
    val likesCount: Int
)

data class MapFriend(
    val id: String,
    val name: String,
    val username: String,
    val avatarInitials: String,
    val avatarBgColor: Long,
    val locationName: String,
    val liveStatus: String,
    val statusEmoji: String,
    val relativeX: Float, // 0f..1f within map viewport
    val relativeY: Float, // 0f..1f within map viewport
    val lastSeenMinutes: Int
)

data class CameraFilter(
    val id: String,
    val name: String,
    val iconName: String,
    val tintColor: Long,
    val description: String
)
