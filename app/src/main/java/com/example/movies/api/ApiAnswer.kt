package com.example.movies.api

import com.example.movies.model.Movie

class ApiAnswer (
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: List<Movie>
)