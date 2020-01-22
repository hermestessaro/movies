package com.example.movies.view

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.util.InfiniteScrollListener
import com.example.movies.viewmodel.TopRatedViewModel
import kotlinx.android.synthetic.main.fragment_list_movies.*

class TopRatedFragment : Fragment() {
    private lateinit var viewModel: TopRatedViewModel
    private val moviesListAdapter = MoviesListAdapter(arrayListOf())
    var page = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_movies, container, false)
        viewModel = ViewModelProviders.of(this).get(TopRatedViewModel::class.java)
        if(checkNetwork()){
            viewModel.fetchFromRemote(page)
        }
        else{
            viewModel.fetchFromDatabase()
        }


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moviesListAdapter
        }

        refresh_layout.setOnRefreshListener {
            moviesList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.fetchFromRemote(page + 1)
            refresh_layout.isRefreshing = false
        }

        observeViewModel()
    }


    fun observeViewModel(){
        viewModel.movies.observe(this, Observer { movies ->
            movies?.let{
                moviesList.visibility = View.VISIBLE
                moviesListAdapter.updateMovielist(movies)
            }
        })

        viewModel.moviesLoadError.observe(this, Observer { isError ->
            isError?.let{
                listError.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer {isLoading ->
            isLoading?.let{
                loadingView.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    listError.visibility = View.GONE
                    moviesList.visibility = View.GONE
                }
            }
        })
    }

    fun checkNetwork(): Boolean{
        val connManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connManager.activeNetworkInfo
        if(activeNetwork != null){
            return activeNetwork.isConnectedOrConnecting
        }
        return false
    }
}