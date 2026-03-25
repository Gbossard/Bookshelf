package com.example.bookshelf.data.repository

import android.util.Log
import com.example.bookshelf.data.network.model.Book
import com.example.bookshelf.data.network.BookshelfApiService

private const val TAG = "BookshelfRepository"
interface BookshelfRepository {
    suspend fun getBooks(query: String): List<Book>?

    suspend fun getBook(id: String): Book?
}

class NetworkBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
) : BookshelfRepository {
    private val bookCache = mutableMapOf<String, Book>()
    private val searchCache = mutableMapOf<String, List<Book>>()

    override suspend fun getBooks(query: String): List<Book>? {
        searchCache[query]?.let { cachedBooks ->
            Log.d(TAG, "Retour de la liste en cache pour: $query")
            return cachedBooks
        }
        return try {
            Log.d(TAG, "Appel réseau réel pour: $query")
            val response = bookshelfApiService.getBooks(query)

            if (response.isSuccessful) {
                val items = response.body()?.items ?: emptyList()
                searchCache[query] = items

                Log.d(TAG, "Success: ${items.size} books found")
                items
            } else {
                Log.e(TAG, "Error response: ${response.code()} - ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (error: Exception) {
            Log.e(TAG, "Exception during getBooks: ${error.message}", error)
            emptyList()
        }
    }

    override suspend fun getBook(id: String): Book? {
        bookCache[id]?.let { cachedBook ->
            return cachedBook
        }
        return try {
            val response = bookshelfApiService.getBook(id)

            if (response.isSuccessful) {
                response.body()?.also { book ->
                    bookCache[id] = book
                }
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