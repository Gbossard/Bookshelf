package com.example.bookshelf.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.bookshelf.data.local.model.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    // Category
    @Query("SELECT * FROM book_table ORDER BY searchOrder ASC")
    fun getAllBooks(): Flow<List<BookEntity>>

    // Search
    @Query("SELECT * FROM book_table WHERE searchQuery = :query ORDER BY searchOrder ASC")
    fun observeSearchResults(query: String): Flow<List<BookEntity>>


    // Book Details
    @Query("SELECT * FROM book_table WHERE id = :id")
    suspend fun getBookById(id: String): BookEntity?
    @Query("SELECT * FROM book_table WHERE id = :id")
    fun getBookByIdFlow(id: String): Flow<BookEntity?>

    // Favorite
    @Query("SELECT * FROM book_table WHERE isFavorite = 1")
    fun getAllFavorites(): Flow<List<BookEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBook(book: BookEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBooks(books: List<BookEntity>)

    @Query("DELETE FROM book_table WHERE searchQuery = :query")
    suspend fun clearSearchResults(query: String)

    @Transaction
    suspend fun updateSearchResults(query: String, newBooks: List<BookEntity>) {
        clearSearchResults(query)
        upsertBooks(newBooks)
    }
}