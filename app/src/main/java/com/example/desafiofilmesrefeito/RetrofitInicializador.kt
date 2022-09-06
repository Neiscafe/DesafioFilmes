package com.example.desafiofilmesrefeito

import com.example.desafiofilmes.services.FilmeService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RetrofitInicializador {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val filmeService = retrofit.create(FilmeService::class.java)
}