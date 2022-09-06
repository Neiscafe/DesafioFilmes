package com.example.desafiofilmes.services

import com.example.desafiofilmesrefeito.model.FilmeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface FilmeService {
    @GET("movie/popular")

    suspend fun buscaTodas(@Query(value = "api_key") key: String = "9106a44c761c36bbb02f24c16958a56a"): Call<FilmeResponse>
}