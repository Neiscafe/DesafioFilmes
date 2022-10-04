package com.example.desafiofilmesrefeito.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.desafiofilmes.model.Filme

@Entity
class FilmeFavorito (
    @PrimaryKey(autoGenerate = true)
    private val id: Int,
    private val favoritos: Filme,
    private val notaDoUsuario: Float = -1f
){


}