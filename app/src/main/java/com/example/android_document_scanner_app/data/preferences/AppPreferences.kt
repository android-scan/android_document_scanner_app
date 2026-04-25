package com.example.android_document_scanner_app.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Без @Inject — AppModule явно создаёт этот класс через @Provides
class AppPreferences(
    private val dataStore: DataStore<Preferences>,
) {
    private object Keys {
        val IS_ONBOARDING_DONE    = booleanPreferencesKey("is_onboarding_done")
        val IS_PREMIUM            = booleanPreferencesKey("is_premium")
        val SCAN_COUNT_TODAY      = intPreferencesKey("scan_count_today")
        val LAST_SCAN_DATE        = stringPreferencesKey("last_scan_date")
        val DEFAULT_EXPORT_FORMAT = stringPreferencesKey("default_export_format")
    }

    // ── Геттеры (Flow) ────────────────────────────────────────────────────────

    val isOnboardingDone: Flow<Boolean>    = dataStore.data.map { it[Keys.IS_ONBOARDING_DONE] ?: false }
    val isPremium: Flow<Boolean>           = dataStore.data.map { it[Keys.IS_PREMIUM] ?: false }
    val scanCountToday: Flow<Int>          = dataStore.data.map { it[Keys.SCAN_COUNT_TODAY] ?: 0 }
    val lastScanDate: Flow<String>         = dataStore.data.map { it[Keys.LAST_SCAN_DATE] ?: "" }
    val defaultExportFormat: Flow<String>  = dataStore.data.map { it[Keys.DEFAULT_EXPORT_FORMAT] ?: "PDF" }

    // ── Сеттеры (suspend) ─────────────────────────────────────────────────────

    suspend fun setOnboardingDone(value: Boolean)     { dataStore.edit { it[Keys.IS_ONBOARDING_DONE] = value } }
    suspend fun setPremium(value: Boolean)            { dataStore.edit { it[Keys.IS_PREMIUM] = value } }
    suspend fun setScanCountToday(value: Int)         { dataStore.edit { it[Keys.SCAN_COUNT_TODAY] = value } }
    suspend fun setLastScanDate(value: String)        { dataStore.edit { it[Keys.LAST_SCAN_DATE] = value } }
    suspend fun setDefaultExportFormat(value: String) { dataStore.edit { it[Keys.DEFAULT_EXPORT_FORMAT] = value } }
}
