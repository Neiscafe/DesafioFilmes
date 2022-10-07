package com.example.desafiofilmesrefeito.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object DataUtil {

    @RequiresApi(Build.VERSION_CODES.O)
    fun periodoEmTexto(data: String): String {

        val dataParse = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(data))
        val dataFormatada = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dataParse)
        return dataFormatada

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun periodoEmTextoApenasAno(data: String): String{
        val dataParse = LocalDate.from(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(data))
        val dataFormatada = DateTimeFormatter.ofPattern("yyyy").format(dataParse)
        return dataFormatada
    }
}