package com.example.desafiofilmesrefeito.database.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.desafiofilmes.model.Filme

@Dao
interface FilmeFavoritoDao {

    @Insert
    suspend fun salva(filme: Filme)

    @Query("SELECT * FROM Filme")
    fun retornaFavoritos(): LiveData<List<Filme>>

}
