package com.example.android_document_scanner_app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android_document_scanner_app.data.db.entities.DocumentEntity
import com.example.android_document_scanner_app.data.db.entities.PageEntity

@Database(
    entities = [DocumentEntity::class, PageEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
}
