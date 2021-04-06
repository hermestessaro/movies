package com.example.movies.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.movies.api.ApiService
import com.example.movies.database.MovieDatabase
import com.example.movies.model.Movie
import com.example.movies.util.MovieBoundaryCallback

class PopularViewModel(application: Application) : BaseViewModel(application) {

    private val service = ApiService(application)

    var moviesLiveData: LiveData<PagedList<Movie>>

    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(true)
        .build()

    init {
        moviesLiveData = initializedPagedListBuilder(config, application).build()
    }

    private fun initializedPagedListBuilder(
        config: PagedList.Config,
        application: Application
    ): LivePagedListBuilder<Int, Movie> {

        return LivePagedListBuilder<Int, Movie>(
            MovieDatabase(getApplication()).movieDao().getPopularPaged(), config)
            .setBoundaryCallback(MovieBoundaryCallback(2, service, MovieDatabase(application)))
    }
}