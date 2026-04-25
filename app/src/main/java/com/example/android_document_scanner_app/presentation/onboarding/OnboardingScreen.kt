package com.example.android_document_scanner_app.presentation.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android_document_scanner_app.presentation.components.PaperGrain
import com.example.android_document_scanner_app.presentation.components.PrimaryButton
import com.example.android_document_scanner_app.presentation.theme.Accent
import com.example.android_document_scanner_app.presentation.theme.AccentLight
import com.example.android_document_scanner_app.presentation.theme.AccentSubtle
import com.example.android_document_scanner_app.presentation.theme.BgPrimary
import com.example.android_document_scanner_app.presentation.theme.BgTertiary
import com.example.android_document_scanner_app.presentation.theme.FontBody
import com.example.android_document_scanner_app.presentation.theme.FontDisplay
import com.example.android_document_scanner_app.presentation.theme.SurfaceRaised
import com.example.android_document_scanner_app.presentation.theme.TextPrimary
import com.example.android_document_scanner_app.presentation.theme.TextSecondary
import com.example.android_document_scanner_app.presentation.theme.TextTertiary
import kotlinx.coroutines.launch

private data class OnboardingSlide(
    val title:    String,
    val subtitle: String,
)

private val SLIDES = listOf(
    OnboardingSlide(
        title    = "Сканируйте\nлюбые документы",
        subtitle = "Автоматическое определение границ и коррекция перспективы — в реальном времени.",
    ),
    OnboardingSlide(
        title    = "Распознавание\nтекста",
        subtitle = "Печатный текст, латиница и кириллица — полностью офлайн на устройстве.",
    ),
    OnboardingSlide(
        title    = "Храните\nи делитесь",
        subtitle = "PDF, JPG или PNG — одним касанием. Всё локально, без облака.",
    ),
)

// ── Иллюстрации (Canvas, viewBox 0 0 200 180) ────────────────────────────────

@Composable
private fun IlloScanner() {
    Canvas(modifier = Modifier.size(width = 200.dp, height = 180.dp)) {
        val vw = 200f; val vh = 180f
        fun sx(x: Float) = x / vw * size.width
        fun sy(y: Float) = y / vh * size.height

        // Документ позади — повёрнут -4°
        rotate(degrees = -4f, pivot = Offset(sx(100f), sy(90f))) {
            val docPath = Path().apply {
                moveTo(sx(22f), sy(52f))
                lineTo(sx(170f), sy(26f))
                lineTo(sx(176f), sy(140f))
                lineTo(sx(28f), sy(166f))
                close()
            }
            drawPath(docPath, color = BgPrimary.copy(alpha = 0.7f))
            drawPath(docPath, color = Accent, style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round))

            // Строки текста на документе (4 отрезка)
            listOf(
                Offset(sx(42f), sy(68f)) to Offset(sx(152f), sy(50f)),
                Offset(sx(44f), sy(80f)) to Offset(sx(140f), sy(64f)),
                Offset(sx(46f), sy(92f)) to Offset(sx(148f), sy(76f)),
                Offset(sx(46f), sy(104f)) to Offset(sx(130f), sy(90f)),
            ).forEach { (s, e) ->
                drawLine(color = AccentLight.copy(alpha = 0.8f), start = s, end = e,
                    strokeWidth = 1.dp.toPx(), cap = StrokeCap.Round)
            }
        }

        // Телефон — внешний контур
        val phonePath = Path().apply {
            addRoundRect(RoundRect(
                left   = sx(58f), top    = sy(20f),
                right  = sx(138f), bottom = sy(160f),
                cornerRadius = CornerRadius(sx(10f)),
            ))
        }
        drawPath(phonePath, color = BgPrimary)
        drawPath(phonePath, color = Accent, style = Stroke(width = 1.5.dp.toPx()))

        // Экран телефона
        val screenPath = Path().apply {
            addRoundRect(RoundRect(
                left   = sx(68f), top    = sy(34f),
                right  = sx(128f), bottom = sy(144f),
                cornerRadius = CornerRadius(sx(2f)),
            ))
        }
        drawPath(screenPath, color = AccentLight.copy(alpha = 0.5f),
            style = Stroke(width = 1.dp.toPx()))

        // Линза — внешняя и внутренняя
        drawCircle(color = BgPrimary,
            radius = sx(14f), center = Offset(sx(98f), sy(80f)))
        drawCircle(color = Accent,
            radius = sx(14f), center = Offset(sx(98f), sy(80f)),
            style = Stroke(width = 1.5.dp.toPx()))
        drawCircle(color = Accent,
            radius = sx(7f), center = Offset(sx(98f), sy(80f)),
            style = Stroke(width = 1.dp.toPx()))

        // 4 L-образных угловых маркера
        val sw = 2.dp.toPx()
        val arm = sx(6f)
        val corners = listOf(
            Offset(sx(78f), sy(58f)) to Pair(Offset(sx(78f), sy(64f)), Offset(sx(84f), sy(58f))),
            Offset(sx(118f), sy(58f)) to Pair(Offset(sx(118f), sy(64f)), Offset(sx(112f), sy(58f))),
            Offset(sx(78f), sy(102f)) to Pair(Offset(sx(78f), sy(96f)), Offset(sx(84f), sy(102f))),
            Offset(sx(118f), sy(102f)) to Pair(Offset(sx(118f), sy(96f)), Offset(sx(112f), sy(102f))),
        )
        corners.forEach { (pivot, arms) ->
            drawLine(color = Accent, start = pivot, end = arms.first,
                strokeWidth = sw, cap = StrokeCap.Round)
            drawLine(color = Accent, start = pivot, end = arms.second,
                strokeWidth = sw, cap = StrokeCap.Round)
        }
    }
}

@Composable
private fun IlloOCR() {
    val lineWidths = listOf(70f, 90f, 58f, 82f, 64f, 76f, 50f)
    Canvas(modifier = Modifier.size(width = 200.dp, height = 180.dp)) {
        val vw = 200f; val vh = 180f
        fun sx(x: Float) = x / vw * size.width
        fun sy(y: Float) = y / vh * size.height

        // Лист бумаги
        val sheetPath = Path().apply {
            addRoundRect(RoundRect(
                left   = sx(36f), top    = sy(20f),
                right  = sx(164f), bottom = sy(160f),
                cornerRadius = CornerRadius(sx(4f)),
            ))
        }
        drawPath(sheetPath, color = BgPrimary)
        drawPath(sheetPath, color = Accent, style = Stroke(width = 1.5.dp.toPx()))

        // 2 выделения OCR (аналог highlight)
        listOf(sy(68f) to sy(76f), sy(100f) to sy(108f)).forEach { (top, bottom) ->
            drawRect(
                color = AccentSubtle.copy(alpha = 0.9f),
                topLeft = Offset(sx(48f), top),
                size = Size(sx(62f), bottom - top),
            )
        }

        // 7 строк текста
        lineWidths.forEachIndexed { i, w ->
            val isAccent = i == 2 || i == 4
            drawRoundRect(
                color = if (isAccent) Accent.copy(alpha = 0.9f) else BgTertiary.copy(alpha = 0.6f),
                topLeft = Offset(sx(50f), sy(38f + i * 16f)),
                size = Size(sx(w), sy(4f)),
                cornerRadius = CornerRadius(sx(1f)),
            )
        }
    }
}

@Composable
private fun IlloFolder() {
    Canvas(modifier = Modifier.size(width = 200.dp, height = 180.dp)) {
        val vw = 200f; val vh = 180f
        fun sx(x: Float) = x / vw * size.width
        fun sy(y: Float) = y / vh * size.height

        // Папка — путь из JSX: M16 48 a4 4 0 0 1 4-4 h38 l10 10 h112 a4 4 0 0 1 4 4 v90 a4 4 0 0 1-4 4 H20 a4 4 0 0 1-4-4Z
        val folderPath = Path().apply {
            moveTo(sx(16f), sy(48f))
            // верхний левый скруглённый вход
            arcTo(Rect(sx(16f), sy(40f), sx(24f), sy(48f)), 180f, 90f, false)
            lineTo(sx(58f), sy(44f))     // вкладка начало
            lineTo(sx(68f), sy(54f))     // вкладка конец (диагональ)
            lineTo(sx(180f), sy(54f))    // вправо до конца
            arcTo(Rect(sx(180f), sy(54f), sx(188f), sy(62f)), 270f, 90f, false)
            lineTo(sx(188f), sy(148f))   // правый край вниз
            arcTo(Rect(sx(180f), sy(148f), sx(188f), sy(156f)), 0f, 90f, false)
            lineTo(sx(24f), sy(156f))    // нижний край влево
            arcTo(Rect(sx(16f), sy(148f), sx(24f), sy(156f)), 90f, 90f, false)
            close()
        }
        drawPath(folderPath, color = BgPrimary)
        drawPath(folderPath, color = Accent, style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round))

        // 3 карточки документа с поворотами
        data class Card(val x: Float, val y: Float, val w: Float, val h: Float, val rot: Float)
        val cards = listOf(
            Card(36f, 64f, 48f, 62f, 0f),
            Card(92f, 64f, 48f, 62f, 3f),
            Card(148f, 64f, 36f, 62f, -2f),
        )
        cards.forEach { c ->
            val cx = sx(c.x + c.w / 2)
            val cy = sy(c.y + c.h / 2)
            rotate(degrees = c.rot, pivot = Offset(cx, cy)) {
                val cardPath = Path().apply {
                    addRoundRect(RoundRect(
                        left   = sx(c.x), top    = sy(c.y),
                        right  = sx(c.x + c.w), bottom = sy(c.y + c.h),
                        cornerRadius = CornerRadius(sx(2f)),
                    ))
                }
                drawPath(cardPath, color = SurfaceRaised)
                drawPath(cardPath, color = BgTertiary, style = Stroke(width = 1.dp.toPx()))

                // 3 строки текста в карточке
                listOf(0f to 0.6f, 8f to 0.45f, 16f to 0.45f).forEach { (dy, alpha) ->
                    val lineW = if (dy == 0f) sx(28f) else if (dy == 8f) sx(22f) else sx(26f)
                    drawRoundRect(
                        color = AccentLight.copy(alpha = alpha.toFloat()),
                        topLeft = Offset(sx(c.x + (if (c.x >= 148f) 2f else 4f)), sy(c.y + 12f + dy)),
                        size = Size(lineW, sy(2f)),
                        cornerRadius = CornerRadius(sx(1f)),
                    )
                }
            }
        }
    }
}

// ── Главный экран ─────────────────────────────────────────────────────────────

@Composable
fun OnboardingScreen(
    onNavigateToPermissions: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState(pageCount = { SLIDES.size })
    val scope      = rememberCoroutineScope()
    val isLastPage = pagerState.currentPage == SLIDES.lastIndex

    val onDone: () -> Unit = {
        viewModel.completeOnboarding()
        onNavigateToPermissions()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .background(BgPrimary),
    ) {
        Column(
            modifier            = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // ── Слайды ────────────────────────────────────────────────────────
            HorizontalPager(
                state    = pagerState,
                modifier = Modifier.weight(1f),
            ) { page ->
                val slide = SLIDES[page]
                Column(
                    modifier            = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    // Иллюстрация — кастомный SVG Canvas
                    when (page) {
                        0 -> IlloScanner()
                        1 -> IlloOCR()
                        else -> IlloFolder()
                    }
                    Spacer(Modifier.height(48.dp))
                    Text(
                        text      = slide.title,
                        style     = TextStyle(
                            fontFamily    = FontDisplay,
                            fontWeight    = FontWeight.SemiBold,
                            fontSize      = 36.sp,
                            lineHeight    = (36 * 1.1).sp,
                            letterSpacing = 0.01.sp,
                        ),
                        color     = TextPrimary,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        text      = slide.subtitle,
                        style     = TextStyle(
                            fontFamily = FontBody,
                            fontWeight = FontWeight.Light,
                            fontSize   = 16.sp,
                            lineHeight = (16 * 1.5).sp,
                        ),
                        color     = TextSecondary,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            // ── Точки-индикаторы ──────────────────────────────────────────────
            Row(
                modifier              = Modifier.padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment     = Alignment.CenterVertically,
            ) {
                repeat(SLIDES.size) { index ->
                    val isActive = pagerState.currentPage == index
                    val dotWidth by animateDpAsState(
                        targetValue = if (isActive) 24.dp else 6.dp,
                        label       = "dot_width_$index",
                    )
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(dotWidth)
                            .background(
                                color = if (isActive) Accent else BgTertiary,
                                shape = RoundedCornerShape(3.dp),
                            ),
                    )
                }
            }

            // ── Кнопка "Далее" / "Начать" — всегда видима ────────────────────
            PrimaryButton(
                text    = if (isLastPage) "Начать" else "Далее",
                onClick = {
                    if (isLastPage) onDone()
                    else scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
            )

            Spacer(Modifier.height(40.dp))
        }

        // ── Кнопка "Пропустить" ───────────────────────────────────────────────
        TextButton(
            onClick  = onDone,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 8.dp),
            colors   = ButtonDefaults.textButtonColors(contentColor = TextTertiary),
        ) {
            Text(
                text  = "Пропустить",
                style = TextStyle(
                    fontFamily = FontBody,
                    fontWeight = FontWeight.Normal,
                    fontSize   = 14.sp,
                ),
            )
        }

        // ── Бумажная текстура ─────────────────────────────────────────────────
        PaperGrain(modifier = Modifier.fillMaxSize())
    }
}
