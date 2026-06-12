package com.example.bookshelf.data.network

import com.example.bookshelf.data.network.model.Book
import com.example.bookshelf.data.network.model.ResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.bookshelf.BuildConfig

interface BookshelfApiService {

    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String,
        @Query("key") apiKey: String = BuildConfig.BOOKS_API_KEY
    ): Response<ResponseData>

    @GET("volumes/{id}")
    suspend fun getBook(
        @Path("id") id: String,
        @Query("key") apiKey: String = BuildConfig.BOOKS_API_KEY
    ): Response<Book>
}