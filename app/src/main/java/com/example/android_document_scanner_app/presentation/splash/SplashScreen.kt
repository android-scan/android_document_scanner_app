package com.example.android_document_scanner_app.presentation.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android_document_scanner_app.presentation.components.PaperGrain
import com.example.android_document_scanner_app.presentation.theme.Accent
import com.example.android_document_scanner_app.presentation.theme.BgPrimary
import com.example.android_document_scanner_app.presentation.theme.FontDisplay
import com.example.android_document_scanner_app.presentation.theme.FontMono
import com.example.android_document_scanner_app.presentation.theme.TextPrimary
import com.example.android_document_scanner_app.presentation.theme.TextTertiary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(1_800)
        val done = viewModel.isOnboardingDone.first()
        if (done) onNavigateToHome() else onNavigateToOnboarding()
    }

    val iconAlpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label         = "icon_alpha",
    )
    val titleAlpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 900, delayMillis = 200),
        label         = "title_alpha",
    )
    val subtitleAlpha by animateFloatAsState(
        targetValue   = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 900, delayMillis = 400),
        label         = "subtitle_alpha",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .background(BgPrimary),
    ) {
        Column(
            modifier            = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(28.dp, Alignment.CenterVertically),
        ) {
            // SVG document icon — 72×84dp, точно как в JSX
            Canvas(
                modifier = Modifier
                    .size(width = 72.dp, height = 84.dp)
                    .alpha(iconAlpha),
            ) {
                val w    = size.width
                val h    = size.height
                val r    = (4f / 72f) * w
                val fold = (16f / 72f) * w

                val x0    = (8f  / 72f) * w
                val y0    = (4f  / 84f) * h
                val x1    = (68f / 72f) * w
                val y1    = (80f / 84f) * h
                val foldX = x1 - fold
                val foldY = y0 + fold

                val docPath = Path().apply {
                    moveTo(x0, y0 + r)
                    quadraticTo(x0, y0, x0 + r, y0)
                    lineTo(foldX, y0)
                    lineTo(x1, foldY)
                    lineTo(x1, y1 - r)
                    quadraticTo(x1, y1, x1 - r, y1)
                    lineTo(x0 + r, y1)
                    quadraticTo(x0, y1, x0, y1 - r)
                    close()
                }
                drawPath(docPath, color = Accent,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))

                val foldPath = Path().apply {
                    moveTo(foldX, y0)
                    lineTo(foldX, foldY)
                    lineTo(x1, foldY)
                }
                drawPath(foldPath, color = Accent,
                    style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))

                listOf(
                    Offset((16f/72f)*w, (38f/84f)*h) to Offset((48f/72f)*w, (38f/84f)*h),
                    Offset((16f/72f)*w, (48f/84f)*h) to Offset((48f/72f)*w, (48f/84f)*h),
                    Offset((16f/72f)*w, (58f/84f)*h) to Offset((38f/72f)*w, (58f/84f)*h),
                ).forEach { (start, end) ->
                    drawLine(
                        color       = Accent.copy(alpha = 0.55f),
                        start       = start,
                        end         = end,
                        strokeWidth = 1.5.dp.toPx(),
                        cap         = StrokeCap.Round,
                    )
                }
            }

            Text(
                text     = "Архив",
                modifier = Modifier.alpha(titleAlpha),
                style    = TextStyle(
                    fontFamily    = FontDisplay,
                    fontWeight    = FontWeight.SemiBold,
                    fontSize      = 34.sp,
                    letterSpacing = 0.02.em,
                    color         = TextPrimary,
                ),
            )

            Text(
                text     = "document · scanner",
                modifier = Modifier.alpha(subtitleAlpha),
                style    = TextStyle(
                    fontFamily    = FontMono,
                    fontWeight    = FontWeight.Normal,
                    fontSize      = 11.sp,
                    letterSpacing = 0.15.em,
                    color         = TextTertiary,
                ),
            )
        }

        PaperGrain(modifier = Modifier.fillMaxSize())
    }
}
