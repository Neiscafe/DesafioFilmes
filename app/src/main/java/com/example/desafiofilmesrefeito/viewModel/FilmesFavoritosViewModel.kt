package com.example.desafiofilmesrefeito.viewModel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.activity.DescricaoFilme
import com.example.desafiofilmesrefeito.repository.FilmeFavoritoRepository
import kotlinx.coroutines.launch

class FilmesFavoritosViewModel(val repository: FilmeFavoritoRepository) : ViewModel() {

    val filmesFavoritos: LiveData<List<Filme>> = repository.retornaFilmesFavoritos()

    fun salva(filme: Filme) {
        viewModelScope.launch {
            repository.salva(filme)
        }
    }

    suspend fun checaSeExiste(filmeId: Int): Boolean {
        return repository.checaSeExiste(filmeId)
    }

    fun remove(filmeId: Int){
        viewModelScope.launch {
            repository.remove(filmeId)
        }
    }
}