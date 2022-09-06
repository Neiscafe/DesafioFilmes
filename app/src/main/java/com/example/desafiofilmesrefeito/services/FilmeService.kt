package com.example.desafiofilmes.services

import com.example.desafiofilmes.model.Filme
import retrofit2.Call
import retrofit2.http.GET

public interface FilmeService {
    @GET("")
    fun buscaTodas(): Call<List<Filme>>
}