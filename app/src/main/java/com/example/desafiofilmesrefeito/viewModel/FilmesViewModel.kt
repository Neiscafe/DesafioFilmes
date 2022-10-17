package com.example.desafiofilmesrefeito.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.RetrofitInicializador
import com.example.desafiofilmesrefeito.repository.FilmeFavoritoRepository
import kotlinx.coroutines.launch

class FilmesViewModel(val repository: FilmeFavoritoRepository) : ViewModel() {

    val filmesFavoritos: LiveData<List<Filme>> = repository.retornaFilmesFavoritos()

    val retrofitBuscaTodos = RetrofitInicializador().buscaTodosOsFilmes
    val retrofitPesquisa = RetrofitInicializador().pesquisaFilmes

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
        val response = retrofitBuscaTodos.buscaTodas("9106a44c761c36bbb02f24c16958a56a", pagina).body()!!.results
        pagina++
        return response
    }

    suspend fun pesquisar(nomeFilme: String): List<Filme>{
        val response = retrofitPesquisa.pesquisa("9106a44c761c36bbb02f24c16958a56a", nomeFilme).body()!!.results
        return response
    }

}