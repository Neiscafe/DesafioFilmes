package com.example.desafiofilmes.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.desafiofilmes.adapter.ListaFilmesAdapter
import com.example.desafiofilmes.dao.FilmeDao
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val listaDao = FilmeDao()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val filme = Filme("Arthur o rei")
        listaDao.adiciona(filme)
        val adapter = ListaFilmesAdapter(listaDao.buscaTodos(), this)
        Log.i("teste de inicializacao", "onCreate: $filme}")

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        setContentView(binding.root)
    }
}