package com.example.bookshelf.data.mapper

import com.example.bookshelf.data.local.model.BookEntity
import com.example.bookshelf.data.network.model.Book

fun Book.toEntity(isFavorite: Boolean = false, searchOrder: Int = 0): BookEntity {
    val isbn10 = this.volumeInfo.industryIdentifiers?.find { it.type == "ISBN_10" }?.identifier
    val isbn13 = this.volumeInfo.industryIdentifiers?.find { it.type == "ISBN_13" }?.identifier

    return BookEntity(
        id = this.id,
        title = this.volumeInfo.title,
        authors = this.volumeInfo.authors?.joinToString(", "),
        publishers = this.volumeInfo.publisher,
        publishedDate = this.volumeInfo.publishedDate,
        description = this.volumeInfo.description,
        pageCount = this.volumeInfo.pageCount,
        thumbnail = this.volumeInfo.imageLinks?.thumbnail?.replace("http://", "https://"),
        isbn10 = isbn10,
        isbn13 = isbn13,
        buyLink = this.saleInfo?.buyLink,
        isFavorite = isFavorite,
        searchOrder = searchOrder
    )
}