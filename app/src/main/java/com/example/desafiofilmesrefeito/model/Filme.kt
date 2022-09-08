package com.example.desafiofilmes.model

import android.util.Log

data class Filme(val title: String,
val poster_path: String,
val vote_average: String) {

    fun concatPoster(): String{
        val imagemCarregada = "https://image.tmdb.org/t/p/w500"+poster_path
        Log.i("imagemTest", "concatPoster: $imagemCarregada")
        return imagemCarregada
    }
}