package com.example.bookshelf.model

data class Book(
    val id: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo
)

data class VolumeInfo(
    val title: String,
    val authors: List<String>,
    val publisher: String,
    val publishedDate: String,
    val description: String,
    val imageLinks: ImageLinks
)

data class ImageLinks(
    val smallThumbnail: String,
    val thumbnail: String
)

data class SaleInfo(
    val buyLink: String?
)