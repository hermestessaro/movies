package com.example.movies.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movies.api.ApiService
import com.example.movies.database.MovieDatabase
import com.example.movies.model.Movie
import com.example.movies.util.MovieBoundaryCallback

class TopRatedViewModel(application: Application) : BaseViewModel(application) {

    private val service = ApiService(application)

    var moviesLiveData: LiveData<PagedList<Movie>>

    val config = PagedList.Config.Builder()
        .setPageSize(20)
        .build()

    init {
        moviesLiveData = initializedPagedListBuilder(config, application).build()
    }

    private fun initializedPagedListBuilder(
        config: PagedList.Config,
        application: Application
    ): LivePagedListBuilder<Int, Movie> {

        return LivePagedListBuilder<Int, Movie>(
            MovieDatabase(getApplication()).movieDao().getTopRatedPaged(), config)
                .setBoundaryCallback(MovieBoundaryCallback(1, service, MovieDatabase(application)))
    }
}