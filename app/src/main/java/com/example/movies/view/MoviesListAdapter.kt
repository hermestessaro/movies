package com.example.movies.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.R
import com.example.movies.databinding.ItemMovieBinding
import com.example.movies.model.Movie
import kotlinx.android.synthetic.main.item_movie.view.*

class MoviesListAdapter(val moviesList: ArrayList<Movie>) : RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder>(), MovieClickListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemMovieBinding>(inflater, R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount() = moviesList.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.view.movie = moviesList[position]
        holder.view.listener = this
    }

    override fun onMovieClicked(v: View) {
        val movieId = v.movieId.text.toString().toInt()
        val action = FavoritesFragmentDirections.ActionDetail()
        action.movieId = movieId
        Navigation.findNavController(v).navigate(action)
    }

    fun updateList(newMovies: List<Movie>){
        moviesList.clear()
        moviesList.addAll(newMovies)
        notifyDataSetChanged()
    }


    class MovieViewHolder(var view: ItemMovieBinding) : RecyclerView.ViewHolder(view.root)
}