package com.example.movies.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.movies.api.ApiAnswer
import com.example.movies.api.ApiService
import com.example.movies.database.MovieDatabase
import com.example.movies.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class TopRatedViewModel(application: Application) : BaseViewModel(application) {

    private val service = ApiService()
    private val disposable = CompositeDisposable()

    val movies = MutableLiveData<List<Movie>>()
    val moviesLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    fun fetchFromRemote() {
        loading.value = true
        disposable.add(
            service.getTopRated(1)
                .subscribeOn(Schedulers.newThread()) //to run the call to the api on a background thread
                .observeOn(AndroidSchedulers.mainThread()) //result of the process will be computed in the main thread
                .subscribeWith(object : DisposableSingleObserver<ApiAnswer>() { //observer of the single
                    override fun onSuccess(answer: ApiAnswer) {
                        //storeMoviesLocally(answer.results)
                        moviesRetrieved(answer.results)
                        Toast.makeText(getApplication(),"Movies retrieved from endpoint", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(e: Throwable) {
                        moviesLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun storeMoviesLocally(list: List<Movie>) {
        launch {
            val dao = MovieDatabase(getApplication()).movieDao()
            dao.deleteAllMovies()
            val result = dao.insertAll(*list.toTypedArray())
            var i = 0
            while (i < list.size) {
                list[i].uuid = result[i].toInt()
                i++
            }
            moviesRetrieved(list)
        }
    }

    private fun moviesRetrieved(moviesList: List<Movie>) {
        movies.value = moviesList
        moviesLoadError.value = false
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}