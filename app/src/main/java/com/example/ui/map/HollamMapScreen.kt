package com.example.ui.map

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterCenterFocus
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HollamDataProvider
import com.example.model.MapFriend
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
import kotlin.math.roundToInt

@Composable
fun HollamMapScreen(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onQuickSnap: () -> Unit = {}
) {
    val context = LocalContext.current
    val friends = remember { HollamDataProvider.sampleMapFriends }
    var selectedFriend by remember { mutableStateOf<MapFriend?>(null) }
    var showHeatmap by remember { mutableStateOf(true) }

    // Map Pan and Zoom State
    var mapOffsetX by remember { mutableFloatStateOf(0f) }
    var mapOffsetY by remember { mutableFloatStateOf(0f) }
    var mapZoom by remember { mutableFloatStateOf(1f) }

    // Pulsing beacon animation for live markers
    val infiniteTransition = rememberInfiniteTransition(label = "beacon_pulse")
    val beaconRadiusPulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beacon_radius"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HollamBlack)
            .statusBarsPadding()
            .testTag("hollam_map_screen_root")
    ) {
        // Interactive Map Canvas
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        mapZoom = (mapZoom * zoom).coerceIn(0.7f, 2.5f)
                        mapOffsetX += pan.x
                        mapOffsetY += pan.y
                    }
                }
        ) {
            val widthPx = constraints.maxWidth.toFloat()
            val heightPx = constraints.maxHeight.toFloat()

            // Futuristic Dark Map Background Grid & Roads
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = (widthPx / 2f) + mapOffsetX
                val cy = (heightPx / 2f) + mapOffsetY

                // Dark map background with subtle district gradients
                drawRect(Color(0xFF090B10))

                // Map Grid Lines
                val gridSize = 60.dp.toPx() * mapZoom
                var gx = (mapOffsetX % gridSize)
                while (gx < size.width) {
                    drawLine(
                        color = Color(0xFF131A26),
                        start = Offset(gx, 0f),
                        end = Offset(gx, size.height),
                        strokeWidth = 1f
                    )
                    gx += gridSize
                }
                var gy = (mapOffsetY % gridSize)
                while (gy < size.height) {
                    drawLine(
                        color = Color(0xFF131A26),
                        start = Offset(0f, gy),
                        end = Offset(size.width, gy),
                        strokeWidth = 1f
                    )
                    gy += gridSize
                }

                // Geometric Road Lines
                val roadColor = Color(0xFF1C2638)
                val primaryAvenueColor = Color(0xFF26334D)

                // Diagonal Highways
                drawLine(
                    color = primaryAvenueColor,
                    start = Offset(cx - 350f * mapZoom, cy - 450f * mapZoom),
                    end = Offset(cx + 400f * mapZoom, cy + 450f * mapZoom),
                    strokeWidth = 6f * mapZoom,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = roadColor,
                    start = Offset(cx - 450f * mapZoom, cy + 120f * mapZoom),
                    end = Offset(cx + 450f * mapZoom, cy - 180f * mapZoom),
                    strokeWidth = 4f * mapZoom
                )
                drawLine(
                    color = roadColor,
                    start = Offset(cx - 100f * mapZoom, cy - 500f * mapZoom),
                    end = Offset(cx - 80f * mapZoom, cy + 500f * mapZoom),
                    strokeWidth = 4f * mapZoom
                )

                // Circular Ring Road
                drawCircle(
                    color = primaryAvenueColor,
                    radius = 260f * mapZoom,
                    center = Offset(cx, cy),
                    style = Stroke(width = 3.5f * mapZoom)
                )

                // Heatmap Hotspots
                if (showHeatmap) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(HollamHotPink.copy(alpha = 0.35f), Color.Transparent),
                            center = Offset(cx + 80f * mapZoom, cy - 60f * mapZoom),
                            radius = 110f * mapZoom
                        ),
                        radius = 110f * mapZoom,
                        center = Offset(cx + 80f * mapZoom, cy - 60f * mapZoom)
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(HollamNeonLime.copy(alpha = 0.25f), Color.Transparent),
                            center = Offset(cx - 120f * mapZoom, cy + 80f * mapZoom),
                            radius = 90f * mapZoom
                        ),
                        radius = 90f * mapZoom,
                        center = Offset(cx - 120f * mapZoom, cy + 80f * mapZoom)
                    )
                }
            }

            // Live Friend Geographic Markers
            friends.forEach { friend ->
                val basePinX = (friend.relativeX * widthPx * mapZoom) + mapOffsetX
                val basePinY = (friend.relativeY * heightPx * mapZoom) + mapOffsetY

                LiveFriendMapMarker(
                    friend = friend,
                    offsetX = basePinX,
                    offsetY = basePinY,
                    beaconPulse = beaconRadiusPulse,
                    onClick = { selectedFriend = friend }
                )
            }
        }

        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .background(HollamDarkSurface.copy(alpha = 0.9f), RoundedCornerShape(20.dp))
                    .border(1.dp, HollamCyberCyan.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HollamSpectralCameraIcon(size = 22.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "خريطة hollam الحية",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Heatmap toggle button
                IconButton(
                    onClick = { showHeatmap = !showHeatmap },
                    modifier = Modifier
                        .size(42.dp)
                        .background(HollamDarkSurface.copy(alpha = 0.85f), CircleShape)
                        .border(1.dp, if (showHeatmap) HollamHotPink else HollamBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = "النقاط الساخنة",
                        tint = if (showHeatmap) HollamHotPink else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Reset to my location button
                IconButton(
                    onClick = {
                        mapOffsetX = 0f
                        mapOffsetY = 0f
                        mapZoom = 1f
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .background(HollamDarkSurface.copy(alpha = 0.85f), CircleShape)
                        .border(1.dp, HollamNeonLime.copy(alpha = 0.4f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "موقعي",
                        tint = HollamNeonLime,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Close map button (returns to Camera)
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(42.dp)
                        .background(HollamDarkElevated, CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White)
                }
            }
        }

        // Live Active Friends Quick Pill Bar at Bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Friend Detail Bottom Sheet
            AnimatedVisibility(
                visible = selectedFriend != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                selectedFriend?.let { friend ->
                    FriendLocationDetailCard(
                        friend = friend,
                        onClose = { selectedFriend = null },
                        onSendSnap = onQuickSnap,
                        onChat = {
                            Toast.makeText(context, "فتح محادثة مع ${friend.name}", Toast.LENGTH_SHORT).show()
                            selectedFriend = null
                        }
                    )
                }
            }
        }
    }
}

/**
 * Live Friend Marker on the map with status bubble & beacon pulse
 */
@Composable
private fun LiveFriendMapMarker(
    friend: MapFriend,
    offsetX: Float,
    offsetY: Float,
    beaconPulse: Float,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt() - 32, offsetY.roundToInt() - 32) }
            .clickable { onClick() }
            .testTag("map_marker_${friend.id}"),
        contentAlignment = Alignment.Center
    ) {
        // Glowing Beacon Pulse
        Canvas(modifier = Modifier.size(68.dp)) {
            val pulseColor = if (friend.id == "mf5") HollamNeonLime else HollamCyberCyan
            drawCircle(
                color = pulseColor.copy(alpha = (0.35f / beaconPulse).coerceIn(0.08f, 0.4f)),
                radius = 28.dp.toPx() * beaconPulse
            )
            drawCircle(
                color = pulseColor.copy(alpha = 0.6f),
                radius = 22.dp.toPx(),
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        // Main Friend Avatar Pin
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(friend.avatarBgColor))
                .border(2.5.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = friend.avatarInitials,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )
        }

        // Status Emoji Bubble attached to top right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(22.dp)
                .background(HollamBlack, CircleShape)
                .border(1.dp, HollamNeonLime, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = friend.statusEmoji, fontSize = 11.sp)
        }
    }
}

/**
 * Friend Location Detail Card when tapping any marker
 */
@Composable
private fun FriendLocationDetailCard(
    friend: MapFriend,
    onClose: () -> Unit,
    onSendSnap: () -> Unit,
    onChat: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("friend_location_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = HollamDarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(friend.avatarBgColor)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = friend.avatarInitials, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = friend.name,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (friend.lastSeenMinutes == 0) "متصل الآن 🟢" else "نشط منذ ${friend.lastSeenMinutes} د",
                            color = HollamNeonLime,
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.background(HollamDarkElevated, CircleShape).size(34.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إغلاق", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Location & Live Status Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HollamDarkElevated, RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "📍 ${friend.locationName}",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "الحالة: ${friend.liveStatus}",
                        color = HollamCyberCyan,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onChat,
                    modifier = Modifier.weight(1f).height(46.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HollamDarkElevated),
                    shape = RoundedCornerShape(23.dp)
                ) {
                    Icon(imageVector = Icons.Default.ChatBubble, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("محادثة", color = Color.White)
                }

                Button(
                    onClick = onSendSnap,
                    modifier = Modifier.weight(1.3f).height(46.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HollamNeonLime),
                    shape = RoundedCornerShape(23.dp)
                ) {
                    Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, tint = HollamBlack)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("لقطة سريعة 📸", color = HollamBlack, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
