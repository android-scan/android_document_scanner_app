package com.example.android_document_scanner_app.presentation.splash

import androidx.lifecycle.ViewModel
import com.example.android_document_scanner_app.data.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

// ── ОТЛАДКА: убрать перед релизом! ──────────────────────────────────────────
private const val DEBUG_FORCE_ONBOARDING = true
// ────────────────────────────────────────────────────────────────────────────

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prefs: AppPreferences,
) : ViewModel() {

    val isOnboardingDone: Flow<Boolean> =
        if (DEBUG_FORCE_ONBOARDING) flow { emit(false) }
        else prefs.isOnboardingDone
}
