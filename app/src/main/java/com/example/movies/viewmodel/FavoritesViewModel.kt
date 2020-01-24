package com.example.movies.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movies.api.ApiService
import com.example.movies.database.MovieDatabase
import com.example.movies.model.Movie
import com.example.movies.model.MovieDataSource
import com.example.movies.util.MovieBoundaryCallback
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : BaseViewModel(application) {

    val moviesLiveData = MutableLiveData<List<Movie>>()

    init {
        fetchFromDatabase()
    }

    private fun fetchFromDatabase(){
        launch {
            val movies = MovieDatabase(getApplication()).movieDao().getAllFavorited()
            moviesReturned(movies)
        }
    }

    private fun moviesReturned(listMovies: List<Movie>){
        moviesLiveData.value = listMovies
    }
}