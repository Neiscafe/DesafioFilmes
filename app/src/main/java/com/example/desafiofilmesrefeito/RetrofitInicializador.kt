package com.example.desafiofilmesrefeito

import com.example.desafiofilmes.services.FilmeService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitInicializador {
    val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://api.themoviedb.org/3/")
        .build()

    val notaService = retrofit.create(FilmeService::class.java)
}