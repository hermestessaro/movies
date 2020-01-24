package com.example.movies.view


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.movies.R
import com.example.movies.database.MovieDatabase
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
        setHasOptionsMenu(true)
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
        currentMovie?.let {
            if(it.favorited == true){
                menu.getItem(R.id.action_favorite).setIcon(R.drawable.ic_star)
                menu.getItem(R.id.action_favorite).setChecked(true)
            }
            else{
                menu.getItem(R.id.action_favorite).setIcon(R.drawable.ic_star_border)
                menu.getItem(R.id.action_favorite).setChecked(false)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_favorite -> {
                currentMovie?.let{
                    if(it.favorited){
                        //tira dos favoritos
                        viewModel.changeFavorite(false, movieId)
                        changeMenuItemState(item)
                        it.favorited = false
                    }
                    else{
                        //coloca nos favoritos
                        viewModel.changeFavorite(true, movieId)
                        changeMenuItemState(item)
                        it.favorited = true
                    }
                }

                Toast.makeText(context, "favorito", Toast.LENGTH_SHORT).show()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun changeMenuItemState(item: MenuItem){
        if(item.isChecked){
            item.setIcon(R.drawable.ic_star_border)
            item.setChecked(true)
        }
        else{
            item.setIcon(R.drawable.ic_star)
            item.setChecked(true)
        }
    }


}
