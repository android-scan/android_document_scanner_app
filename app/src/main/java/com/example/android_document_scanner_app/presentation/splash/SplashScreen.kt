package com.example.android_document_scanner_app.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.android_document_scanner_app.presentation.theme.Accent
import com.example.android_document_scanner_app.presentation.theme.BgPrimary
import com.example.android_document_scanner_app.presentation.theme.FontDisplay
import com.example.android_document_scanner_app.presentation.theme.TextPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        delay(1_500)
        val done = viewModel.isOnboardingDone.first()
        if (done) onNavigateToHome() else onNavigateToOnboarding()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgPrimary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector   = Icons.Outlined.Article,
            contentDescription = null,
            modifier      = Modifier.size(80.dp),
            tint          = Accent,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text  = "DocScanner",
            style = TextStyle(
                fontFamily  = FontDisplay,
                fontWeight  = FontWeight.SemiBold,
                fontSize    = 28.sp,
            ),
            color = TextPrimary,
        )
    }
}
