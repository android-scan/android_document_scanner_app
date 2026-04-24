package com.example.android_document_scanner_app.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.example.android_document_scanner_app.R

private val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage   = "com.google.android.gms",
    certificates      = R.array.com_google_android_gms_fonts_certs,
)

// Cormorant Garamond — заголовки, названия документов
val FontDisplay = FontFamily(
    Font(GoogleFont("Cormorant Garamond"), googleFontProvider, FontWeight.Normal),
    Font(GoogleFont("Cormorant Garamond"), googleFontProvider, FontWeight.Medium),
    Font(GoogleFont("Cormorant Garamond"), googleFontProvider, FontWeight.SemiBold),
    Font(GoogleFont("Cormorant Garamond"), googleFontProvider, FontWeight.Bold),
)

// DM Sans — основной текст UI
val FontBody = FontFamily(
    Font(GoogleFont("DM Sans"), googleFontProvider, FontWeight.Normal),
    Font(GoogleFont("DM Sans"), googleFontProvider, FontWeight.Medium),
    Font(GoogleFont("DM Sans"), googleFontProvider, FontWeight.SemiBold),
)

// DM Mono — даты, имена файлов, метаданные
val FontMono = FontFamily(
    Font(GoogleFont("DM Mono"), googleFontProvider, FontWeight.Normal),
    Font(GoogleFont("DM Mono"), googleFontProvider, FontWeight.Medium),
)

// Готовые стили для мета-информации (не входят в стандартный Typography)
val MonoMedium = TextStyle(fontFamily = FontMono, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp)
val MonoSmall  = TextStyle(fontFamily = FontMono, fontWeight = FontWeight.Normal,  fontSize = 11.sp, lineHeight = 14.sp)

val DocScannerTypography = Typography(
    // Display и Headline — Cormorant Garamond
    displayLarge   = TextStyle(fontFamily = FontDisplay, fontWeight = FontWeight.Bold,     fontSize = 57.sp, lineHeight = 64.sp),
    displayMedium  = TextStyle(fontFamily = FontDisplay, fontWeight = FontWeight.SemiBold, fontSize = 45.sp, lineHeight = 52.sp),
    displaySmall   = TextStyle(fontFamily = FontDisplay, fontWeight = FontWeight.SemiBold, fontSize = 36.sp, lineHeight = 44.sp),
    headlineLarge  = TextStyle(fontFamily = FontDisplay, fontWeight = FontWeight.SemiBold, fontSize = 32.sp, lineHeight = 40.sp),
    headlineMedium = TextStyle(fontFamily = FontDisplay, fontWeight = FontWeight.Medium,   fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall  = TextStyle(fontFamily = FontDisplay, fontWeight = FontWeight.Medium,   fontSize = 24.sp, lineHeight = 32.sp),

    // Title, Body, Label — DM Sans
    titleLarge   = TextStyle(fontFamily = FontBody, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium  = TextStyle(fontFamily = FontBody, fontWeight = FontWeight.Medium,   fontSize = 16.sp, lineHeight = 24.sp),
    titleSmall   = TextStyle(fontFamily = FontBody, fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 20.sp),
    bodyLarge    = TextStyle(fontFamily = FontBody, fontWeight = FontWeight.Normal,   fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium   = TextStyle(fontFamily = FontBody, fontWeight = FontWeight.Normal,   fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall    = TextStyle(fontFamily = FontBody, fontWeight = FontWeight.Normal,   fontSize = 12.sp, lineHeight = 16.sp),
    labelLarge   = TextStyle(fontFamily = FontBody, fontWeight = FontWeight.Medium,   fontSize = 14.sp, lineHeight = 20.sp),
    labelMedium  = TextStyle(fontFamily = FontBody, fontWeight = FontWeight.Medium,   fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall   = TextStyle(fontFamily = FontBody, fontWeight = FontWeight.Medium,   fontSize = 11.sp, lineHeight = 16.sp),
)
