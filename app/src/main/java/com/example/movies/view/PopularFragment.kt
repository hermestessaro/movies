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
import com.example.movies.viewmodel.PopularViewModel
import kotlinx.android.synthetic.main.fragment_list_movies.*

class PopularFragment : Fragment() {

    private lateinit var viewModel: PopularViewModel
    private val moviesListAdapter = MoviesPagedListAdapter(1)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(PopularViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list_movies, container, false)

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
}