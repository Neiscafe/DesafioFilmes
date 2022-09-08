package com.example.desafiofilmes.services

import com.example.desafiofilmesrefeito.model.FilmeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmeService {
    @GET("movie/popular")
    suspend fun buscaTodas(
        @Query(value = "api_key") key: String = "9106a44c761c36bbb02f24c16958a56a"
    ): Response<FilmeResponse>
}