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

    @Query("SELECT EXISTS(SELECT 1 FROM Filme WHERE id = :filmeId) ")
    suspend fun checaSeExiste(filmeId: Int): Boolean

    @Query("DELETE FROM Filme WHERE id = :filmeId")
    suspend fun remove(filmeId: Int)

}
