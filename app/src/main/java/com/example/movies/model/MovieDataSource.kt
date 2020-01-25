package com.example.movies.model

import android.app.Application
import androidx.paging.PageKeyedDataSource
import com.example.movies.api.ApiAnswer
import com.example.movies.api.ApiService
import com.example.movies.database.MovieDatabase
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MovieDataSource(app: Application, private val code: Int):PageKeyedDataSource<Int, Movie>() {
    private val service = ApiService(app)
    private val disposable = CompositeDisposable()
    private val application = app
    private val TOP_RATED = 0
    private val POPULAR = 1

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        disposable.add(determineServiceCall(code, 1)!!
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread()) //result of the process will be computed in the main thread
            .subscribeWith(object : DisposableSingleObserver<ApiAnswer>() { //observer of the single
                override fun onSuccess(answer: ApiAnswer) {
                    storeMoviesLocally(answer.results, application)
                    callback.onResult(answer.results, answer.page, answer.page+1)
                }

                override fun onError(e: Throwable) {
                    callback.onError(e)
                }
            })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        disposable.add(determineServiceCall(code, params.key)!!
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<ApiAnswer>() {
                override fun onSuccess(answer: ApiAnswer) {
                    storeMoviesLocally(answer.results, application)
                    callback.onResult(answer.results, answer.page+1)
                }

                override fun onError(e: Throwable) {
                    callback.onError(e)
                }
            })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        disposable.add(determineServiceCall(code, 1)!!
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread()) //result of the process will be computed in the main thread
            .subscribeWith(object : DisposableSingleObserver<ApiAnswer>() { //observer of the single
                override fun onSuccess(answer: ApiAnswer) {
                    //Log.d("result", answer.results[10].movieTitle)
                    //callback.onResult(answer.results, answer.page)
                }

                override fun onError(e: Throwable) {
                    callback.onError(e)
                }
            })
        )
    }

    private fun storeMoviesLocally(list: List<Movie>, application: Application) {
        GlobalScope.launch {
            val dao = MovieDatabase(application).movieDao()
            for(movie in list){
                movie.favorited = false
            }
            dao.insertAll(list)
        }

    }

    private fun determineServiceCall(code: Int, page:Int): Single<ApiAnswer>?{
        if(code == TOP_RATED){
            return service.getTopRated(page)
        }
        else if(code == POPULAR){
            return service.getPopular(page)
        }
        return null
    }

}