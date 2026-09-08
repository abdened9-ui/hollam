package com.example.model

object HollamDataProvider {

    val sampleFriends = listOf(
        FriendChat(
            id = "f1",
            name = "سارة أحمد",
            username = "sara_design",
            avatarInitials = "SA",
            avatarBgColor = 0xFFA855F7,
            lastMessage = "أرسلت لقطة جديدة 📸",
            timeAgo = "الآن",
            streakDays = 47,
            unreadCount = 2,
            snapStatus = SnapStatus.RECEIVED_NEW_SNAP,
            isOnline = true
        ),
        FriendChat(
            id = "f2",
            name = "عمر الخالد",
            username = "omar_tech",
            avatarInitials = "OK",
            avatarBgColor = 0xFF00FF87,
            lastMessage = "يكتب الآن...",
            timeAgo = "1 د",
            streakDays = 112,
            unreadCount = 0,
            snapStatus = SnapStatus.TYPING,
            isOnline = true
        ),
        FriendChat(
            id = "f3",
            name = "ليلى حسن",
            username = "layla_art",
            avatarInitials = "LH",
            avatarBgColor = 0xFF00F0FF,
            lastMessage = "فيديو لحظي جديد 🎥",
            timeAgo = "12 د",
            streakDays = 29,
            unreadCount = 1,
            snapStatus = SnapStatus.RECEIVED_NEW_VIDEO,
            isOnline = false
        ),
        FriendChat(
            id = "f4",
            name = "ياسين المنصور",
            username = "yassine_m",
            avatarInitials = "YM",
            avatarBgColor = 0xFFFF007A,
            lastMessage = "تم فتح اللقطة منذ 2 س",
            timeAgo = "2 س",
            streakDays = 85,
            unreadCount = 0,
            snapStatus = SnapStatus.SENT_OPENED,
            isOnline = false
        ),
        FriendChat(
            id = "f5",
            name = "نور الهدى",
            username = "nour_moment",
            avatarInitials = "NH",
            avatarBgColor = 0xFFFFB703,
            lastMessage = "التقينا اليوم في مقهى بوليفارد!",
            timeAgo = "3 س",
            streakDays = 14,
            unreadCount = 0,
            snapStatus = SnapStatus.DELIVERED_TEXT,
            isOnline = true
        ),
        FriendChat(
            id = "f6",
            name = "كريم سامي",
            username = "kareem_dev",
            avatarInitials = "KS",
            avatarBgColor = 0xFF6366F1,
            lastMessage = "تم فتح المحادثة",
            timeAgo = "أمس",
            streakDays = 6,
            unreadCount = 0,
            snapStatus = SnapStatus.SENT_OPENED,
            isOnline = false
        )
    )

    val sampleStories = listOf(
        UserStory(
            id = "s0",
            authorName = "قصتي",
            authorUsername = "alex_hollam",
            avatarInitials = "ME",
            avatarBgColor = 0xFF7928CA,
            timeAgo = "أضف قصة +",
            caption = "شارك لحظتك الآن عبر كاميرا hollam!",
            colorOverlay = 0xFF181824,
            isViewed = true
        ),
        UserStory(
            id = "s1",
            authorName = "سارة أحمد",
            authorUsername = "sara_design",
            avatarInitials = "SA",
            avatarBgColor = 0xFFA855F7,
            timeAgo = "منذ 15 د",
            caption = "أجواء الرياض الليلة لا توصف! سماء صافية ونسيم هادئ 🌃✨",
            colorOverlay = 0xFF240046,
            isViewed = false
        ),
        UserStory(
            id = "s2",
            authorName = "عمر الخالد",
            authorUsername = "omar_tech",
            avatarInitials = "OK",
            avatarBgColor = 0xFF00FF87,
            timeAgo = "منذ 45 د",
            caption = "تجربة أول كاميرا سريعة متصلة بشبكة hollam الفورية ⚡🚀",
            colorOverlay = 0xFF0A2E1C,
            isViewed = false
        ),
        UserStory(
            id = "s3",
            authorName = "ليلى حسن",
            authorUsername = "layla_art",
            avatarInitials = "LH",
            avatarBgColor = 0xFF00F0FF,
            timeAgo = "منذ 2 س",
            caption = "لوحة رقمية جديدة مستوحاة من الأشكال الطيفية 🎨🔮",
            colorOverlay = 0xFF072738,
            isViewed = false
        ),
        UserStory(
            id = "s4",
            authorName = "ياسين المنصور",
            authorUsername = "yassine_m",
            avatarInitials = "YM",
            avatarBgColor = 0xFFFF007A,
            timeAgo = "منذ 5 س",
            caption = "مباراة ملحمية في استاد الملك فهد ⚽🔥",
            colorOverlay = 0xFF4A0022,
            isViewed = true
        )
    )

    val sampleDiscover = listOf(
        DiscoverItem(
            id = "d1",
            title = "أفضل الأماكن السياحية المخفية في الخليج",
            creator = "مستكشف المدن 🗺️",
            views = "1.4M",
            category = "سفر واستكشاف",
            bgGradientHex1 = 0xFF1E1B4B,
            bgGradientHex2 = 0xFF4338CA,
            likesCount = 42800
        ),
        DiscoverItem(
            id = "d2",
            title = "ابتكارات الذكاء الاصطناعي والكاميرات اللحظية 2026",
            creator = "تك هب ⚡",
            views = "890K",
            category = "تكنولوجيا",
            bgGradientHex1 = 0xFF064E3B,
            bgGradientHex2 = 0xFF047857,
            likesCount = 31200
        ),
        DiscoverItem(
            id = "d3",
            title = "تحدي الطبخ السريع في 60 ثانية فقط!",
            creator = "شيف اللحظة 🍳",
            views = "2.1M",
            category = "تحديات",
            bgGradientHex1 = 0xFF701A75,
            bgGradientHex2 = 0xFFC026D3,
            likesCount = 89400
        ),
        DiscoverItem(
            id = "d4",
            title = "بث مباشر للموسيقى التصويرية الهادئة",
            creator = "ألحان الفضاء 🎵",
            views = "650K",
            category = "موسيقى",
            bgGradientHex1 = 0xFF1E293B,
            bgGradientHex2 = 0xFF0F172A,
            likesCount = 18600
        )
    )

    val sampleMapFriends = listOf(
        MapFriend(
            id = "mf1",
            name = "سارة",
            username = "sara_design",
            avatarInitials = "SA",
            avatarBgColor = 0xFFA855F7,
            locationName = "مقهى السحاب - الطابق 40",
            liveStatus = "تستمتع بقهوة مختصة ☕",
            statusEmoji = "☕",
            relativeX = 0.32f,
            relativeY = 0.38f,
            lastSeenMinutes = 2
        ),
        MapFriend(
            id = "mf2",
            name = "عمر",
            username = "omar_tech",
            avatarInitials = "OK",
            avatarBgColor = 0xFF00FF87,
            locationName = "حي السفارات - الحديقة البيئية",
            liveStatus = "رياضة المشي المسائية 🏃",
            statusEmoji = "🏃",
            relativeX = 0.72f,
            relativeY = 0.45f,
            lastSeenMinutes = 5
        ),
        MapFriend(
            id = "mf3",
            name = "ليلى",
            username = "layla_art",
            avatarInitials = "LH",
            avatarBgColor = 0xFF00F0FF,
            locationName = "معرض الفن الحديث",
            liveStatus = "تجهيز المعرض الجديد 🎨",
            statusEmoji = "🎨",
            relativeX = 0.50f,
            relativeY = 0.68f,
            lastSeenMinutes = 15
        ),
        MapFriend(
            id = "mf4",
            name = "ياسين",
            username = "yassine_m",
            avatarInitials = "YM",
            avatarBgColor = 0xFFFF007A,
            locationName = "المركز المالي (KAFD)",
            liveStatus = "في اجتماع عمل سريع 💼",
            statusEmoji = "💼",
            relativeX = 0.25f,
            relativeY = 0.72f,
            lastSeenMinutes = 20
        ),
        MapFriend(
            id = "mf5",
            name = "أنا (الموقع الحالي)",
            username = "alex_hollam",
            avatarInitials = "ME",
            avatarBgColor = 0xFF7928CA,
            locationName = "البوليفارد وورلد",
            liveStatus = "أبث لحظتي الآن 📸",
            statusEmoji = "⚡",
            relativeX = 0.54f,
            relativeY = 0.30f,
            lastSeenMinutes = 0
        )
    )

    val sampleCameraFilters = listOf(
        CameraFilter("f0", "طبيعي", "flare", 0xFFFFFFFF, "بدون فلتر"),
        CameraFilter("f1", "سيبيربانك", "cyber", 0xFF00F0FF, "ألوان نيون مستقبلية"),
        CameraFilter("f2", "طيف متوهج", "spectral", 0xFFA855F7, "هالة ضوئية متدرجة"),
        CameraFilter("f3", "فسفور نايت", "neon", 0xFF00FF87, "تباين فسفوري مشع"),
        CameraFilter("f4", "غروب ذهبي", "sunset", 0xFFFFB703, "إضاءة دافئة سينمائية"),
        CameraFilter("f5", "ريترو كلاسيك", "vhs", 0xFFFF007A, "طابع تسعينات كلاسيكي")
    )
}
