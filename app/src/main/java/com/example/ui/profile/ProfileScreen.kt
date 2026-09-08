package com.example.ui.profile

import android.widget.Toast
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ui.theme.HollamTextSecondary

import androidx.compose.material.icons.automirrored.filled.ExitToApp
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    var isGhostModeEnabled by remember { mutableStateOf(false) }
    var areNotificationsEnabled by remember { mutableStateOf(true) }
    var showSnapcodeDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HollamBlack)
            .statusBarsPadding()
            .testTag("profile_screen_root")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.background(HollamDarkElevated, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White)
                }

                Text(
                    text = "الملف الشخصي والإعدادات",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                IconButton(
                    onClick = {
                        Toast.makeText(context, "تم نسخ رابط الحساب hollam.me/alex", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.background(HollamDarkElevated, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "مشاركة", tint = HollamNeonLime)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                // Profile Card with Holographic Snapcode
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("profile_user_card"),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = HollamDarkSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Avatar with Official Mascot & Spectral Ring
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.clickable { showSnapcodeDialog = true }
                            ) {
                                HollamSpectralCameraIcon(size = 96.dp)
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Text(
                                text = "أليكس هولام",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "@alex_hollam",
                                fontSize = 14.sp,
                                color = HollamNeonLime,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Score & VIP Badge
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .background(HollamDarkElevated, RoundedCornerShape(14.dp))
                                        .border(1.dp, HollamNeonLime.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "⚡ 48,290 نقطة لحظية",
                                        color = HollamNeonLime,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .background(HollamDarkElevated, RoundedCornerShape(14.dp))
                                        .border(1.dp, HollamElectricViolet.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "👑 hollam VIP",
                                        color = HollamElectricViolet,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Stats Row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(modifier = Modifier.weight(1f), title = "اللقطات 📸", value = "1,420")
                        StatCard(modifier = Modifier.weight(1f), title = "أعلى سلسلة 🔥", value = "112 يوم")
                        StatCard(modifier = Modifier.weight(1f), title = "الأوسمة 🏆", value = "16 وسام")
                    }
                }

                // Interactive Hollam Snapcode Banner
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showSnapcodeDialog = true },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = HollamDarkElevated)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.QrCode,
                                    contentDescription = null,
                                    tint = HollamNeonLime,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "رمز hollam Snapcode",
                                        color = Color.White,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "امسح الرمز لإضافة حسابك فوراً",
                                        color = HollamTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                            Text(text = "عرض", color = HollamNeonLime, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                // Settings & Privacy Header
                item {
                    Text(
                        text = "الخصوصية والتحكم ⚙️",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Ghost Mode Toggle
                item {
                    SettingToggleRow(
                        icon = Icons.Default.VisibilityOff,
                        iconTint = HollamHotPink,
                        title = "وضع الشبح (Ghost Mode)",
                        subtitle = "إخفاء موقعك الجغرافي تماماً عن خريطة hollam",
                        isChecked = isGhostModeEnabled,
                        onCheckedChange = {
                            isGhostModeEnabled = it
                            Toast.makeText(
                                context,
                                if (it) "تم تفعيل وضع الشبح 👻" else "تم إيقاف وضع الشبح 📍",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }

                // Notifications Toggle
                item {
                    SettingToggleRow(
                        icon = Icons.Default.Notifications,
                        iconTint = HollamCyberCyan,
                        title = "تنبيهات اللحظات الفورية",
                        subtitle = "تنبيهك عندما يرسل الأصدقاء لقطات جديدة",
                        isChecked = areNotificationsEnabled,
                        onCheckedChange = { areNotificationsEnabled = it }
                    )
                }

                // Story Privacy
                item {
                    SettingNavRow(
                        icon = Icons.Default.Lock,
                        iconTint = HollamElectricViolet,
                        title = "خصوصية القصص",
                        value = "الأصدقاء فقط"
                    )
                }

                // App Security
                item {
                    SettingNavRow(
                        icon = Icons.Default.Security,
                        iconTint = HollamNeonLime,
                        title = "أمان الحساب والتحقق الثنائي",
                        value = "مُفعل"
                    )
                }

                // Logout Button
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                try {
                                    if (FirebaseApp.getApps(context).isNotEmpty()) {
                                        FirebaseAuth.getInstance().signOut()
                                    }
                                } catch (_: Throwable) {}
                                Toast.makeText(context, "تم تسجيل الخروج", Toast.LENGTH_SHORT).show()
                                onLogout()
                            }
                            .testTag("profile_logout_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = HollamDarkSurface)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(HollamHotPink.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                        contentDescription = null,
                                        tint = HollamHotPink,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "تسجيل الخروج",
                                    color = HollamHotPink,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Snapcode Modal Dialog
        if (showSnapcodeDialog) {
            SnapcodeModal(onClose = { showSnapcodeDialog = false })
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HollamDarkSurface)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, color = HollamTextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
private fun SettingToggleRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HollamDarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(HollamDarkElevated, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = subtitle, color = HollamTextSecondary, fontSize = 11.sp)
                }
            }
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = HollamBlack,
                    checkedTrackColor = HollamNeonLime,
                    uncheckedThumbColor = HollamTextSecondary,
                    uncheckedTrackColor = HollamDarkElevated
                )
            )
        }
    }
}

@Composable
private fun SettingNavRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    title: String,
    value: String
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = HollamDarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(HollamDarkElevated, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = title, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Text(text = value, color = HollamNeonLime, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SnapcodeModal(onClose: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable { onClose() }
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = false) {},
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = HollamDarkSurface)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "رمز hollam Snapcode الخاص بك",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Geometric Snapcode Canvas with Spectral Center
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color(0xFF13111C), RoundedCornerShape(24.dp))
                        .border(2.dp, HollamNeonLime, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(170.dp)) {
                        // Futuristic code dots
                        val step = 14.dp.toPx()
                        var x = 0f
                        while (x < size.width) {
                            var y = 0f
                            while (y < size.height) {
                                if ((x.toInt() + y.toInt()) % 3 == 0) {
                                    drawCircle(
                                        color = HollamElectricViolet.copy(alpha = 0.35f),
                                        radius = 2.dp.toPx(),
                                        center = Offset(x, y)
                                    )
                                }
                                y += step
                            }
                            x += step
                        }
                    }

                    // Centered Spectral Aperture
                    HollamSpectralCameraIcon(size = 80.dp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "@alex_hollam",
                    color = HollamNeonLime,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.background(HollamDarkElevated, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White)
                }
            }
        }
    }
}
