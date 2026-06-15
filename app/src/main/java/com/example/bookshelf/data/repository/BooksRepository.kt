package com.example.bookshelf.data.repository

import android.util.Log
import com.example.bookshelf.data.local.BookDao
import com.example.bookshelf.data.local.model.BookEntity
import com.example.bookshelf.data.mapper.toEntity
import com.example.bookshelf.data.network.BookshelfApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "DefaultBookRepository"
interface BookshelfRepository {
    // Category
    fun getAllBooks(): Flow<List<BookEntity>>
    suspend fun refreshBooks(query: String)

    // Search
    fun observeSearchResults(query: String): Flow<List<BookEntity>>
    suspend fun refreshSearch(query: String)

    // Book Details
    fun getBookByIdFlow(id: String): Flow<BookEntity?>
    suspend fun getBookById(id: String): BookEntity?

    // Favorite
    fun getAllFavorites(): Flow<List<BookEntity>>
    suspend fun toggleFavorite(book: BookEntity)
}

@Singleton
class DefaultBooksRepository @Inject constructor(
    private val bookshelfApiService: BookshelfApiService,
    private val bookDao: BookDao
): BookshelfRepository {
    // Category
    override fun getAllBooks(): Flow<List<BookEntity>> = bookDao.getAllBooks()

    override suspend fun refreshBooks(query: String) {
        try {
            val response = bookshelfApiService.getBooks(query)

            if (response.isSuccessful) {
                val items = response.body()?.items ?: emptyList()

                val entities = items.mapIndexed { index, networkBook ->
                    val existingBook = bookDao.getBookById(networkBook.id)
                    networkBook.toEntity(
                        isFavorite = existingBook?.isFavorite ?: false,
                        searchOrder = index,
                        searchQuery = query
                    )
                }
                bookDao.upsertBooks(entities)
            }
        } catch (error: Exception) {
            Log.e(TAG, "Error refreshBooks", error)
        }
    }


    // Search
    override suspend fun refreshSearch(query: String) {
        try {
            val response = bookshelfApiService.getBooks(query)

            if (response.isSuccessful) {
                val items = response.body()?.items ?: emptyList()

                val entities = items.mapIndexed { index, networkBook ->
                    val existingBook = bookDao.getBookById(networkBook.id)
                    networkBook.toEntity(
                        isFavorite = existingBook?.isFavorite ?: false,
                        searchOrder = index,
                        searchQuery = query,
                    )
                }
                bookDao.updateSearchResults(query, entities)
            } else {
                Log.e(TAG, "Error response searchBooks: ${response.code()} ${response.message()}")
                throw Exception("HTTP ${response.code()}")
            }
        } catch (error: Exception) {
            Log.e(TAG, "Error searchBooks: ", error)
            throw error
        }
    }

    override fun observeSearchResults(query: String): Flow<List<BookEntity>> = bookDao.observeSearchResults(query)


    // Book Details
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


    // Favorite
    override fun getAllFavorites(): Flow<List<BookEntity>> = bookDao.getAllFavorites()

    override suspend fun toggleFavorite(book: BookEntity) {
        val updatedBook = book.copy(isFavorite = !book.isFavorite)
        bookDao.upsertBook(updatedBook)
    }
}