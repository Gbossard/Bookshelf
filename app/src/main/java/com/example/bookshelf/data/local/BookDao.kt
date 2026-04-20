package com.example.bookshelf.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookshelf.data.local.model.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM book_table ORDER BY searchOrder ASC")
    fun getAllBooks(): Flow<List<BookEntity>>


    @Query("SELECT * FROM book_table WHERE isSearchResult = 1 ORDER BY searchOrder ASC")
    fun getSearchResults(): Flow<List<BookEntity>>


    @Query("SELECT * FROM book_table WHERE id = :id")
    suspend fun getBookById(id: String): BookEntity?
    @Query("SELECT * FROM book_table WHERE id = :id")
    fun getBookByIdFlow(id: String): Flow<BookEntity?>


    @Query("SELECT * FROM book_table WHERE isFavorite = 1")
    fun getAllFavorites(): Flow<List<BookEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBook(book: BookEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBooks(books: List<BookEntity>)


    @Query("UPDATE book_table SET isSearchResult = 0")
    suspend fun clearSearchFlags()

    @Query("DELETE FROM book_table WHERE isSearchResult = 0 AND isFavorite = 0")
    suspend fun clearUnusedBooks()
}