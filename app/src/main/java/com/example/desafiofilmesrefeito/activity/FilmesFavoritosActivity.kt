package com.example.desafiofilmesrefeito.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiofilmes.activity.ListaFilmesActivity
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R
import com.example.desafiofilmesrefeito.adapter.ListaFavoritosAdapter
import com.example.desafiofilmesrefeito.database.FilmeDatabase
import com.example.desafiofilmesrefeito.repository.FilmeFavoritoRepository
import com.example.desafiofilmesrefeito.viewModel.FilmesFavoritosActivityViewModel
import com.example.desafiofilmesrefeito.viewModel.factory.FilmesFavoritosActivityViewModelFactory

class FilmesFavoritosActivity : AppCompatActivity() {
    private lateinit var filmeFavoritado: Filme
    private lateinit var adapter: ListaFavoritosAdapter
    private lateinit var manager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    private val viewModel by lazy {
        val repository =
            FilmeFavoritoRepository(FilmeDatabase.getInstance(this).getFilmeFavoritoDao())
        val factory = FilmesFavoritosActivityViewModelFactory(repository)
        ViewModelProviders.of(this, factory).get(FilmesFavoritosActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filmes_favoritos)

        inicializaItensLista()

        capturaIntent()

        salvaNoBancoAssincrona()

        implementandoClickListener()

        criaBotaoVoltarAppBar()

        criaBotaoFavoritosAppBar()
    }

    private fun inicializaItensLista() {
        recyclerView = findViewById(R.id.recyclerViewFavoritos)
        adapter = ListaFavoritosAdapter()
        manager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
    }

    private fun capturaIntent() {
        val extra = intent
        filmeFavoritado = extra.getSerializableExtra("FilmeFavoritado") as Filme
    }

    private fun salvaNoBancoAssincrona() {
        viewModel.salva(filmeFavoritado)
        viewModel.filmesFavoritos.observe(this) { listaAtualizada ->
            adapter.populaAdapter(listaAtualizada)
        }
    }

    private fun implementandoClickListener() {
        adapter.setOnItemClickListener(object : ListaFavoritosAdapter.onItemClickListener {
            override fun onItemClick(posicao: Int) {
                println("Click funcionando!")
            }
        })
    }

    private fun criaBotaoVoltarAppBar() {
        val voltar = findViewById<ImageView>(R.id.ImageVFlecha)
        voltar.setOnClickListener {
            val intent = Intent(this, ListaFilmesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun criaBotaoFavoritosAppBar() {
        val favoritos = findViewById<TextView>(R.id.TextVFavoritos)
        favoritos.setOnClickListener {
            val intent = Intent(this, FilmesFavoritosActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}