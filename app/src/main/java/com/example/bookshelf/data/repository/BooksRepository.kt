package com.example.bookshelf.data.repository

import android.util.Log
import com.example.bookshelf.data.local.BookDao
import com.example.bookshelf.data.local.model.BookEntity
import com.example.bookshelf.data.mapper.toEntity
import com.example.bookshelf.data.network.BookshelfApiService
import kotlinx.coroutines.flow.Flow

private const val TAG = "DefaultBookRepository"
interface BookshelfRepository {
    fun getAllBooks(): Flow<List<BookEntity>>
    suspend fun refreshBooks(query: String)
    fun getBookByIdFlow(id: String): Flow<BookEntity?>
    suspend fun getBookById(id: String): BookEntity?
    fun getAllFavorites(): Flow<List<BookEntity>>
    suspend fun addFavorite(book: BookEntity)
    suspend fun deleteFavorite(book: BookEntity)
    suspend fun updateBookCache(book: BookEntity)
    suspend fun clearCache()
}

class DefaultBooksRepository(
    private val bookshelfApiService: BookshelfApiService,
    private val bookDao: BookDao
): BookshelfRepository {
    override fun getAllBooks(): Flow<List<BookEntity>> = bookDao.getAllBooks()
    override fun getAllFavorites(): Flow<List<BookEntity>> = bookDao.getAllFavorites()

    override suspend fun refreshBooks(query: String) {
        try {
            val response = bookshelfApiService.getBooks(query)

            if (response.isSuccessful) {
                val items = response.body()?.items ?: emptyList()

                val entities = items.map { networkBook ->
                    val existingBook = bookDao.getBookById(networkBook.id)
                    val isFavorite = existingBook?.isFavorite ?: false

                    networkBook.toEntity(isFavorite = isFavorite)
                }
                bookDao.clearCache()
                bookDao.upsertBooks(entities)
            }
        } catch (error: Exception) {
            Log.e(TAG, "Error refreshBooks", error)
        }
    }

    override suspend fun getBookById(id: String): BookEntity? {
        val localBook = bookDao.getBookById(id)
        if (localBook != null) return localBook

        return try {
            val response = bookshelfApiService.getBook(id)

            if (response.isSuccessful) {
                response.body()?.let { networkBook ->
                    val entity = networkBook.toEntity(isFavorite = false)
                    bookDao.upsertBook(entity)
                    entity
                }
            } else {
                Log.e(TAG, "Error response getBookById: ${response.code()} ${response.message()}")
                null
            }
        } catch (error: Exception) {
            Log.e(TAG, "Exception during getBook: ${error.message}", error)
            null
        }
    }

    override fun getBookByIdFlow(id: String): Flow<BookEntity?> {
        return bookDao.getBookByIdFlow(id)

    }

    override suspend fun addFavorite(book: BookEntity) = bookDao.upsertBook(book.copy(isFavorite = true))
    override suspend fun deleteFavorite(book: BookEntity) = bookDao.upsertBook(book.copy(isFavorite = false))
    override suspend fun updateBookCache(book: BookEntity) = bookDao.upsertBook(book)
    override suspend fun clearCache() = bookDao.clearCache()
}