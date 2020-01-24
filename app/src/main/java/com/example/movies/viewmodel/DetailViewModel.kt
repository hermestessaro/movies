package com.example.movies.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.movies.database.MovieDatabase
import com.example.movies.model.Movie
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): BaseViewModel(application) {

    val movieLiveData = MutableLiveData<Movie>()

    fun fetchMovie(id: Int){
        launch {
            val movie = MovieDatabase(getApplication()).movieDao().getMovie(id)
            movieLiveData.value = movie
        }
    }
}