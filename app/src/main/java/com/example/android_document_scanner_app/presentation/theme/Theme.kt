package com.example.android_document_scanner_app.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DocScannerColorScheme = lightColorScheme(
    primary              = Accent,
    onPrimary            = Color.White,
    primaryContainer     = AccentSubtle,
    onPrimaryContainer   = TextPrimary,

    secondary            = AccentLight,
    onSecondary          = Color.White,
    secondaryContainer   = BgSecondary,
    onSecondaryContainer = TextPrimary,

    tertiary             = Success,
    onTertiary           = Color.White,

    background           = BgPrimary,
    onBackground         = TextPrimary,

    surface              = Surface,
    onSurface            = TextPrimary,
    surfaceVariant       = BgTertiary,
    onSurfaceVariant     = TextSecondary,

    outline              = BgTertiary,

    error                = Danger,
    onError              = Color.White,
)

@Composable
fun DocScannerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DocScannerColorScheme,
        typography  = DocScannerTypography,
        content     = content,
    )
}
