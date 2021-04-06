package com.example.movies.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Movie(
    @PrimaryKey
    @ColumnInfo(name="movie_id")
    @SerializedName("id")
    val movieId: String,

    @ColumnInfo(name="movie_title")
    @SerializedName("title")
    val movieTitle: String?,

    @ColumnInfo(name="vote_average")
    @SerializedName("vote_average")
    val voteAverage: String?,

    @SerializedName("popularity")
    val popularity: String?,

    @SerializedName("overview")
    val synopsis: String?,

    @ColumnInfo(name="release_date")
    @SerializedName("release_date")
    val releaseDate: String?,

    @ColumnInfo(name="image_path")
    @SerializedName("poster_path")
    val imagePath: String?,

    @ColumnInfo(name="favorited")
    var favorited: Boolean
)