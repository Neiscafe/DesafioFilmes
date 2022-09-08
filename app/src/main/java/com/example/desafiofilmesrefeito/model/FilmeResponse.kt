package com.example.desafiofilmesrefeito.model

import com.example.desafiofilmes.model.Filme
import com.squareup.moshi.Json

data class FilmeResponse(
    @field:Json(name = "page") val page: Int,
    @field:Json(name = "results")val results: List<Filme>,
    @field:Json(name = "total_results")val total_results: Int,
    @field:Json(name = "total_pages")val total_pages: Int
)