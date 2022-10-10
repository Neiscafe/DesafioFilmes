package com.example.desafiofilmesrefeito.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafiofilmes.activity.ListaFilmesActivity
import com.example.desafiofilmes.model.Filme
import com.example.desafiofilmesrefeito.R
import com.example.desafiofilmesrefeito.adapter.ListaFavoritosAdapter
import com.example.desafiofilmesrefeito.database.FilmeDatabase
import com.example.desafiofilmesrefeito.repository.FilmeFavoritoRepository
import com.example.desafiofilmesrefeito.viewModel.FilmesFavoritosViewModel
import com.example.desafiofilmesrefeito.viewModel.factory.FilmesFavoritosViewModelFactory
import org.w3c.dom.Text

class FilmesFavoritosActivity : AppCompatActivity() {
    private lateinit var listaDeFavoritos: List<Filme>
    private lateinit var adapter: ListaFavoritosAdapter
    private lateinit var manager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView

    private val viewModel by lazy {
        val repository =
            FilmeFavoritoRepository(FilmeDatabase.getInstance(this).getFilmeFavoritoDao())
        val factory = FilmesFavoritosViewModelFactory(repository)
        ViewModelProviders.of(this, factory).get(FilmesFavoritosViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filmes_favoritos)

        inicializaItensLista()

        PopulaListaAssincrona()

        implementandoClickListener()

        configuraAppBar()
    }


    private fun inicializaItensLista() {
        recyclerView = findViewById(R.id.recyclerViewFavoritos)
        adapter = ListaFavoritosAdapter()
        manager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
    }

    private fun PopulaListaAssincrona() {
        viewModel.filmesFavoritos.observe(this) { listaAtualizada ->
            listaDeFavoritos = listaAtualizada
            adapter.populaAdapter(listaAtualizada)
            if(listaAtualizada.isEmpty()){
                val listaVazia = findViewById<TextView>(R.id.TextVListaVazia)
                listaVazia.isVisible = true
            }
        }
    }

    private fun implementandoClickListener() {
        adapter.setOnItemClickListener(object : ListaFavoritosAdapter.onItemClickListener {
            override fun onItemClick(posicao: Int) {
                val filmeSendoEnviado = listaDeFavoritos.get(posicao)
                val intent = Intent(this@FilmesFavoritosActivity, DescricaoFilme::class.java)
                intent.putExtra("filmeEnviado", filmeSendoEnviado)
                startActivityForResult(intent, 1)
            }
        })
    }

    private fun configuraAppBar() {
        setTitle("Favoritos")
        criaBotaoVoltarAppBar()
        ocultaBotaoFavoritos()
    }

    private fun criaBotaoVoltarAppBar() {
        val voltar = findViewById<ImageView>(R.id.ImageVFlecha)
        voltar.setOnClickListener {
            val intent = Intent(this, ListaFilmesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun ocultaBotaoFavoritos() {
        val favoritos = findViewById<TextView>(R.id.TextVFavoritos)
        favoritos.isVisible = false
    }

}