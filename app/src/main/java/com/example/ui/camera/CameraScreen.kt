package com.example.ui.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FlashAuto
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import java.io.File
import com.example.model.CameraFilter
import com.example.model.HollamDataProvider
import com.example.ui.components.HollamLogo
import com.example.ui.components.HollamSpectralCameraIcon
import com.example.ui.theme.HollamBlack
import com.example.ui.theme.HollamCyberCyan
import com.example.ui.theme.HollamDarkElevated
import com.example.ui.theme.HollamDarkSurface
import com.example.ui.theme.HollamElectricViolet
import com.example.ui.theme.HollamHotPink
import com.example.ui.theme.HollamNeonLime
import kotlinx.coroutines.delay

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    onNavigateToChat: () -> Unit = {},
    onNavigateToStories: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToMap: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (!isGranted) {
            Toast.makeText(context, "يمكنك استخدام محاكي الكاميرا اللحظية", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Camera states
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var flashMode by remember { mutableIntStateOf(0) } // 0: Auto, 1: On, 2: Off
    var isGridVisible by remember { mutableStateOf(false) }
    var isNightMode by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf(HollamDataProvider.sampleCameraFilters[0]) }

    // Shutter & Capture states
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var isCapturing by remember { mutableStateOf(false) }
    var capturedPhotoUri by remember { mutableStateOf<String?>(null) }
    var isRecordingVideo by remember { mutableStateOf(false) }
    var flashAnimVisible by remember { mutableStateOf(false) }

    // Tap to focus position
    var focusPoint by remember { mutableStateOf<Offset?>(null) }

    // Real takePicture function matching Flutter's:
    // final image = await controller!.takePicture();
    // print("Captured: ${image.path}");
    fun capturePhoto() {
        val currentCapture = imageCapture
        val photoFile = File(
            context.cacheDir,
            "snap_${System.currentTimeMillis()}.jpg"
        )

        if (currentCapture != null && hasCameraPermission) {
            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
            currentCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        println("Captured: ${photoFile.absolutePath}")
                        capturedPhotoUri = photoFile.absolutePath
                        isCapturing = true
                        flashAnimVisible = true
                        Toast.makeText(context, "تم التقاط الصورة بنجاح! 📸", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        println("Captured: ${photoFile.absolutePath}")
                        capturedPhotoUri = photoFile.absolutePath
                        isCapturing = true
                        flashAnimVisible = true
                        Toast.makeText(context, "تم التقاط لحظة hollam! 📸", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        } else {
            println("Captured: ${photoFile.absolutePath}")
            capturedPhotoUri = photoFile.absolutePath
            isCapturing = true
            flashAnimVisible = true
            Toast.makeText(context, "تم التقاط لحظة hollam! 📸", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(HollamBlack)
            .testTag("camera_screen_root")
    ) {
        // 1. Camera Preview Layer
        if (hasCameraPermission) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            focusPoint = offset
                        }
                    },
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                            val capture = ImageCapture.Builder()
                                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                .build()
                            imageCapture = capture

                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(lensFacing)
                                .build()

                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                capture
                            )
                        } catch (e: Exception) {
                            // Fallback gracefully to simulated viewfinder
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                update = {
                    // Update preview if lens changed
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(it.context)
                    cameraProviderFuture.addListener({
                        try {
                            val cameraProvider = cameraProviderFuture.get()
                            val preview = Preview.Builder().build().also { p ->
                                p.setSurfaceProvider(it.surfaceProvider)
                            }
                            val capture = ImageCapture.Builder()
                                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                .build()
                            imageCapture = capture

                            val cameraSelector = CameraSelector.Builder()
                                .requireLensFacing(lensFacing)
                                .build()
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, capture)
                        } catch (_: Exception) {}
                    }, ContextCompat.getMainExecutor(it.context))
                }
            )
        } else {
            // Stylized Viewfinder Simulation with Live Spectral Grain
            SimulatedViewfinder(
                filter = selectedFilter,
                lensFacing = lensFacing,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            focusPoint = offset
                        }
                    }
            )
        }

        // Filter Tint Overlay
        if (selectedFilter.id != "f0") {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(selectedFilter.tintColor).copy(alpha = 0.15f))
            )
        }

        // Camera Grid Overlay
        if (isGridVisible) {
            CameraGridOverlay(modifier = Modifier.fillMaxSize())
        }

        // Focus Box Indicator
        focusPoint?.let { pos ->
            FocusIndicator(pos = pos) {
                focusPoint = null
            }
        }

        // Capture Flash Animation
        AnimatedVisibility(
            visible = flashAnimVisible,
            enter = fadeIn(tween(50)),
            exit = fadeOut(tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }

        // 2. Top Header Toolbar
        TopCameraToolbar(
            flashMode = flashMode,
            isGridVisible = isGridVisible,
            isNightMode = isNightMode,
            hasPermission = hasCameraPermission,
            onToggleFlash = { flashMode = (flashMode + 1) % 3 },
            onToggleGrid = { isGridVisible = !isGridVisible },
            onToggleNightMode = { isNightMode = !isNightMode },
            onRequestPermission = { permissionLauncher.launch(Manifest.permission.CAMERA) },
            onOpenProfile = onNavigateToProfile
        )

        // 3. Bottom Camera Controls & Shutter
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Filters Reel
            FiltersCarousel(
                filters = HollamDataProvider.sampleCameraFilters,
                selectedFilter = selectedFilter,
                onSelectFilter = { selectedFilter = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Shutter Row with Flip Camera and Gallery/Story shortcuts
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Quick chat/gallery shortcut
                IconButton(
                    onClick = onNavigateToChat,
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                        .testTag("camera_quick_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "المحادثات",
                        tint = HollamNeonLime,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Instant Shutter Button matching Flutter's CircleAvatar double-circle shutter button
                HollamShutterButton(
                    isRecording = isRecordingVideo,
                    onTapCapture = {
                        capturePhoto()
                    }
                )

                // Flip camera button
                IconButton(
                    onClick = {
                        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                            CameraSelector.LENS_FACING_FRONT
                        } else {
                            CameraSelector.LENS_FACING_BACK
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                        .testTag("camera_flip_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.FlipCameraAndroid,
                        contentDescription = "تبديل الكاميرا",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Swipe Guide Navigation Bar
            NavigationPillsBar(
                onNavigateToChat = onNavigateToChat,
                onNavigateToStories = onNavigateToStories,
                onNavigateToMap = onNavigateToMap
            )
        }

        // Captured Photo Review Modal
        if (isCapturing && capturedPhotoUri != null) {
            CapturedSnapDialog(
                photoUri = capturedPhotoUri,
                filter = selectedFilter,
                onDismiss = {
                    isCapturing = false
                    capturedPhotoUri = null
                },
                onSend = {
                    Toast.makeText(context, "تم إرسال اللحظة إلى الأصدقاء والقصة! 🚀", Toast.LENGTH_LONG).show()
                    isCapturing = false
                    capturedPhotoUri = null
                }
            )
        }

        LaunchedEffect(flashAnimVisible) {
            if (flashAnimVisible) {
                delay(120)
                flashAnimVisible = false
            }
        }
    }
}

@Composable
private fun TopCameraToolbar(
    flashMode: Int,
    isGridVisible: Boolean,
    isNightMode: Boolean,
    hasPermission: Boolean,
    onToggleFlash: () -> Unit,
    onToggleGrid: () -> Unit,
    onToggleNightMode: () -> Unit,
    onRequestPermission: () -> Unit,
    onOpenProfile: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile & Settings Top Button
            IconButton(
                onClick = onOpenProfile,
                modifier = Modifier
                    .size(44.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .border(1.5.dp, HollamElectricViolet, CircleShape)
                    .testTag("camera_profile_button")
            ) {
                Text(
                    text = "ME",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            // Hollam Modern Fluid Logo
            HollamLogo(
                iconSize = 26.dp,
                fontSize = 22,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )

            // Right Quick Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flash toggle
                IconButton(
                    onClick = onToggleFlash,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                ) {
                    Icon(
                        imageVector = when (flashMode) {
                            1 -> Icons.Default.FlashOn
                            2 -> Icons.Default.FlashOff
                            else -> Icons.Default.FlashAuto
                        },
                        contentDescription = "الفلاش",
                        tint = if (flashMode == 1) HollamNeonLime else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Grid toggle
                IconButton(
                    onClick = onToggleGrid,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.GridOn,
                        contentDescription = "الشبكة",
                        tint = if (isGridVisible) HollamNeonLime else Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Night mode toggle
                IconButton(
                    onClick = onToggleNightMode,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black.copy(alpha = 0.45f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Nightlight,
                        contentDescription = "الوضع الليلي",
                        tint = if (isNightMode) HollamCyberCyan else Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        if (!hasPermission) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(HollamDarkElevated.copy(alpha = 0.9f), RoundedCornerShape(12.dp))
                    .border(1.dp, HollamNeonLime.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .clickable { onRequestPermission() }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        tint = HollamNeonLime,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "اضغط لتفعيل الكاميرا المباشرة",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = "تفعيل",
                    color = HollamNeonLime,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Hollam Shutter Button with instantaneous glowing spectral ring
 */
@Composable
fun HollamShutterButton(
    isRecording: Boolean,
    onTapCapture: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "shutter_spectral_spin")
    val spinAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shutter_spin"
    )

    val scalePulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shutter_pulse"
    )

    Box(
        modifier = modifier
            .size(80.dp)
            .scale(if (isRecording) 1.12f else scalePulse)
            .clickable { onTapCapture() }
            .testTag("camera_shutter_button"),
        contentAlignment = Alignment.Center
    ) {
        // Outer CircleAvatar (radius 35 -> size 70dp, backgroundColor: Colors.white)
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(Color.White, CircleShape)
                .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Inner CircleAvatar (radius 30 -> size 60dp, backgroundColor: Colors.white, foregroundColor: Colors.grey)
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        if (isRecording) HollamHotPink else Color(0xFFF2F2F2),
                        CircleShape
                    )
                    .border(2.dp, Color(0xFFCCCCCC), CircleShape)
            )
        }
    }
}

@Composable
private fun FiltersCarousel(
    filters: List<CameraFilter>,
    selectedFilter: CameraFilter,
    onSelectFilter: (CameraFilter) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(filters) { filter ->
            val isSelected = filter.id == selectedFilter.id
            val borderColor = if (isSelected) HollamNeonLime else Color.White.copy(alpha = 0.2f)
            val textColor = if (isSelected) HollamNeonLime else Color.White.copy(alpha = 0.7f)

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) HollamDarkElevated.copy(alpha = 0.9f)
                        else Color.Black.copy(alpha = 0.4f)
                    )
                    .border(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelectFilter(filter) }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color(filter.tintColor), CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = filter.name,
                    color = textColor,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun NavigationPillsBar(
    onNavigateToChat: () -> Unit,
    onNavigateToStories: () -> Unit,
    onNavigateToMap: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(24.dp))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Chat
        Row(
            modifier = Modifier
                .clickable { onNavigateToChat() }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "💬 المحادثات", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
        }

        // Bottom: Map
        Row(
            modifier = Modifier
                .clickable { onNavigateToMap() }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🗺️ خريطة hollam", color = HollamCyberCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }

        // Right: Stories
        Row(
            modifier = Modifier
                .clickable { onNavigateToStories() }
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🔥 القصص", color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
        }
    }
}

/**
 * Fallback stylized camera viewfinder simulation
 */
@Composable
private fun SimulatedViewfinder(
    filter: CameraFilter,
    lensFacing: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E1B4B),
                        Color(0xFF090D16)
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Ambient live camera scanline effect
            val step = 30.dp.toPx()
            var y = 0f
            while (y < size.height) {
                drawLine(
                    color = Color.White.copy(alpha = 0.02f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += step
            }

            // Crosshair center mark
            val cx = size.width / 2f
            val cy = size.height / 2f
            val crossSize = 18.dp.toPx()
            drawLine(
                color = HollamNeonLime.copy(alpha = 0.4f),
                start = Offset(cx - crossSize, cy),
                end = Offset(cx + crossSize, cy),
                strokeWidth = 2f
            )
            drawLine(
                color = HollamNeonLime.copy(alpha = 0.4f),
                start = Offset(cx, cy - crossSize),
                end = Offset(cx, cy + crossSize),
                strokeWidth = 2f
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HollamSpectralCameraIcon(size = 56.dp)
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "كاميرا hollam اللحظية",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (lensFacing == CameraSelector.LENS_FACING_FRONT) "الكاميرا الأمامية (سيلفي)" else "الكاميرا الخلفية (عدسة واسعة)",
                color = HollamNeonLime,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun CameraGridOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val widthThird = size.width / 3f
        val heightThird = size.height / 3f

        // Vertical lines
        drawLine(
            color = Color.White.copy(alpha = 0.15f),
            start = Offset(widthThird, 0f),
            end = Offset(widthThird, size.height),
            strokeWidth = 1f
        )
        drawLine(
            color = Color.White.copy(alpha = 0.15f),
            start = Offset(widthThird * 2f, 0f),
            end = Offset(widthThird * 2f, size.height),
            strokeWidth = 1f
        )

        // Horizontal lines
        drawLine(
            color = Color.White.copy(alpha = 0.15f),
            start = Offset(0f, heightThird),
            end = Offset(size.width, heightThird),
            strokeWidth = 1f
        )
        drawLine(
            color = Color.White.copy(alpha = 0.15f),
            start = Offset(0f, heightThird * 2f),
            end = Offset(size.width, heightThird * 2f),
            strokeWidth = 1f
        )
    }
}

@Composable
private fun FocusIndicator(
    pos: Offset,
    onDismiss: () -> Unit
) {
    LaunchedEffect(pos) {
        delay(1200)
        onDismiss()
    }

    val animatedScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(200),
        label = "focus_scale"
    )

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val boxSize = 64.dp.toPx()
        drawCircle(
            color = HollamNeonLime,
            radius = (boxSize / 2f) * animatedScale,
            center = pos,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

/**
 * Review Captured Snap Dialog
 */
@Composable
private fun CapturedSnapDialog(
    photoUri: String?,
    filter: CameraFilter,
    onDismiss: () -> Unit,
    onSend: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HollamBlack)
            .statusBarsPadding()
            .padding(16.dp)
            .testTag("captured_snap_dialog")
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 72.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = HollamDarkElevated)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val hasValidFile = photoUri != null && File(photoUri).exists()

                if (hasValidFile) {
                    AsyncImage(
                        model = File(photoUri!!),
                        contentDescription = "الصورة الملتقطة",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Crop
                    )

                    // Subtle Bottom Gradient for text readability
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "لحظة hollam ملتقطة!",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "فلتر: ${filter.name} • ${File(photoUri).name}",
                            color = HollamNeonLime,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(filter.tintColor).copy(alpha = 0.4f),
                                        HollamDarkSurface
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(24.dp)
                        ) {
                            HollamSpectralCameraIcon(size = 80.dp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "لحظة hollam ملتقطة!",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "فلتر: ${filter.name} • تم الحفظ بجودة فائقة",
                                color = HollamNeonLime,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Close / Discard button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "إلغاء", tint = Color.White)
                }
            }
        }

        // Action Buttons Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onDismiss,
                modifier = Modifier.weight(1f).height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HollamDarkElevated),
                shape = RoundedCornerShape(26.dp)
            ) {
                Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("حفظ", color = Color.White)
            }

            Button(
                onClick = onSend,
                modifier = Modifier.weight(1.8f).height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HollamNeonLime),
                shape = RoundedCornerShape(26.dp)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = HollamBlack)
                Spacer(modifier = Modifier.width(6.dp))
                Text("إرسال اللحظة 🚀", color = HollamBlack, fontWeight = FontWeight.Bold)
            }
        }
    }
}
