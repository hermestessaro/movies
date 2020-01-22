package com.example.movies.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.viewmodel.PopularViewModel
import kotlinx.android.synthetic.main.fragment_list_movies.*

class PopularFragment : Fragment() {

    private lateinit var viewModel: PopularViewModel
    private val moviesListAdapter = MoviesListAdapter(arrayListOf())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(PopularViewModel::class.java)
        viewModel.fetchFromRemote()
        val root = inflater.inflate(R.layout.fragment_list_movies, container, false)

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
            viewModel.fetchFromRemote()
            refresh_layout.isRefreshing = false
        }

        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.movies.observe(this, Observer { movies ->
            movies?.let {
                moviesList.visibility = View.VISIBLE
                moviesListAdapter.updateMovielist(movies)
            }
        })

        viewModel.moviesLoadError.observe(this, Observer { isError ->
            isError?.let {
                listError.visibility = if (it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    listError.visibility = View.GONE
                    moviesList.visibility = View.GONE
                }
            }
        })
    }
}