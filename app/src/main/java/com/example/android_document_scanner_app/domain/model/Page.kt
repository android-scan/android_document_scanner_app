package com.example.android_document_scanner_app.domain.model

import com.example.android_document_scanner_app.data.db.entities.PageEntity

data class Page(
    val id: Long = 0,
    val documentId: Long,
    val pageNumber: Int,
    val filePath: String,
    val ocrText: String? = null,
)

fun Page.toEntity(): PageEntity = PageEntity(
    id         = id,
    documentId = documentId,
    pageNumber = pageNumber,
    filePath   = filePath,
    ocrText    = ocrText,
)
