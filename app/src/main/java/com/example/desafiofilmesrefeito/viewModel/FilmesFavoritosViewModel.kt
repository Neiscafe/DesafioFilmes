package com.example.desafiofilmesrefeito.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.RetrofitInicializador
import com.example.desafiofilmesrefeito.repository.FilmeFavoritoRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FilmesFavoritosViewModel(val repository: FilmeFavoritoRepository) : ViewModel() {

    val filmesFavoritos: LiveData<List<Filme>> = repository.retornaFilmesFavoritos()

    val retrofit = RetrofitInicializador().retrofit
    private var pagina = 1

    fun salva(filme: Filme) {
        viewModelScope.launch {
            repository.salva(filme)
        }
    }

    suspend fun checaSeExiste(filmeId: Int): Boolean {
        return repository.checaSeExiste(filmeId)
    }

    fun remove(filmeId: Int) {
        viewModelScope.launch {
            repository.remove(filmeId)
        }
    }

    suspend fun populaLista(): List<Filme> {
        val response = retrofit.buscaTodas("9106a44c761c36bbb02f24c16958a56a", pagina).body()!!.results
        pagina++
        return response
    }

}