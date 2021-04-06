package com.example.movies.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.movies.model.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>): List<Long>

    @Query("SELECT * FROM movie ORDER BY vote_average DESC")
    fun getTopRatedPaged(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    fun getPopularPaged(): DataSource.Factory<Int, Movie>

    @Query("SELECT * FROM movie WHERE movie_id = :movieId")
    suspend fun getMovie(movieId: Int): Movie

    @Query("SELECT * FROM movie WHERE favorited = 1")
    suspend fun getAllFavorited(): List<Movie>

    @Query("UPDATE movie SET favorited = :favoritedValue WHERE movie_id = :movieId")
    suspend fun updateFavorited(favoritedValue: Boolean, movieId: Int)

    @Query("DELETE FROM movie")
    fun deleteAllMovies()
}