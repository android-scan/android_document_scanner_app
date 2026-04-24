package com.example.android_document_scanner_app.data.repository

import androidx.room.withTransaction
import com.example.android_document_scanner_app.data.db.AppDatabase
import com.example.android_document_scanner_app.data.db.DocumentDao
import com.example.android_document_scanner_app.data.db.entities.toDomain
import com.example.android_document_scanner_app.domain.model.Document
import com.example.android_document_scanner_app.domain.model.Page
import com.example.android_document_scanner_app.domain.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocumentRepository @Inject constructor(
    private val dao: DocumentDao,
    private val db: AppDatabase,
) {

    fun getAllDocuments(): Flow<List<Document>> =
        dao.getAllDocuments().map { list -> list.map { it.toDomain() } }

    fun getDocumentById(id: Long): Flow<Document?> =
        dao.getDocumentById(id).map { it?.toDomain() }

    fun getPagesForDocument(documentId: Long): Flow<List<Page>> =
        dao.getPagesForDocument(documentId).map { list -> list.map { it.toDomain() } }

    // Транзакция: сначала документ, потом страницы с его ID
    suspend fun saveDocument(doc: Document, pages: List<Page>): Result<Long> =
        runCatching {
            db.withTransaction {
                val docId = dao.insertDocument(doc.toEntity())
                pages.forEach { page ->
                    dao.insertPage(page.copy(documentId = docId).toEntity())
                }
                docId
            }
        }

    // Возвращает пути к файлам страниц — вызывающий слой должен удалить их с диска
    suspend fun deleteDocument(id: Long): Result<List<String>> =
        runCatching {
            val filePaths = dao.getPagesForDocument(id)
                .firstOrNull()
                ?.map { it.filePath }
                ?: emptyList()
            dao.deleteDocument(id) // CASCADE удалит страницы из БД
            filePaths
        }
}
