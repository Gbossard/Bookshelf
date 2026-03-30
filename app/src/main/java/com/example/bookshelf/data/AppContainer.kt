package com.example.bookshelf.data

import android.content.Context
import com.example.bookshelf.data.local.BookDatabase
import com.example.bookshelf.data.network.BookshelfApiService
import com.example.bookshelf.data.repository.BookshelfRepository
import com.example.bookshelf.data.repository.DefaultBooksRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

interface AppContainer {
    val bookshelfRepository: BookshelfRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val baseUrl = "https://www.googleapis.com/books/v1/"
    private val json = Json {ignoreUnknownKeys = true}

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: BookshelfApiService by lazy {
        retrofit.create(BookshelfApiService::class.java)
    }

    private val database: BookDatabase by lazy {
        BookDatabase.getDatabase(context)
    }

    override val bookshelfRepository: BookshelfRepository by lazy {
        DefaultBooksRepository(
            bookshelfApiService = retrofitService,
            bookDao = database.bookDao()
        )
    }
}