package com.example.movies.api

import android.content.Context
import com.example.movies.model.Movie
import com.example.movies.util.NetworkConnectionInterceptor
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiService(val context: Context) {
    private val BASE_URL = "https://api.themoviedb.org/3/movie/"
    private val API_KEY = "7c16b4aacfe7f1fecffdb9ef52856a41"
    private val API_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI3YzE2YjRhYWNmZTdmMWZlY2ZmZGI5ZWY1Mjg1NmE0MSIsInN1YiI6IjVlMjc1ZGU1NGNhNjc2MDAxYTNmOWVkMiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.TOX0WDflls6qZoA6_hqZeBHPw8zZ6l2X97pDi13zlYk"

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(getClient())
        .build()
        .create(Api::class.java)

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor{
            val token ="Bearer $API_ACCESS_TOKEN"
            it.proceed(
                it.request().newBuilder()
                    .addHeader("Authorization", token)
                    .addHeader("Content-Type", "application/json;charset=utf-8")
                    .build()
            )
        }
        .addInterceptor(NetworkConnectionInterceptor(context))
        .build()
    }

    suspend fun getTopRated(page: Int): Response<ApiAnswer?>{
        return api.getTopRated(page)
    }

    suspend fun getPopular(page: Int): Response<ApiAnswer?>{
        return api.getPopular(page)
    }
}