package com.example.bookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseData(
    val kind: String? = null,
    val totalItems: Int? = 0,
    val items: List<Book>? = null
)

@Serializable
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo,
    val saleInfo: SaleInfo? = null
)

@Serializable
data class VolumeInfo(
    val title: String,
    val authors: List<String>? = listOf(),
    val publisher: String?  = "",
    val publishedDate: String? = "",
    val description: String?  = "",
    val imageLinks: ImageLinks? = null,
    val pageCount: Int? = 0,
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
    val type: String? = "",
    val identifier: String? = ""
)

@Serializable
data class SaleInfo(
    val buyLink: String? = ""
)