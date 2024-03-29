package com.example.movies.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.paging.PagedList
import com.example.movies.api.ApiAnswer
import com.example.movies.api.ApiService
import com.example.movies.database.MovieDatabase
import com.example.movies.model.Movie
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import java.util.concurrent.Executor

class MovieBoundaryCallback(
    private val code: Int,
    private val service: ApiService,
    private val database: MovieDatabase
) : PagedList.BoundaryCallback<Movie>() {

    private val TOP_RATED = 0
    private val POPULAR = 1
    private val FAVORITES = 2
    var lastRequestedPage = 1
    val disposable = CompositeDisposable()

    //no items in db
    @MainThread
    override fun onZeroItemsLoaded() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = determineServiceCall(code, lastRequestedPage)
            withContext(Dispatchers.Main){
                try {
                    if(response?.isSuccessful!!){
                        saveInDatabase(response.body()!!.results)
                    }
                } catch (e: HttpException){
                    e.printStackTrace()
                } catch (e: Throwable){
                    e.printStackTrace()
                }

            }
        }

    }

    //end of data in db
    override fun onItemAtEndLoaded(itemAtEnd: Movie) {
        lastRequestedPage++
        CoroutineScope(Dispatchers.IO).launch {
            val response = determineServiceCall(code, lastRequestedPage)
            withContext(Dispatchers.Main){
                try {
                    if(response?.isSuccessful!!){
                        saveInDatabase(response.body()!!.results)
                    }
                } catch (e: HttpException){
                    e.printStackTrace()
                } catch (e: Throwable){
                    e.printStackTrace()
                }

            }
        }
    }


    private suspend fun determineServiceCall(code: Int, page:Int): Response<ApiAnswer?>?{
        if(code == TOP_RATED){
            return service.getTopRated(page)
        }
        if(code == POPULAR){
            return service.getPopular(page)
        }
        if(code == FAVORITES){
            //doest nothing
        }
        return null
    }



    private fun saveInDatabase(movies: List<Movie>){
        GlobalScope.launch {
            for(movie in movies){
                movie.favorited = false
            }
            database.movieDao().insertAll(movies)
        }
    }

    fun Context.isConnectedToNetwork(): Boolean {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting() ?: false
    }
}