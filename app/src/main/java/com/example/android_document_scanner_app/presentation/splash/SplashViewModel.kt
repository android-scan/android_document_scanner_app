package com.example.android_document_scanner_app.presentation.splash

import androidx.lifecycle.ViewModel
import com.example.android_document_scanner_app.data.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val prefs: AppPreferences,
) : ViewModel() {
    val isOnboardingDone: Flow<Boolean> = prefs.isOnboardingDone
}
