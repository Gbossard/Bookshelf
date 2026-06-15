package com.example.bookshelf.di

import com.example.bookshelf.data.repository.BookshelfRepository
import com.example.bookshelf.data.repository.DefaultBooksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBookshelfRepository(
        defaultBooksRepository: DefaultBooksRepository
    ): BookshelfRepository
}