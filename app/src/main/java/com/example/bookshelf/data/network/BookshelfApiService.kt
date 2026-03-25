package com.example.bookshelf.data.network

import com.example.bookshelf.data.network.model.Book
import com.example.bookshelf.data.network.model.ResponseData
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