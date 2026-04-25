package com.example.android_document_scanner_app.presentation.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android_document_scanner_app.presentation.components.PrimaryButton
import com.example.android_document_scanner_app.presentation.theme.Accent
import com.example.android_document_scanner_app.presentation.theme.BgPrimary
import com.example.android_document_scanner_app.presentation.theme.BgTertiary
import com.example.android_document_scanner_app.presentation.theme.FontBody
import com.example.android_document_scanner_app.presentation.theme.FontDisplay
import com.example.android_document_scanner_app.presentation.theme.TextPrimary
import com.example.android_document_scanner_app.presentation.theme.TextSecondary
import com.example.android_document_scanner_app.presentation.theme.TextTertiary

private data class OnboardingSlide(
    val icon:     ImageVector,
    val title:    String,
    val subtitle: String,
)

private val SLIDES = listOf(
    OnboardingSlide(
        icon     = Icons.Outlined.DocumentScanner,
        title    = "Сканируйте\nлюбые документы",
        subtitle = "Автоматическое определение границ и коррекция перспективы — в реальном времени.",
    ),
    OnboardingSlide(
        icon     = Icons.Outlined.TextFields,
        title    = "Автоматическое\nраспознавание текста",
        subtitle = "Печатный текст, латиница и кириллица — полностью офлайн на устройстве.",
    ),
    OnboardingSlide(
        icon     = Icons.Outlined.FolderOpen,
        title    = "Храните\nи делитесь удобно",
        subtitle = "PDF, JPG или PNG — одним касанием. Всё локально, без облака.",
    ),
)

@Composable
fun OnboardingScreen(
    onNavigateToPermissions: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val pagerState = rememberPagerState(pageCount = { SLIDES.size })
    val isLastPage = pagerState.currentPage == SLIDES.lastIndex

    val onDone: () -> Unit = {
        viewModel.completeOnboarding()
        onNavigateToPermissions()
    }

    // Alpha для кнопки "Начать" — плавно появляется на последнем слайде
    val buttonAlpha by animateFloatAsState(
        targetValue = if (isLastPage) 1f else 0f,
        label       = "button_alpha",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary),
    ) {
        // Кнопка "Пропустить" — правый верхний угол
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
                    Icon(
                        imageVector        = slide.icon,
                        contentDescription = null,
                        modifier           = Modifier.size(96.dp),
                        tint               = Accent,
                    )
                    Spacer(Modifier.height(48.dp))
                    Text(
                        text      = slide.title,
                        style     = TextStyle(
                            fontFamily    = FontDisplay,
                            fontWeight    = FontWeight.Bold,
                            fontSize      = 30.sp,
                            lineHeight    = 36.sp,
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
                            lineHeight = 24.sp,
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

            // ── Кнопка "Начать" — место всегда зарезервировано (48dp) ─────────
            // alpha → плавное появление; enabled=false → нет случайного клика
            Box(
                modifier         = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp),
                contentAlignment = Alignment.Center,
            ) {
                PrimaryButton(
                    text     = "Начать",
                    onClick  = onDone,
                    enabled  = isLastPage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(buttonAlpha),
                )
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}
