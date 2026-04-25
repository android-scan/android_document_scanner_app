package com.example.android_document_scanner_app.data.preferences

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

class SubscriptionManagerTest {

    private val prefs: AppPreferences = mockk()
    private lateinit var manager: SubscriptionManager

    @BeforeEach
    fun setUp() {
        manager = SubscriptionManager(prefs)
        // Дефолтные сеттеры — не бросают исключений
        coJustRun { prefs.setScanCountToday(any()) }
        coJustRun { prefs.setLastScanDate(any()) }
    }

    // ── canScan ───────────────────────────────────────────────────────────────

    @Test
    fun `canScan возвращает true когда isPremium независимо от счётчика`() {
        val today = LocalDate.now().toString()
        every { prefs.isPremium }      returns flowOf(true)
        every { prefs.lastScanDate }   returns flowOf(today)
        every { prefs.scanCountToday } returns flowOf(999) // далеко за лимитом

        assertTrue(manager.canScan())
    }

    @Test
    fun `canScan возвращает false когда scanCountToday=10 и isPremium=false`() {
        val today = LocalDate.now().toString()
        every { prefs.isPremium }      returns flowOf(false)
        every { prefs.lastScanDate }   returns flowOf(today)
        every { prefs.scanCountToday } returns flowOf(10) // ровно лимит

        assertFalse(manager.canScan())
    }

    @Test
    fun `canScan возвращает true когда scanCountToday=9 и isPremium=false`() {
        val today = LocalDate.now().toString()
        every { prefs.isPremium }      returns flowOf(false)
        every { prefs.lastScanDate }   returns flowOf(today)
        every { prefs.scanCountToday } returns flowOf(9) // один до лимита

        assertTrue(manager.canScan())
    }

    // ── incrementScanCount ────────────────────────────────────────────────────

    @Test
    fun `incrementScanCount сбрасывает счётчик если дата изменилась`() = runTest {
        val yesterday = LocalDate.now().minusDays(1).toString()
        every { prefs.lastScanDate }   returns flowOf(yesterday)
        every { prefs.scanCountToday } returns flowOf(5)

        manager.incrementScanCount()

        // Сначала сброс до 0 (новый день), затем инкремент до 1
        coVerify { prefs.setScanCountToday(0) }
        coVerify { prefs.setLastScanDate(LocalDate.now().toString()) }
        coVerify { prefs.setScanCountToday(1) }
    }

    @Test
    fun `incrementScanCount не сбрасывает счётчик если дата не изменилась`() = runTest {
        val today = LocalDate.now().toString()
        every { prefs.lastScanDate }   returns flowOf(today)
        every { prefs.scanCountToday } returns flowOf(3)

        manager.incrementScanCount()

        // Сброса быть не должно, только инкремент
        coVerify(exactly = 0) { prefs.setScanCountToday(0) }
        coVerify(exactly = 1) { prefs.setScanCountToday(4) }
    }

    // ── activatePremium / resetPremium ────────────────────────────────────────

    @Test
    fun `activatePremium пишет true в prefs`() = runTest {
        coJustRun { prefs.setPremium(any()) }

        manager.activatePremium()

        coVerify(exactly = 1) { prefs.setPremium(true) }
    }

    @Test
    fun `resetPremium пишет false в prefs`() = runTest {
        coJustRun { prefs.setPremium(any()) }

        manager.resetPremium()

        coVerify(exactly = 1) { prefs.setPremium(false) }
    }
}
