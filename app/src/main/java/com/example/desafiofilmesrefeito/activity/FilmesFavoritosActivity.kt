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

class FilmesFavoritosActivity : AppCompatActivity() {
    private var listaSelecionados = mutableListOf<Filme>()
    private lateinit var listaFilmes: List<Filme>
    private lateinit var adapter: ListaFavoritosAdapter
    private lateinit var manager: LinearLayoutManager
    private lateinit var recyclerView: RecyclerView
    private var estado = 0

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

        populaListaAssincrona()

        configuraLongListener()

        configuraAppBar()
    }


    private fun inicializaItensLista() {
        recyclerView = findViewById(R.id.recyclerViewFavoritos)
        adapter = ListaFavoritosAdapter()
        manager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
    }

    private fun populaListaAssincrona() {
        viewModel.filmesFavoritos.observe(this) { listaAtualizada ->
            listaFilmes = listaAtualizada
            adapter.populaAdapter(listaAtualizada)
            if (listaAtualizada.isEmpty()) {
                val listaVazia = findViewById<TextView>(R.id.TextVListaVazia)
                listaVazia.isVisible = true
            }
        }
    }

    private fun configuraLongListener() {
        adapter.setOnItemClickListener(object : ListaFavoritosAdapter.onItemClickListener {
            override fun onItemClick(posicao: Int) {

                if (!listaFilmes[posicao].selected) {
                    val filmeSendoEnviado = listaFilmes[posicao]
                    val intent = Intent(this@FilmesFavoritosActivity, DescricaoFilme::class.java)
                    intent.putExtra("filmeEnviado", filmeSendoEnviado)
                    startActivity(intent)
                } else {
                    listaFilmes[posicao].selected = false
                    estado--
                    listaSelecionados.remove(listaFilmes[posicao])
                    adapter.notifyDataSetChanged()
                    configuraAppBar()
                }
            }

            override fun onItemLongClick(posicao: Int) {
                if (listaFilmes[posicao].selected == false) {
                    listaFilmes[posicao].selected = true
                    estado++
                    listaSelecionados.add(listaFilmes[posicao])
                    adapter.notifyDataSetChanged()
                    configuraAppBar()
                } else {
                    listaFilmes[posicao].selected = false
                    estado--
                    listaSelecionados.remove(listaFilmes[posicao])
                    adapter.notifyDataSetChanged()
                    configuraAppBar()
                }
            }
        })
    }

    private fun configuraAppBar() {
        setTitle("Favoritos")
        criaBotaoVoltarAppBar()
        ocultaBotaoFavoritos()
        var iconeFavoritar = findViewById<ImageView>(R.id.ImageVIconeFavoritar)

        if (estado > 0) {
            iconeFavoritar.isVisible = true

            iconeFavoritar.setOnClickListener {
                for (filme in listaSelecionados) {
                    viewModel.remove(filme.id)
                    iconeFavoritar.isVisible = false
                    filme.selected = false
                    adapter.notifyDataSetChanged()
                    estado = 0
                }
            }
        } else {
            iconeFavoritar.isVisible = false
        }
    }

    private fun criaBotaoVoltarAppBar() {
        val voltar = findViewById<ImageView>(R.id.ImageVFlecha)
        voltar.setOnClickListener {
            val intent = Intent(this, ListaFilmesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun ocultaBotaoFavoritos() {
        val favoritos = findViewById<TextView>(R.id.TextVFavoritos)
        favoritos.isVisible = false
    }

}