package com.example.desafiofilmesrefeito.services

import com.example.desafiofilmesrefeito.model.FilmeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PesquisaService {
    @GET("search/movie")
    suspend fun pesquisa(
        @Query(value = "api_key") key: String,
        @Query("query")nomeDofilme: String
    ): Response<FilmeResponse>

}