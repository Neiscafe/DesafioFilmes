package com.example.desafiofilmesrefeito

import com.example.desafiofilmesrefeito.services.FilmeService
import com.example.desafiofilmesrefeito.services.PesquisaService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInicializador {
    val buscaTodosOsFilmes: FilmeService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(FilmeService::class.java)

    val pesquisaFilmes: PesquisaService = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(PesquisaService::class.java)
}