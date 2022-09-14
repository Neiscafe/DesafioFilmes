package com.example.desafiofilmes.model

import android.util.Log
import java.io.Serializable

data class Filme(val title: String,
val poster_path: String,
val vote_average: String,
val overview: String,
val release_date: String,
val vote_count: String,
val backdrop_path: String) : Serializable {

    fun concatPoster(): String{
        val imagemPosterCarregada = "https://image.tmdb.org/t/p/w500"+poster_path
        return imagemPosterCarregada
    }

    fun concatFundo(): String{
        val imagemFundoCarregada = "https://image.tmdb.org/t/p/w500"+backdrop_path
        return imagemFundoCarregada
    }
}