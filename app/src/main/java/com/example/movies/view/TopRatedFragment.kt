package com.example.movies.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.model.Movie
import com.example.movies.view.adapters.MoviesPagedListAdapter
import com.example.movies.viewmodel.TopRatedViewModel
import kotlinx.android.synthetic.main.fragment_list_movies.*

class TopRatedFragment : Fragment() {
    private lateinit var viewModel: TopRatedViewModel
    private val moviesListAdapter = MoviesPagedListAdapter(0)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_list_movies, container, false)
        viewModel = ViewModelProviders.of(this).get(TopRatedViewModel::class.java)
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moviesListAdapter
        }

        observeViewModel()

    }

    fun observeViewModel() {
        viewModel.moviesLiveData.observe (this, Observer<PagedList<Movie>>{
            moviesList.visibility = View.VISIBLE
            moviesListAdapter.submitList(it)
        })
    }
    /*
    fun checkNetwork(): Boolean{
        val connManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connManager.activeNetworkInfo
        if(activeNetwork != null){
            return activeNetwork.isConnectedOrConnecting
        }
        return false
    }*/
}