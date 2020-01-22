package com.example.movies.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.movies.model.Movie

@Dao
interface MovieDao {
    @Insert
    suspend fun insertAll(vararg movies: Movie): List<Long>

    @Query("SELECT * FROM movie ORDER BY vote_average")
    suspend fun getTopRated(): List<Movie>

    @Query("SELECT * FROM movie ORDER BY popularity")
    suspend fun getPopular(): List<Movie>

    @Query("SELECT * FROM movie WHERE uuid = :movieId")
    suspend fun getMovie(movieId: Int): Movie

    @Query("DELETE FROM movie")
    suspend fun deleteAllMovies()

    //@Query("SELECT * FROM movie WHERE favorited = 1")
    //suspend fun getAllFavorites()
}