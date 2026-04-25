package com.example.android_document_scanner_app.data.repository

import com.example.android_document_scanner_app.data.db.AppDatabase
import com.example.android_document_scanner_app.data.db.DocumentDao
import com.example.android_document_scanner_app.data.db.entities.DocumentEntity
import com.example.android_document_scanner_app.data.db.entities.PageEntity
import com.example.android_document_scanner_app.domain.model.Document
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.Runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DocumentRepositoryTest {

    private val dao: DocumentDao = mockk()
    private val db: AppDatabase = mockk()

    // Анонимный подкласс: runInTransaction просто выполняет блок без реальной БД
    private lateinit var repository: DocumentRepository

    @BeforeEach
    fun setUp() {
        repository = object : DocumentRepository(dao, db) {
            override suspend fun <T> runInTransaction(block: suspend () -> T): T = block()
        }
    }

    // ── saveDocument ──────────────────────────────────────────────────────────

    @Test
    fun `saveDocument сохраняет документ и возвращает сгенерированный id`() = runTest {
        coEvery { dao.insertDocument(any()) } returns 42L

        val doc = Document(name = "Договор", createdAt = 1_000L, pageCount = 1, format = "PDF")

        val result = repository.saveDocument(doc, emptyList())

        assertTrue(result.isSuccess)
        assertEquals(42L, result.getOrNull())
        coVerify(exactly = 1) { dao.insertDocument(any()) }
    }

    @Test
    fun `saveDocument вставляет страницы с id документа`() = runTest {
        coEvery { dao.insertDocument(any()) } returns 7L
        coEvery { dao.insertPage(any()) } just Runs

        val doc = Document(name = "Паспорт", createdAt = 0L, pageCount = 2, format = "JPG")
        val pages = listOf(
            com.example.android_document_scanner_app.domain.model.Page(
                documentId = 0, pageNumber = 1, filePath = "/p1.jpg"
            ),
            com.example.android_document_scanner_app.domain.model.Page(
                documentId = 0, pageNumber = 2, filePath = "/p2.jpg"
            ),
        )

        repository.saveDocument(doc, pages)

        // Обе страницы должны быть вставлены с documentId = 7
        coVerify(exactly = 2) { dao.insertPage(match { it.documentId == 7L }) }
    }

    // ── deleteDocument ────────────────────────────────────────────────────────

    @Test
    fun `deleteDocument вызывает dao deleteDocument и возвращает пути файлов`() = runTest {
        val pages = listOf(
            PageEntity(id = 1, documentId = 5, pageNumber = 1, filePath = "/files/scans/doc_p1.jpg"),
            PageEntity(id = 2, documentId = 5, pageNumber = 2, filePath = "/files/scans/doc_p2.jpg"),
        )
        every { dao.getPagesForDocument(5L) } returns flowOf(pages)
        coEvery { dao.deleteDocument(5L) } just Runs

        val result = repository.deleteDocument(5L)

        assertTrue(result.isSuccess)
        assertEquals(
            listOf("/files/scans/doc_p1.jpg", "/files/scans/doc_p2.jpg"),
            result.getOrNull(),
        )
        coVerify(exactly = 1) { dao.deleteDocument(5L) }
    }

    // ── getAllDocuments ───────────────────────────────────────────────────────

    @Test
    fun `getAllDocuments возвращает Flow с маппингом Entity в Domain`() = runTest {
        val entities = listOf(
            DocumentEntity(id = 1, name = "Договор аренды", createdAt = 1000L, pageCount = 3, format = "PDF"),
            DocumentEntity(id = 2, name = "Паспорт",        createdAt = 2000L, pageCount = 1, format = "JPG"),
        )
        every { dao.getAllDocuments() } returns flowOf(entities)

        val documents = repository.getAllDocuments().first()

        assertEquals(2, documents.size)
        assertEquals("Договор аренды", documents[0].name)
        assertEquals(1L, documents[0].id)
        assertEquals("PDF", documents[0].format)
        assertEquals("Паспорт", documents[1].name)
    }
}
