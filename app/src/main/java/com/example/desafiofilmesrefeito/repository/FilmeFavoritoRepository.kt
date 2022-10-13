package com.example.desafiofilmesrefeito.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.database.dao.FilmeFavoritoDao
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FilmeFavoritoRepository(val dao: FilmeFavoritoDao) {

    suspend fun salva(filme: Filme){
        dao.salva(filme)
    }

    fun retornaFilmesFavoritos(): LiveData<List<Filme>>{
        return dao.retornaFavoritos()
    }

    suspend fun checaSeExiste(filmeId: Int): Boolean{
        return dao.checaSeExiste(filmeId)
    }

    suspend fun remove(filmeId: Int) {
        dao.remove(filmeId)
    }



}