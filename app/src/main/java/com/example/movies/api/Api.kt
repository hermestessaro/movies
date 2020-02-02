package com.example.movies.api

import com.example.movies.model.Movie
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("top_rated")
    suspend fun getTopRated(@Query("page") page: Int): Response<ApiAnswer>

    @GET("popular")
    suspend fun getPopular(@Query("page") page: Int): Response<ApiAnswer>
}
