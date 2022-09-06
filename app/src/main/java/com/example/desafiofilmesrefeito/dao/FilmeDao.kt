package com.example.desafiofilmes.dao

import com.example.desafiofilmes.model.Filme

class FilmeDao() {

    companion object {
        private val listaFilmes = mutableListOf<Filme>()
    }

    fun adiciona(filme: Filme){
        listaFilmes.add(filme)
    }

    fun buscaTodos(): List<Filme>{
        return listaFilmes.toList()
    }

}