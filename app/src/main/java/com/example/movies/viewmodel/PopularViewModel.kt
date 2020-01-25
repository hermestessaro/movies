package com.example.movies.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movies.api.ApiAnswer
import com.example.movies.api.ApiService
import com.example.movies.database.MovieDatabase
import com.example.movies.model.Movie
import com.example.movies.model.MovieDataSource
import com.example.movies.util.MovieBoundaryCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class PopularViewModel(application: Application) : BaseViewModel(application) {

    private val service = ApiService(application)

    var moviesLiveData: LiveData<PagedList<Movie>>

    val config = PagedList.Config.Builder()
        .setPageSize(20)
        .build()

    init {
        moviesLiveData = initializedPagedListBuilder(config, application).build()
    }

    fun initializedPagedListBuilder(
        config: PagedList.Config,
        application: Application
    ): LivePagedListBuilder<Int, Movie> {
        val dataSourceFactory = object : DataSource.Factory<Int, Movie>() {
            override fun create(): DataSource<Int, Movie> {
                return MovieDataSource(application, 1)
            }
        }

        return LivePagedListBuilder<Int, Movie>(
            MovieDatabase(getApplication()).movieDao().getPopularPaged(),
            config
        )
            .setBoundaryCallback(MovieBoundaryCallback(2, service, MovieDatabase(application)))
    }
}