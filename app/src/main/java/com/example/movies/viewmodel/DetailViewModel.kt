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

    fun changeFavorite(value: Boolean, movieId: Int){
        launch {
            MovieDatabase(getApplication()).movieDao().updateFavorited(value, movieId)
        }
    }



    fun returnMovieFavoritedState(): Int{
        if(movieLiveData.value?.favorited == true){
            return 1
        }
        else return 0
    }
}