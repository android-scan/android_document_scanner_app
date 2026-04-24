package com.example.android_document_scanner_app.domain.model

import com.example.android_document_scanner_app.data.db.entities.DocumentEntity

data class Document(
    val id: Long = 0,
    val name: String,
    val createdAt: Long,
    val pageCount: Int,
    val format: String,
    val folderName: String? = null,
    val documentType: String? = null,
)

fun Document.toEntity(): DocumentEntity = DocumentEntity(
    id           = id,
    name         = name,
    createdAt    = createdAt,
    pageCount    = pageCount,
    format       = format,
    folderName   = folderName,
    documentType = documentType,
)
