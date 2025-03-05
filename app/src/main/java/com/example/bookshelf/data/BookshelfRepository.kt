package com.example.bookshelf.data

import android.util.Log
import com.example.bookshelf.model.Book
import com.example.bookshelf.network.BookshelfApiService

interface BookshelfRepository {
    suspend fun getBooks(query: String): List<Book>?
}

class NetworkBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
) : BookshelfRepository {
    override suspend fun getBooks(query: String): List<Book>? {
        return try {
            val response = bookshelfApiService.getBooks(query)
            if (response.isSuccessful) {
                response.body()?.items ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error get data", e)
            null
        }
    }

    private val TAG = "NetworkBookshelfRepository"
}