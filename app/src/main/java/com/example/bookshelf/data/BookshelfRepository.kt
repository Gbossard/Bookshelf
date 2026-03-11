package com.example.bookshelf.data

import android.util.Log
import com.example.bookshelf.model.Book
import com.example.bookshelf.network.BookshelfApiService

private const val TAG = "BookshelfRepository"
interface BookshelfRepository {
    suspend fun getBooks(query: String): List<Book>?

    suspend fun getBook(id: String): Book?
}

class NetworkBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
) : BookshelfRepository {
    override suspend fun getBooks(query: String): List<Book>? {
        return try {
            val response = bookshelfApiService.getBooks(query)
            if (response.isSuccessful) {
                val items = response.body()?.items
                Log.d(TAG, "Success: ${items?.size ?: 0} books found")
                items ?: emptyList()
            } else {
                Log.e(TAG, "Error response: ${response.code()} - ${response.errorBody()?.string()}")
                null
            }
        } catch (error: Exception) {
            Log.e(TAG, "Exception during getBooks: ${error.message}", error)
            null
        }
    }

    override suspend fun getBook(id: String): Book? {
        return try {
            val response = bookshelfApiService.getBook(id)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e(TAG, "Error response getBook: ${response.code()}")
                null
            }
        } catch (error: Exception) {
            Log.e(TAG, "Exception during getBook: ${error.message}", error)
            null
        }
    }
}