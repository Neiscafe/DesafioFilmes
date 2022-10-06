package com.example.desafiofilmesrefeito.repository

import androidx.lifecycle.LiveData
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.database.dao.FilmeFavoritoDao

class FilmeFavoritoRepository(val dao: FilmeFavoritoDao) {

    suspend fun salva(filme: Filme){
        dao.salva(filme)
    }

    fun retornaFilmesFavoritos(): LiveData<List<Filme>>{
        return dao.retornaFavoritos()
    }
}