package com.example.movies.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.movies.R
import com.example.movies.databinding.FragmentDetailBinding
import com.example.movies.model.Movie
import com.example.movies.viewmodel.DetailViewModel

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel
    private var movieId = 0

    private lateinit var dataBinding: FragmentDetailBinding
    private var currentMovie: Movie? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            movieId = DetailFragmentArgs.fromBundle(it).movieId
        }

        viewModel = ViewModelProviders.of(this).get(DetailViewModel::class.java)
        viewModel.fetchMovie(movieId)

        observeViewModel()
    }

    fun observeViewModel(){
        viewModel.movieLiveData.observe(this, Observer {movie ->
            currentMovie = movie
            movie?.let{
                dataBinding.movie = movie
            }
        })
    }


}
