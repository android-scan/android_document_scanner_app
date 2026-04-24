package com.example.android_document_scanner_app.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.android_document_scanner_app.domain.model.Page

@Entity(
    tableName = "pages",
    foreignKeys = [
        ForeignKey(
            entity        = DocumentEntity::class,
            parentColumns = ["id"],
            childColumns  = ["documentId"],
            // при удалении документа — все его страницы тоже удаляются
            onDelete      = ForeignKey.CASCADE,
        )
    ],
    indices = [Index("documentId")], // индекс для быстрого JOIN по documentId
)
data class PageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val documentId: Long,   // FK → DocumentEntity.id
    val pageNumber: Int,    // 1-based
    val filePath: String,   // абсолютный путь в getFilesDir()
    val ocrText: String? = null, // null если OCR не запускался
)

fun PageEntity.toDomain(): Page = Page(
    id         = id,
    documentId = documentId,
    pageNumber = pageNumber,
    filePath   = filePath,
    ocrText    = ocrText,
)
