package com.example.desafiofilmesrefeito.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.repository.FilmeFavoritoRepository
import kotlinx.coroutines.launch

class FilmesFavoritosActivityViewModel(val repository: FilmeFavoritoRepository) : ViewModel() {

    val filmesFavoritos: LiveData<List<Filme>> = repository.retornaFilmesFavoritos()


    fun salva(filme: Filme) {
        viewModelScope.launch {
            repository.salva(filme)
        }
    }
}