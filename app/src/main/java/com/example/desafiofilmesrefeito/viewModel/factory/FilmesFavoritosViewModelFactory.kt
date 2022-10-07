package com.example.desafiofilmesrefeito.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.desafiofilmesrefeito.repository.FilmeFavoritoRepository
import com.example.desafiofilmesrefeito.viewModel.FilmesFavoritosViewModel

class FilmesFavoritosViewModelFactory(val repository: FilmeFavoritoRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return FilmesFavoritosViewModel(repository) as T
    }
}