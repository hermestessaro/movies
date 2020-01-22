package com.example.movies.api

import com.example.movies.model.Movie
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("top_rated")
    fun getTopRated(@Query("page") page: Int): Single<ApiAnswer>

    @GET("popular")
    fun getPopular(@Query("page") page: Int): Single<ApiAnswer>
}
