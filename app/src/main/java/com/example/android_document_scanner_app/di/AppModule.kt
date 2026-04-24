package com.example.android_document_scanner_app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.room.Room
import com.example.android_document_scanner_app.data.db.AppDatabase
import com.example.android_document_scanner_app.data.db.DocumentDao
import com.example.android_document_scanner_app.data.preferences.AppPreferences
import com.example.android_document_scanner_app.data.preferences.SubscriptionManager
import com.example.android_document_scanner_app.data.repository.DocumentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "doc_scanner.db",
        ).build()

    @Provides
    @Singleton
    fun provideDocumentDao(db: AppDatabase): DocumentDao = db.documentDao()

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("doc_scanner_prefs") }
        )

    @Provides
    @Singleton
    fun provideAppPreferences(dataStore: DataStore<Preferences>): AppPreferences =
        AppPreferences(dataStore)

    @Provides
    @Singleton
    fun provideSubscriptionManager(prefs: AppPreferences): SubscriptionManager =
        SubscriptionManager(prefs)

    @Provides
    @Singleton
    fun provideDocumentRepository(dao: DocumentDao, db: AppDatabase): DocumentRepository =
        DocumentRepository(dao, db)
}
