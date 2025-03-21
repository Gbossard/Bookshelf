package com.example.bookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseData(
    val kind: String,
    val totalItems: Int,
    val items: List<Book>?
)

@Serializable
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo
)

@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>? = listOf(),
    val publisher: String?  = "",
    val publishedDate: String,
    val description: String?  = "",
    val imageLinks: ImageLinks? = null,
    val pageCount: Int,
    val industryIdentifiers: List<IndustryIdentifiers>? = listOf(),
)

@Serializable
data class ImageLinks(
    val smallThumbnail: String? = "",
    val thumbnail: String? = ""
) {
    val httpsThumbnail: String?
        get() = thumbnail?.replace("http", "https")
    val httpsSmallThumbnail: String?
        get() = smallThumbnail?.replace("http", "https")

}

@Serializable
data class IndustryIdentifiers(
    val type: String,
    val identifier: String
)

@Serializable
data class SaleInfo(
    val buyLink: String? = ""
)