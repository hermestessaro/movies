package com.example.movies.api

import androidx.paging.PagedList
import com.example.movies.model.Movie

class ApiAnswer (
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: PagedList<Movie>
)