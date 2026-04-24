package com.example.android_document_scanner_app.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android_document_scanner_app.domain.model.Document

@Entity(tableName = "documents")
data class DocumentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val createdAt: Long,       // System.currentTimeMillis()
    val pageCount: Int,
    val format: String,        // "PDF" | "JPG" | "PNG"
    val folderName: String? = null,    // бэкдор: папки (сейчас не используется)
    val documentType: String? = null,  // бэкдор: "RECEIPT" | "CARD" | "GENERIC"
)

fun DocumentEntity.toDomain(): Document = Document(
    id           = id,
    name         = name,
    createdAt    = createdAt,
    pageCount    = pageCount,
    format       = format,
    folderName   = folderName,
    documentType = documentType,
)
