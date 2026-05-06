package com.example.bookshelf.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table")
data class BookEntity (
    @PrimaryKey
    val id: String,
    val title: String,
    val authors: String?,
    val publishers: String?,
    val publishedDate: String?,
    val description: String?,
    val pageCount: Int?,
    val thumbnail: String?,
    val isbn10: String?,
    val isbn13: String?,
    val buyLink: String?,
    val isFavorite: Boolean = false,
    val searchOrder: Int = 0,
    val searchQuery: String? = null,
)