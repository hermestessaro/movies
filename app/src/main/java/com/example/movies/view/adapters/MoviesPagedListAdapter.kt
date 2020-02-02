package com.example.movies.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ItemMovieBinding
import com.example.movies.model.Movie
import com.example.movies.view.FavoritesFragmentDirections
import com.example.movies.view.PopularFragmentDirections
import com.example.movies.view.TopRatedFragmentDirections
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesPagedListAdapter(val listType: Int): MovieClickListener,
    PagedListAdapter<Movie, MoviesPagedListAdapter.MovieViewHolder>(object : DiffUtil.ItemCallback<Movie>(){
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie)
            = oldItem.movieId == newItem.movieId

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie)
            = oldItem == newItem

    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemMovieBinding>(inflater, R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val item = getItem(position)
        if(item != null){
            holder.view.movie = getItem(position)
            holder.view.listener = this
        }

    }

    override fun onMovieClicked(v: View) {
        val movieId = v.movieId.text.toString().toInt()

        when(listType){
            0 -> {
                val action = TopRatedFragmentDirections.ActionDetail()
                action.movieId = movieId
                Navigation.findNavController(v).navigate(action)
            }

            1 -> {
                val action = PopularFragmentDirections.ActionDetail()
                action.movieId = movieId
                Navigation.findNavController(v).navigate(action)
            }

            2 -> {
                val action = FavoritesFragmentDirections.ActionDetail()
                action.movieId = movieId
                Navigation.findNavController(v).navigate(action)
            }
        }


    }

    class MovieViewHolder(var view: ItemMovieBinding) : RecyclerView.ViewHolder(view.root)
}