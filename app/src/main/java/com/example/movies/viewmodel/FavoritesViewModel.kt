package com.example.movies.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.movies.database.MovieDatabase
import com.example.movies.model.Movie
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : BaseViewModel(application) {

    val moviesLiveData = MutableLiveData<List<Movie>>()

    init {
        fetchFromDatabase()
    }

    fun fetchFromDatabase(){
        launch {
            val movies = MovieDatabase(getApplication()).movieDao().getAllFavorited()
            moviesReturned(movies)
        }
    }

    private fun moviesReturned(listMovies: List<Movie>){
        moviesLiveData.value = listMovies
    }
}