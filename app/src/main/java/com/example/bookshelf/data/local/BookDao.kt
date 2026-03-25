package com.example.bookshelf.data.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookshelf.model.local.BookEntity
import kotlinx.coroutines.flow.Flow

interface BookDao {
    @Query("SELECT * FROM book_table")
    fun getAllBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM book_table WHERE id = :id")
    suspend fun getBookById(id: String): BookEntity?

    @Query("SELECT * FROM book_table WHERE isFavorite = 1")
    fun getAllFavorites(): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(book: BookEntity)

    @Delete
    suspend fun deleteFavorite(book: BookEntity)

    @Query("DELETE FROM book_table WHERE isFavorite = 0")
    suspend fun clearCache()
}