package com.example.bookshelf.di

import android.content.Context
import com.example.bookshelf.data.local.BookDao
import com.example.bookshelf.data.local.BookDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideBookDatabase(@ApplicationContext context: Context) : BookDatabase {
        return BookDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideBookDao(database: BookDatabase): BookDao {
        return database.bookDao()
    }
}