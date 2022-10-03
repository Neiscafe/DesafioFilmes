package com.example.desafiofilmesrefeito.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.desafiofilmes.model.Filme

@Entity
class FilmeFavorito (
    @PrimaryKey
    private val favoritos: Filme,
    val notaDoUsuario: Float = -1f
){


}