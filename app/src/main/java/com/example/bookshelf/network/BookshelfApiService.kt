package com.example.bookshelf.network

import com.example.bookshelf.model.Book
import com.example.bookshelf.model.ResponseData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookshelfApiService {

    @GET("volumes")
    suspend fun getBooks(@Query("q") query: String): Response<ResponseData>

    @GET("volumes/{id}")
    suspend fun getBook(@Path("id") id: String): Response<Book>
}