package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.HollamCyberCyan
import com.example.ui.theme.HollamElectricViolet
import com.example.ui.theme.HollamHotPink
import com.example.ui.theme.HollamNeonLime

/**
 * Official Hollam Mascot Icon with animated spectral aura
 */
@Composable
fun HollamSpectralCameraIcon(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    isAnimated: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "spectral_rotation")
    val rotationAngle by if (isAnimated) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    val pulseScale by if (isAnimated) {
        infiniteTransition.animateFloat(
            initialValue = 0.94f,
            targetValue = 1.04f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse"
        )
    } else {
        androidx.compose.runtime.remember { androidx.compose.runtime.mutableFloatStateOf(1f) }
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Outer rotating spectral ring
        Canvas(modifier = Modifier.size(size)) {
            val strokeWidth = size.toPx() * 0.08f
            val radius = (size.toPx() - strokeWidth) / 2f * pulseScale
            val centerOffset = Offset(size.toPx() / 2f, size.toPx() / 2f)

            rotate(rotationAngle, pivot = centerOffset) {
                val sweepBrush = Brush.sweepGradient(
                    colors = listOf(
                        HollamNeonLime,
                        HollamCyberCyan,
                        HollamElectricViolet,
                        HollamHotPink,
                        HollamNeonLime
                    ),
                    center = centerOffset
                )

                drawCircle(
                    brush = sweepBrush,
                    radius = radius,
                    center = centerOffset,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        // Centered Official Mascot Logo
        val mascotSize = size * 0.82f
        Image(
            painter = painterResource(id = R.drawable.ic_hollam_mascot),
            contentDescription = "Hollam Official Mascot",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(mascotSize)
                .clip(CircleShape)
                .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
        )
    }
}

/**
 * Full Hollam Logo with typography and official mascot icon
 */
@Composable
fun HollamLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 28.dp,
    fontSize: Int = 24,
    showTagline: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_hollam_mascot),
            contentDescription = "hollam",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(iconSize)
                .clip(CircleShape)
                .border(1.5.dp, Color.White.copy(alpha = 0.9f), CircleShape)
        )

        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp
                    )
                ) {
                    append("hollam")
                }
            },
            fontSize = fontSize.sp,
            fontFamily = FontFamily.SansSerif
        )
    }
}

/**
 * Official Hollam App Icon Badge (Rounded Squircle for Launch/Auth/Settings)
 */
@Composable
fun HollamOfficialAppIconBadge(
    modifier: Modifier = Modifier,
    size: Dp = 90.dp,
    cornerRadius: Dp = size * 0.22f
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius))
            .border(2.dp, Color.White.copy(alpha = 0.35f), RoundedCornerShape(cornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_hollam_mascot),
            contentDescription = "Hollam Official App Icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
