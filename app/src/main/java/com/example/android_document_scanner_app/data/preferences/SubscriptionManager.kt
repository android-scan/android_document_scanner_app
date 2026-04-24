package com.example.android_document_scanner_app.data.preferences

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private const val FREE_SCAN_LIMIT = 10

@Singleton
class SubscriptionManager @Inject constructor(
    private val prefs: AppPreferences,
) {
    // runBlocking здесь намеренно — это mock реализация для демо.
    // В продакшне заменить на Flow/StateFlow и collect в ViewModel.

    fun isPremium(): Boolean =
        runBlocking { prefs.isPremium.first() }

    // true если пользователь премиум ИЛИ ещё не исчерпал дневной лимит
    fun canScan(): Boolean = runBlocking {
        resetCountIfDayChanged()
        if (prefs.isPremium.first()) return@runBlocking true
        prefs.scanCountToday.first() < FREE_SCAN_LIMIT
    }

    suspend fun activatePremium() {
        prefs.setPremium(true)
    }

    suspend fun resetPremium() {
        prefs.setPremium(false)
    }

    // Увеличивает счётчик. Сбрасывает, если наступил новый день.
    suspend fun incrementScanCount() {
        resetCountIfDayChanged()
        val current = prefs.scanCountToday.first()
        prefs.setScanCountToday(current + 1)
    }

    // ── Внутренние ────────────────────────────────────────────────────────────

    private suspend fun resetCountIfDayChanged() {
        val today = LocalDate.now().toString() // "yyyy-MM-dd"
        val saved = prefs.lastScanDate.firstOrNull() ?: ""
        if (saved != today) {
            prefs.setScanCountToday(0)
            prefs.setLastScanDate(today)
        }
    }
}
